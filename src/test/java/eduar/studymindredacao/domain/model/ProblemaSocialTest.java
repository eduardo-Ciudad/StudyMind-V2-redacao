package eduar.studymindredacao.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProblemaSocialTest {

    @Test
    void criaProblemaValido() {
        var problema = new ProblemaSocial(null, "saude-mental", "Saúde mental", "saude");

        assertThat(problema.slug()).isEqualTo("saude-mental");
        assertThat(problema.macroeixoSlug()).isEqualTo("saude");
    }

    @Test
    void recusaSlugForaDoPadrao() {
        assertThatThrownBy(() -> new ProblemaSocial(null, "Saúde Mental", "Saúde mental", "saude"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("slug");
    }

    @Test
    void exigeNome() {
        assertThatThrownBy(() -> new ProblemaSocial(null, "saude-mental", null, "saude"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nome");
    }

    @Test
    void exigeMacroeixo() {
        assertThatThrownBy(() -> new ProblemaSocial(null, "saude-mental", "Saúde mental", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("macroeixoSlug");
    }
}
