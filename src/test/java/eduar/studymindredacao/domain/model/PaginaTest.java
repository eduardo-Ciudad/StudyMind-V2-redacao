package eduar.studymindredacao.domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaginaTest {

    @Test
    void calculaTotalDePaginasArredondandoParaCima() {
        var pagina = new Pagina<>(List.of("a", "b"), 0, 20, 63);

        assertThat(pagina.totalPaginas()).isEqualTo(4);
        assertThat(pagina.temProxima()).isTrue();
    }

    @Test
    void ultimaPaginaNaoTemProxima() {
        var pagina = new Pagina<>(List.of("a"), 3, 20, 61);

        assertThat(pagina.totalPaginas()).isEqualTo(4);
        assertThat(pagina.temProxima()).isFalse();
    }

    @Test
    void semItensTemZeroPaginas() {
        var pagina = new Pagina<String>(List.of(), 0, 20, 0);

        assertThat(pagina.totalPaginas()).isZero();
        assertThat(pagina.temProxima()).isFalse();
    }

    @Test
    void copiaAListaDeItens() {
        var origem = new ArrayList<>(List.of("a"));
        var pagina = new Pagina<>(origem, 0, 20, 1);

        origem.add("b");

        assertThat(pagina.itens()).containsExactly("a");
        assertThatThrownBy(() -> pagina.itens().add("c")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void recusaValoresInvalidos() {
        assertThatThrownBy(() -> new Pagina<String>(null, 0, 20, 0)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Pagina<>(List.of(), -1, 20, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Pagina<>(List.of(), 0, 0, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Pagina<>(List.of(), 0, 20, -1)).isInstanceOf(IllegalArgumentException.class);
    }
}
