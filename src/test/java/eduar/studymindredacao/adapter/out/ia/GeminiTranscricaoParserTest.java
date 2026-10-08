package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.domain.exception.ImagemNaoReconhecidaException;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeminiTranscricaoParserTest {
    private final GeminiTranscricaoParser parser = new GeminiTranscricaoParser(JsonMapper.builder().build());

    /** Resposta já desembrulhada pelo envelope, como o GeminiClient entrega. */
    private static GeminiResposta resposta(String textoGerado) {
        return new GeminiResposta(textoGerado, "gemini-teste-001", 1800, 400);
    }

    private static GeminiResposta redacao(String linhasJson) {
        return resposta("{\"eh_redacao\": true, \"motivo\": null, \"linhas\": " + linhasJson + "}");
    }

    @Test
    void interpretaAsLinhasComModeloETokens() {
        var resultado = parser.interpretar(redacao("[\"A educação no Brasil\", \"enfrenta desafios.\"]"));

        assertThat(resultado.linhas()).containsExactly("A educação no Brasil", "enfrenta desafios.");
        assertThat(resultado.modelo()).isEqualTo("gemini-teste-001");
        assertThat(resultado.tokensEntrada()).isEqualTo(1800);
        assertThat(resultado.tokensSaida()).isEqualTo(400);
    }

    @Test
    void naoAlteraOTextoDoAluno() {
        var resultado = parser.interpretar(redacao("[\"  a sociedade nao percebeu ,que os jovens\"]"));

        assertThat(resultado.linhas()).containsExactly("  a sociedade nao percebeu ,que os jovens");
    }

    @Test
    void separaItemQueJuntouDuasLinhasDaFolha() {
        var resultado = parser.interpretar(redacao("[\"primeira linha\\nsegunda linha\", \"terceira\\r\\nquarta\"]"));

        assertThat(resultado.linhas()).containsExactly("primeira linha", "segunda linha", "terceira", "quarta");
    }

    @Test
    void removeLinhasVaziasSoDasPontas() {
        var resultado = parser.interpretar(redacao("[\"\", \"Introdução.\", \"\", \"Desenvolvimento.\", \"  \"]"));

        assertThat(resultado.linhas()).containsExactly("Introdução.", "", "Desenvolvimento.");
    }

    @Test
    void padronizaVariacoesDoMarcadorDeIlegivel() {
        var resultado = parser.interpretar(redacao("[\"a [ilegivel] da\", \"o [ ILEGÍVEL ] e o [Ilegível]\"]"));

        assertThat(resultado.linhas()).containsExactly("a [ilegível] da", "o [ilegível] e o [ilegível]");
        assertThat(resultado.linhasComTrechoIlegivel()).containsExactly(1, 2);
    }

    @Test
    void imagemQueNaoERedacaoNaoViraFalhaDeIA() {
        var naoERedacao = resposta("{\"eh_redacao\": false, \"motivo\": \"foto de um gato\", \"linhas\": []}");

        assertThatThrownBy(() -> parser.interpretar(naoERedacao))
                .isInstanceOf(ImagemNaoReconhecidaException.class)
                .hasMessage(ImagemNaoReconhecidaException.MENSAGEM);
    }

    @Test
    void redacaoSemNenhumaLinhaEscritaTambemNaoEReconhecida() {
        assertThatThrownBy(() -> parser.interpretar(redacao("[]")))
                .isInstanceOf(ImagemNaoReconhecidaException.class);
        assertThatThrownBy(() -> parser.interpretar(redacao("[\"\", \"   \"]")))
                .isInstanceOf(ImagemNaoReconhecidaException.class);
    }

    @Test
    void rejeitaTextoQueNaoEJson() {
        assertThatThrownBy(() -> parser.interpretar(resposta("Aqui está a transcrição: ...")))
                .isInstanceOfSatisfying(GeminiException.class, e -> assertThat(e.podeRepetir()).isTrue())
                .hasMessageContaining("não é JSON");
    }

    @Test
    void rejeitaRespostaSemOCampoEhRedacao() {
        assertThatThrownBy(() -> parser.interpretar(resposta("{\"linhas\": [\"texto\"]}")))
                .isInstanceOf(GeminiException.class)
                .hasMessageContaining("eh_redacao");
    }

    @Test
    void rejeitaRespostaSemListaDeLinhas() {
        assertThatThrownBy(() -> parser.interpretar(resposta("{\"eh_redacao\": true, \"linhas\": \"texto corrido\"}")))
                .isInstanceOf(GeminiException.class)
                .hasMessageContaining("lista de linhas");
    }

    @Test
    void rejeitaLinhaQueNaoETexto() {
        assertThatThrownBy(() -> parser.interpretar(redacao("[\"ok\", {\"texto\": \"objeto\"}]")))
                .isInstanceOf(GeminiException.class)
                .hasMessageContaining("não é texto");
        assertThatThrownBy(() -> parser.interpretar(redacao("[\"ok\", null]")))
                .isInstanceOf(GeminiException.class);
    }
}