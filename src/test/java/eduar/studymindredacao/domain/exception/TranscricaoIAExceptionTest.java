package eduar.studymindredacao.domain.exception;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TranscricaoIAExceptionTest {

    @Test
    void guardaACausaOriginalParaOLog() {
        var causa = new IOException("timeout de leitura");

        var excecao = new TranscricaoIAException("Gemini indisponível", causa);

        assertThat(excecao).hasMessage("Gemini indisponível").hasCause(causa);
    }

    @Test
    void podeSerCriadaSemCausa() {
        assertThat(new TranscricaoIAException("resposta sem candidatos"))
                .hasMessage("resposta sem candidatos")
                .hasNoCause();
    }

    @Test
    void naoEConfundidaComFalhaDaAvaliacao() {
        assertThat(new TranscricaoIAException("x")).isNotInstanceOf(AvaliacaoIAException.class);
    }
}