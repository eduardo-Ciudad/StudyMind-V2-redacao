package eduar.studymindredacao.domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResultadoTranscricaoTest {

    private static ResultadoTranscricao resultado(List<String> linhas) {
        return new ResultadoTranscricao(linhas, "gemini-teste", 1500, 300);
    }

    @Test
    void montaOTextoComUmaQuebraPorLinhaDaFolha() {
        var transcricao = resultado(List.of("A educação no Brasil", "enfrenta desafios históricos."));

        assertThat(transcricao.texto()).isEqualTo("A educação no Brasil\nenfrenta desafios históricos.");
    }

    @Test
    void preservaOTextoComoFoiLidoSemCorrigir() {
        var transcricao = resultado(List.of("  a sociedade nao percebeu ,que os jovens"));

        assertThat(transcricao.texto()).isEqualTo("  a sociedade nao percebeu ,que os jovens");
    }

    @Test
    void contaSoAsLinhasEscritas() {
        var transcricao = resultado(List.of("Introdução.", "", "Desenvolvimento.", "   ", "Conclusão."));

        assertThat(transcricao.qtdLinhasEscritas()).isEqualTo(3);
        assertThat(transcricao.linhas()).hasSize(5);
    }

    @Test
    void indicaAsLinhasComTrechoIlegivelContandoAPartirDeUm() {
        var transcricao = resultado(List.of(
                "Primeira linha legível.",
                "a [ilegível] da sociedade",
                "Terceira linha.",
                "[ilegível] e também [ilegível]"
        ));

        assertThat(transcricao.temTrechoIlegivel()).isTrue();
        assertThat(transcricao.linhasComTrechoIlegivel()).containsExactly(2, 4);
    }

    @Test
    void semTrechoIlegivelDevolveListaVazia() {
        var transcricao = resultado(List.of("Tudo legível."));

        assertThat(transcricao.temTrechoIlegivel()).isFalse();
        assertThat(transcricao.linhasComTrechoIlegivel()).isEmpty();
    }

    @Test
    void rejeitaTranscricaoSemLinhasOuSemTexto() {
        assertThatThrownBy(() -> resultado(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> resultado(List.of())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> resultado(List.of("", "   ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não tem texto");
    }

    @Test
    void rejeitaLinhaNula() {
        assertThatThrownBy(() -> resultado(Arrays.asList("ok", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nula");
    }

    @Test
    void rejeitaLinhaQueContemQuebraDeLinha() {
        assertThatThrownBy(() -> resultado(List.of("duas linhas\nem uma")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("única linha");
        assertThatThrownBy(() -> resultado(List.of("com retorno\r")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejeitaTokensNegativos() {
        assertThatThrownBy(() -> new ResultadoTranscricao(List.of("texto"), "gemini-teste", -1, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ResultadoTranscricao(List.of("texto"), "gemini-teste", 0, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void alterarAListaOriginalNaoAlteraOResultado() {
        List<String> linhas = new ArrayList<>(List.of("original"));
        var transcricao = resultado(linhas);

        linhas.set(0, "alterada");

        assertThat(transcricao.linhas()).containsExactly("original");
    }

    @Test
    void listasDevolvidasNaoPodemSerAlteradas() {
        var transcricao = resultado(List.of("a [ilegível]"));

        assertThatThrownBy(() -> transcricao.linhas().add("intrusa"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> transcricao.linhasComTrechoIlegivel().add(9))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
