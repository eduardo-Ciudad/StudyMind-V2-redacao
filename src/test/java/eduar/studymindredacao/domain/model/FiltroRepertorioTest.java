package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.FuncaoArgumentativa;
import eduar.studymindredacao.domain.model.enums.TipoEntidade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FiltroRepertorioTest {

    @Test
    void semFiltrosUsaTamanhoPadrao() {
        var filtro = FiltroRepertorio.semFiltros(2);

        assertThat(filtro.pagina()).isEqualTo(2);
        assertThat(filtro.tamanho()).isEqualTo(FiltroRepertorio.TAMANHO_PADRAO);
        assertThat(filtro.macroeixo()).isNull();
        assertThat(filtro.busca()).isNull();
    }

    @Test
    void aceitaTodosOsFiltrosJuntos() {
        var filtro = new FiltroRepertorio("saude", "saude-mental", TipoEntidade.PERSON,
                FuncaoArgumentativa.EXPLAIN_CAUSE, "estigma", 0, 50);

        assertThat(filtro.macroeixo()).isEqualTo("saude");
        assertThat(filtro.problema()).isEqualTo("saude-mental");
        assertThat(filtro.tipo()).isEqualTo(TipoEntidade.PERSON);
        assertThat(filtro.funcao()).isEqualTo(FuncaoArgumentativa.EXPLAIN_CAUSE);
        assertThat(filtro.busca()).isEqualTo("estigma");
    }

    @Test
    void textosEmBrancoViramNulo() {
        var filtro = new FiltroRepertorio(" ", "", null, null, "   ", 0, 20);

        assertThat(filtro.macroeixo()).isNull();
        assertThat(filtro.problema()).isNull();
        assertThat(filtro.busca()).isNull();
    }

    @Test
    void buscaSemEspacosNasPontas() {
        var filtro = new FiltroRepertorio(null, null, null, null, "  constituição  ", 0, 20);

        assertThat(filtro.busca()).isEqualTo("constituição");
    }

    @Test
    void aceitaBuscaNoLimite() {
        var filtro = new FiltroRepertorio(null, null, null, null, "a".repeat(100), 0, 20);

        assertThat(filtro.busca()).hasSize(100);
    }

    @Test
    void recusaBuscaLongaDemais() {
        assertThatThrownBy(() -> new FiltroRepertorio(null, null, null, null, "a".repeat(101), 0, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("busca");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Saude", "saude mental", "saúde", "-saude", "saude--mental"})
    void recusaSlugInvalido(String slug) {
        assertThatThrownBy(() -> new FiltroRepertorio(slug, null, null, null, null, 0, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("macroeixo");
        assertThatThrownBy(() -> new FiltroRepertorio(null, slug, null, null, null, 0, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("problema");
    }

    @Test
    void recusaPaginaNegativa() {
        assertThatThrownBy(() -> new FiltroRepertorio(null, null, null, null, null, -1, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pagina");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 51, -5})
    void recusaTamanhoForaDoIntervalo(int tamanho) {
        assertThatThrownBy(() -> new FiltroRepertorio(null, null, null, null, null, 0, tamanho))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tamanho");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50})
    void aceitaTamanhoNosLimites(int tamanho) {
        assertThat(new FiltroRepertorio(null, null, null, null, null, 0, tamanho).tamanho()).isEqualTo(tamanho);
    }
}
