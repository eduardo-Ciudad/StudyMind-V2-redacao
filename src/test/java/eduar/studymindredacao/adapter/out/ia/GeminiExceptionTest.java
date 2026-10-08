package eduar.studymindredacao.adapter.out.ia;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GeminiExceptionTest {

    @Test
    void repetivelPermiteNovaTentativa() {
        var excecao = GeminiException.repetivel("resposta sem candidatos");

        assertThat(excecao.podeRepetir()).isTrue();
        assertThat(excecao).hasMessage("resposta sem candidatos").hasNoCause();
    }

    @Test
    void repetivelGuardaACausa() {
        var causa = new IOException("timeout");

        var excecao = GeminiException.repetivel("Gemini indisponível", causa);

        assertThat(excecao.podeRepetir()).isTrue();
        assertThat(excecao).hasCause(causa);
    }

    @Test
    void definitivaNaoPermiteNovaTentativa() {
        var causa = new IllegalStateException("HTTP 429");

        var excecao = GeminiException.definitiva("Gemini recusou a requisição (HTTP 429)", causa);

        assertThat(excecao.podeRepetir()).isFalse();
        assertThat(excecao).hasMessageContaining("HTTP 429").hasCause(causa);
    }
}