package eduar.studymindredacao.adapter.out.persistence.repository;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepertorioRepositoryAdapterTest {

    @Test
    void escapaCuringasDoLike() {
        assertThat(RepertorioRepositoryAdapter.escaparLike("50%")).isEqualTo("50\\%");
        assertThat(RepertorioRepositoryAdapter.escaparLike("a_b")).isEqualTo("a\\_b");
        assertThat(RepertorioRepositoryAdapter.escaparLike("c\\d")).isEqualTo("c\\\\d");
    }

    @Test
    void mantemTextoComumENulo() {
        assertThat(RepertorioRepositoryAdapter.escaparLike("constituição")).isEqualTo("constituição");
        assertThat(RepertorioRepositoryAdapter.escaparLike(null)).isNull();
    }
}
