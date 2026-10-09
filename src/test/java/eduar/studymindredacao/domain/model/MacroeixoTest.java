package eduar.studymindredacao.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MacroeixoTest {

    @Test
    void criaMacroeixoValido() {
        var macroeixo = new Macroeixo(null, "infancia-adolescencia", "Infância e adolescência", 3);

        assertThat(macroeixo.slug()).isEqualTo("infancia-adolescencia");
        assertThat(macroeixo.ordem()).isEqualTo(3);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Saude", "saúde", "saude mental", "-saude", "saude-", "saude--mental", ""})
    void recusaSlugForaDoPadrao(String slug) {
        assertThatThrownBy(() -> new Macroeixo(null, slug, "Saúde", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("slug");
    }

    @Test
    void recusaSlugLongoDemais() {
        var slug = "a".repeat(Macroeixo.TAMANHO_MAXIMO_SLUG + 1);

        assertThatThrownBy(() -> new Macroeixo(null, slug, "Saúde", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("slug");
    }

    @Test
    void exigeNome() {
        assertThatThrownBy(() -> new Macroeixo(null, "saude", " ", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nome");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void recusaOrdemNaoPositiva(int ordem) {
        assertThatThrownBy(() -> new Macroeixo(null, "saude", "Saúde", ordem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ordem");
    }
}
