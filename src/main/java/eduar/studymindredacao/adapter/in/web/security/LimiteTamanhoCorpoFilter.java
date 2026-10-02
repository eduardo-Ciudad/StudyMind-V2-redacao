package eduar.studymindredacao.adapter.in.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Recusa corpos grandes antes de qualquer leitura ou desserialização. Uma redação de 5.000 caracteres
 * fica muito abaixo de 64 KB. Requisições sem Content-Length (chunked) passam: o limite delas fica no proxy.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LimiteTamanhoCorpoFilter extends OncePerRequestFilter {
    static final long TAMANHO_MAXIMO_BYTES = 64 * 1024;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getContentLengthLong() > TAMANHO_MAXIMO_BYTES) {
            response.setStatus(413);
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(
                    "{\"type\":\"about:blank\",\"title\":\"Content Too Large\",\"status\":413,"
                            + "\"detail\":\"Requisição grande demais.\"}"
            );
            return;
        }
        chain.doFilter(request, response);
    }
}
