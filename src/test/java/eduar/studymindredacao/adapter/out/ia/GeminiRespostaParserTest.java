package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.domain.exception.AvaliacaoIAException;
import eduar.studymindredacao.domain.model.CompetenciaAvaliada;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static eduar.studymindredacao.adapter.out.ia.GeminiRespostas.AVALIACAO_ANULADA;
import static eduar.studymindredacao.adapter.out.ia.GeminiRespostas.AVALIACAO_VALIDA;
import static eduar.studymindredacao.adapter.out.ia.GeminiRespostas.envelope;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeminiRespostaParserTest {
    private final GeminiRespostaParser parser = new GeminiRespostaParser(JsonMapper.builder().build());

    @Test
    void interpretaAvaliacaoCompleta() {
        var resultado = parser.interpretar(envelope(AVALIACAO_VALIDA), "modelo-config");

        assertThat(resultado.anulada()).isFalse();
        assertThat(resultado.notaTotal()).isEqualTo(640);
        assertThat(resultado.competencias()).extracting(CompetenciaAvaliada::numero).containsExactly(1, 2, 3, 4, 5);
        assertThat(resultado.pontosFortes()).containsExactly("tese explícita", "boa coesão");
        assertThat(resultado.diagnostico()).isEqualTo("A maior fragilidade está na C5.");
        assertThat(resultado.tokensEntrada()).isEqualTo(2100);
        assertThat(resultado.tokensSaida()).isEqualTo(650);
        assertThat(resultado.modelo()).isEqualTo("gemini-teste-001");
        assertThat(resultado.respostaBrutaJson()).startsWith("{").contains("\"diagnostico\"");
    }

    @Test
    void somaTokensDeRaciocinioNaSaida() {
        var resultado = parser.interpretar(envelope(AVALIACAO_VALIDA, 900), "modelo-config");

        assertThat(resultado.tokensEntrada()).isEqualTo(2100);
        assertThat(resultado.tokensSaida()).isEqualTo(650 + 900);
    }

    @Test
    void agrupaProblemasPorCompetencia() {
        var resultado = parser.interpretar(envelope(AVALIACAO_VALIDA), "modelo-config");

        var c5 = resultado.competencias().get(4);
        var c2 = resultado.competencias().get(1);
        var c1 = resultado.competencias().get(0);
        assertThat(c5.problemasIdentificados()).containsExactly("não define quem executa a ação", "sem meio de execução");
        assertThat(c2.problemasIdentificados()).containsExactly("cita Bauman sem relacionar ao tema");
        assertThat(c1.problemasIdentificados()).isEmpty();
    }

    @Test
    void interpretaRedacaoAnulada() {
        var resultado = parser.interpretar(envelope(AVALIACAO_ANULADA), "modelo-config");

        assertThat(resultado.anulada()).isTrue();
        assertThat(resultado.motivoAnulacao()).isEqualTo("Fuga total ao tema");
        assertThat(resultado.notaTotal()).isZero();
    }

    @Test
    void motivoNuloNaoViraTextoNull() {
        var resultado = parser.interpretar(envelope(AVALIACAO_VALIDA), "modelo-config");

        assertThat(resultado.motivoAnulacao()).isNull();
    }

    @Test
    void aceitaJsonDentroDeBlocoMarkdown() {
        var resultado = parser.interpretar(envelope("```json\n" + AVALIACAO_VALIDA + "\n```"), "modelo-config");

        assertThat(resultado.notaTotal()).isEqualTo(640);
    }

    @Test
    void usaModeloConfiguradoQuandoRespostaNaoInforma() {
        String semModelo = envelope(AVALIACAO_VALIDA).replace("\"modelVersion\":\"gemini-teste-001\"", "\"x\":1");

        assertThat(parser.interpretar(semModelo, "modelo-config").modelo()).isEqualTo("modelo-config");
    }

    @Test
    void rejeitaNotaForaDosNiveisOficiais() {
        String notaInvalida = AVALIACAO_VALIDA.replace("\"nota\": 160, \"nivel_referencia\": \"poucos desvios\"",
                "\"nota\": 150, \"nivel_referencia\": \"poucos desvios\"");

        assertThatThrownBy(() -> parser.interpretar(envelope(notaInvalida), "m"))
                .isInstanceOf(AvaliacaoIAException.class)
                .hasMessageContaining("inconsistente");
    }

    @Test
    void rejeitaAvaliacaoComMenosDeCincoCompetencias() {
        String quatro = AVALIACAO_VALIDA.replace(
                ",\n    {\"competencia\": \"C5\", \"nota\": 80, \"nivel_referencia\": \"proposta insuficiente\", \"resumo\": \"Falta agente.\"}", "");

        assertThatThrownBy(() -> parser.interpretar(envelope(quatro), "m"))
                .isInstanceOf(AvaliacaoIAException.class);
    }

    @Test
    void rejeitaTextoGeradoQueNaoEJson() {
        assertThatThrownBy(() -> parser.interpretar(envelope("Aqui está a avaliação: nota 800"), "m"))
                .isInstanceOf(AvaliacaoIAException.class)
                .hasMessageContaining("não é JSON");
    }

    @Test
    void rejeitaPromptBloqueado() {
        assertThatThrownBy(() -> parser.interpretar("{\"promptFeedback\":{\"blockReason\":\"SAFETY\"}}", "m"))
                .isInstanceOf(AvaliacaoIAException.class)
                .hasMessageContaining("SAFETY");
    }

    @Test
    void rejeitaRespostaSemCandidatos() {
        assertThatThrownBy(() -> parser.interpretar("{\"candidates\":[]}", "m"))
                .isInstanceOf(AvaliacaoIAException.class)
                .hasMessageContaining("sem candidatos");
    }

    @Test
    void removeBlocoMarkdown() {
        assertThat(GeminiRespostaParser.removerBlocoMarkdown("```json\n{\"a\":1}\n```")).isEqualTo("{\"a\":1}");
        assertThat(GeminiRespostaParser.removerBlocoMarkdown("```\n{\"a\":1}```")).isEqualTo("{\"a\":1}");
        assertThat(GeminiRespostaParser.removerBlocoMarkdown("  {\"a\":1}  ")).isEqualTo("{\"a\":1}");
    }
}
