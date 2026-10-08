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

    @Test
    void deixaPassarFotoGrandeNoUploadDaTranscricao() throws Exception {
        var request = new MockHttpServletRequest("POST", "/transcricoes");
        request.setContent(new byte[3 * 1024 * 1024]);
        var chain = new MockFilterChain();

        filtro.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }

    @Test
    void recusaUploadAcimaDoLimiteDaTranscricao() throws Exception {
        var request = new MockHttpServletRequest("POST", "/transcricoes");
        request.setContent(new byte[(int) LimiteTamanhoCorpoFilter.TAMANHO_MAXIMO_UPLOAD_BYTES + 1]);
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        filtro.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(413);
        assertThat(chain.getRequest()).isNull();
    }

    @Test
    void limiteDoUploadNaoValeParaOutrasRotas() throws Exception {
        var request = new MockHttpServletRequest("POST", "/redacoes");
        request.setContent(new byte[3 * 1024 * 1024]);
        var response = new MockHttpServletResponse();

        filtro.doFilter(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(413);
    }
}
