package eduar.studymindredacao.adapter.in.web.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class LimiteTamanhoCorpoFilterTest {
    private final LimiteTamanhoCorpoFilter filtro = new LimiteTamanhoCorpoFilter();

    @Test
    void recusaCorpoAcimaDoLimiteSemChamarOResto() throws Exception {
        var request = new MockHttpServletRequest("POST", "/auth/login");
        request.setContent(new byte[(int) LimiteTamanhoCorpoFilter.TAMANHO_MAXIMO_BYTES + 1]);
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        filtro.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(413);
        assertThat(response.getContentAsString()).contains("\"status\":413");
        assertThat(chain.getRequest()).isNull();
    }

    @Test
    void deixaPassarCorpoNormal() throws Exception {
        var request = new MockHttpServletRequest("POST", "/redacoes");
        request.setContent("{\"texto\":\"ok\"}".getBytes());
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        filtro.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }
}
