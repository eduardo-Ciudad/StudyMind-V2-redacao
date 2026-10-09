package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.TipoEvidencia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DadosEvidenciaTest {

    @Test
    void aceitaVariosTiposDeEvidencia() {
        var dados = new DadosEvidencia(
                List.of(TipoEvidencia.OFFICIAL_STATISTICS, TipoEvidencia.NATIONAL_SURVEY), null, 2025);

        assertThat(dados.tipos()).containsExactly(TipoEvidencia.OFFICIAL_STATISTICS, TipoEvidencia.NATIONAL_SURVEY);
        assertThat(dados.temTipo()).isTrue();
    }

    @Test
    void listaNulaViraVazia() {
        var dados = new DadosEvidencia(null, null, null);

        assertThat(dados.tipos()).isEmpty();
        assertThat(dados.temTipo()).isFalse();
    }

    @Test
    void copiaAListaRecebida() {
        var tipos = new ArrayList<>(List.of(TipoEvidencia.TIME_SERIES));
        var dados = new DadosEvidencia(tipos, null, null);

        tipos.add(TipoEvidencia.CROSS_SECTIONAL);

        assertThat(dados.tipos()).containsExactly(TipoEvidencia.TIME_SERIES);
    }

    @Test
    void anoEhOpcional() {
        var dados = new DadosEvidencia(List.of(TipoEvidencia.ADMINISTRATIVE_DATA), "  ", null);

        assertThat(dados.ano()).isNull();
        assertThat(dados.populacao()).isNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {1899, 2101, 0, -2025})
    void recusaAnoForaDoIntervalo(int ano) {
        assertThatThrownBy(() -> new DadosEvidencia(List.of(), null, ano))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ano");
    }
}
