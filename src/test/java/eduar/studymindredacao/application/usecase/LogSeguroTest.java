package eduar.studymindredacao.application.usecase;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogSeguroTest {

    @Test
    void trocaQuebrasDeLinhaPorEspaco() {
        assertThat(LogSeguro.limpar("falha\r\n2026-10-02 INFO linha forjada")).isEqualTo("falha 2026-10-02 INFO linha forjada");
    }

    @Test
    void cortaMensagensLongas() {
        String longa = "x".repeat(LogSeguro.TAMANHO_MAXIMO + 50);

        assertThat(LogSeguro.limpar(longa)).hasSize(LogSeguro.TAMANHO_MAXIMO + 1).endsWith("…");
    }

    @Test
    void nuloViraTextoVazio() {
        assertThat(LogSeguro.limpar(null)).isEmpty();
    }
}
