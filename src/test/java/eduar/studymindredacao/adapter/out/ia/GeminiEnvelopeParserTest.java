package eduar.studymindredacao.adapter.out.ia;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static eduar.studymindredacao.adapter.out.ia.GeminiRespostas.AVALIACAO_VALIDA;
import static eduar.studymindredacao.adapter.out.ia.GeminiRespostas.envelope;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeminiEnvelopeParserTest {
    private final GeminiEnvelopeParser parser = new GeminiEnvelopeParser(JsonMapper.builder().build());

    @Test
    void extraiTextoModeloETokens() {
        var resposta = parser.ler(envelope(AVALIACAO_VALIDA), "modelo-config");

        assertThat(resposta.textoGerado()).isEqualTo(AVALIACAO_VALIDA.trim());
        assertThat(resposta.modelo()).isEqualTo("gemini-teste-001");
        assertThat(resposta.tokensEntrada()).isEqualTo(2100);
        assertThat(resposta.tokensSaida()).isEqualTo(650);
    }

    @Test
    void somaTokensDeRaciocinioNaSaida() {
        var resposta = parser.ler(envelope(AVALIACAO_VALIDA, 900), "modelo-config");

        assertThat(resposta.tokensEntrada()).isEqualTo(2100);
        assertThat(resposta.tokensSaida()).isEqualTo(650 + 900);
    }

    @Test
    void ignoraAsPartesDeRaciocinio() {
        String comRaciocinio = """
                {"candidates":[{"content":{"parts":[
                  {"text":"pensando na nota da C1...","thought":true},
                  {"text":"{\\"a\\":1}"}
                ]}}],"modelVersion":"gemini-teste-001"}
                """;

        assertThat(parser.ler(comRaciocinio, "m").textoGerado()).isEqualTo("{\"a\":1}");
    }

    @Test
    void removeBlocoMarkdownEmVoltaDoJson() {
        var resposta = parser.ler(envelope("```json\n{\"a\":1}\n```"), "modelo-config");

        assertThat(resposta.textoGerado()).isEqualTo("{\"a\":1}");
    }

    @Test
    void usaModeloConfiguradoQuandoRespostaNaoInforma() {
        String semModelo = envelope(AVALIACAO_VALIDA).replace("\"modelVersion\":\"gemini-teste-001\"", "\"x\":1");

        assertThat(parser.ler(semModelo, "modelo-config").modelo()).isEqualTo("modelo-config");
    }

    @Test
    void cortaNomeDeModeloLongoDemais() {
        String longo = "m".repeat(GeminiEnvelopeParser.TAMANHO_MAXIMO_MODELO + 10);
        String comModeloLongo = envelope(AVALIACAO_VALIDA).replace("gemini-teste-001", longo);

        assertThat(parser.ler(comModeloLongo, "m").modelo()).hasSize(GeminiEnvelopeParser.TAMANHO_MAXIMO_MODELO);
    }

    @Test
    void rejeitaPromptBloqueado() {
        assertThatThrownBy(() -> parser.ler("{\"promptFeedback\":{\"blockReason\":\"SAFETY\"}}", "m"))
                .isInstanceOf(GeminiException.class)
                .hasMessageContaining("SAFETY");
    }

    @Test
    void rejeitaRespostaSemCandidatos() {
        assertThatThrownBy(() -> parser.ler("{\"candidates\":[]}", "m"))
                .isInstanceOf(GeminiException.class)
                .hasMessageContaining("sem candidatos");
    }

    @Test
    void rejeitaCandidatoSemTexto() {
        String cortado = "{\"candidates\":[{\"content\":{\"parts\":[]},\"finishReason\":\"MAX_TOKENS\"}]}";

        assertThatThrownBy(() -> parser.ler(cortado, "m"))
                .isInstanceOf(GeminiException.class)
                .hasMessageContaining("MAX_TOKENS");
    }

    @Test
    void rejeitaRespostaHttpQueNaoEJson() {
        assertThatThrownBy(() -> parser.ler("<html>502 Bad Gateway</html>", "m"))
                .isInstanceOf(GeminiException.class)
                .hasMessageContaining("não é JSON");
        assertThatThrownBy(() -> parser.ler("", "m"))
                .isInstanceOf(GeminiException.class);
    }

    @Test
    void falhasDeFormatoPodemSerRepetidas() {
        assertThatThrownBy(() -> parser.ler("{\"candidates\":[]}", "m"))
                .isInstanceOfSatisfying(GeminiException.class, e -> assertThat(e.podeRepetir()).isTrue());
    }

    @Test
    void removeBlocoMarkdown() {
        assertThat(GeminiEnvelopeParser.removerBlocoMarkdown("```json\n{\"a\":1}\n```")).isEqualTo("{\"a\":1}");
        assertThat(GeminiEnvelopeParser.removerBlocoMarkdown("```\n{\"a\":1}```")).isEqualTo("{\"a\":1}");
        assertThat(GeminiEnvelopeParser.removerBlocoMarkdown("  {\"a\":1}  ")).isEqualTo("{\"a\":1}");
    }
}