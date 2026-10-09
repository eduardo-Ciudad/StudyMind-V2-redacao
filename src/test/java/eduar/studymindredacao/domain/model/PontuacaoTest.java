package eduar.studymindredacao.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PontuacaoTest {

    @Test
    void aceitaNotasDentroDaEscala() {
        var pontuacao = new Pontuacao(4, 5, 4, 5, 5, 5);

        assertThat(pontuacao.aplicabilidade()).isEqualTo(5);
        assertThat(pontuacao.versatilidade()).isEqualTo(4);
    }

    @Test
    void aceitaOsLimitesDaEscala() {
        var pontuacao = new Pontuacao(1, 1, 1, 5, 5, 5);

        assertThat(pontuacao.versatilidade()).isEqualTo(1);
        assertThat(pontuacao.originalidade()).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6, -1})
    void recusaNotaForaDaEscala(int nota) {
        assertThatThrownBy(() -> new Pontuacao(3, 3, 3, nota, 3, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("aplicabilidade");
    }
}
