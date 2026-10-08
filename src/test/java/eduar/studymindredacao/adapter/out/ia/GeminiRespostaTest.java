package eduar.studymindredacao.adapter.out.ia;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeminiRespostaTest {

    @Test
    void guardaTextoModeloETokens() {
        var resposta = new GeminiResposta("{\"a\":1}", "gemini-teste-001", 2100, 650);

        assertThat(resposta.textoGerado()).isEqualTo("{\"a\":1}");
        assertThat(resposta.modelo()).isEqualTo("gemini-teste-001");
        assertThat(resposta.tokensEntrada()).isEqualTo(2100);
        assertThat(resposta.tokensSaida()).isEqualTo(650);
    }

    @Test
    void rejeitaTextoVazio() {
        assertThatThrownBy(() -> new GeminiResposta(" ", "m", 0, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new GeminiResposta(null, "m", 0, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejeitaModeloVazio() {
        assertThatThrownBy(() -> new GeminiResposta("{}", null, 0, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejeitaTokensNegativos() {
        assertThatThrownBy(() -> new GeminiResposta("{}", "m", -1, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new GeminiResposta("{}", "m", 0, -1)).isInstanceOf(IllegalArgumentException.class);
    }
}