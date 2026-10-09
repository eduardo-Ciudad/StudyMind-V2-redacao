package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.TipoFonte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FonteTest {

    @Test
    void aceitaFonteComUrl() {
        var fonte = new Fonte(TipoFonte.GENERAL, "IBGE", "https://www.ibge.gov.br/");

        assertThat(fonte.url()).isEqualTo("https://www.ibge.gov.br/");
    }

    @Test
    void aceitaFonteSemUrl() {
        var fonte = new Fonte(TipoFonte.GENERAL, "Atlas da Violência 2026", null);

        assertThat(fonte.url()).isNull();
    }

    @Test
    void urlEmBrancoViraNula() {
        var fonte = new Fonte(TipoFonte.PROFILE, "Currículo Lattes", "   ");

        assertThat(fonte.url()).isNull();
    }

    @Test
    void exigeTipo() {
        assertThatThrownBy(() -> new Fonte(null, "IBGE", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("tipo");
    }

    @Test
    void exigeDescricao() {
        assertThatThrownBy(() -> new Fonte(TipoFonte.GENERAL, " ", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("descricao");
    }

    @ParameterizedTest
    @ValueSource(strings = {"javascript:alert(1)", "ftp://exemplo.com", "www.ibge.gov.br"})
    void recusaUrlQueNaoSejaHttp(String url) {
        assertThatThrownBy(() -> new Fonte(TipoFonte.GENERAL, "Fonte", url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("url");
    }

    @Test
    void recusaUrlLongaDemais() {
        var url = "https://exemplo.com/" + "a".repeat(Fonte.TAMANHO_MAXIMO_URL);

        assertThatThrownBy(() -> new Fonte(TipoFonte.GENERAL, "Fonte", url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("url");
    }
}
