package eduar.studymindredacao.application.usecase;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PoliticaCadastroTest {

    @Test
    void listaVaziaComCadastroFechadoNaoPermiteNinguem() {
        var politica = PoliticaCadastro.de(false, "");

        assertThat(politica.emailsPermitidos()).isEmpty();
        assertThat(politica.permite("qualquer@exemplo.com")).isFalse();
    }

    @Test
    void normalizaAListaEIgnoraItensVazios() {
        var politica = PoliticaCadastro.de(false, " A@Exemplo.com ,, b@exemplo.com ,");

        assertThat(politica.emailsPermitidos()).containsExactlyInAnyOrder("a@exemplo.com", "b@exemplo.com");
        assertThat(politica.permite("a@exemplo.com")).isTrue();
    }

    @Test
    void cadastroAbertoPermiteTodos() {
        assertThat(PoliticaCadastro.de(true, null).permite("qualquer@exemplo.com")).isTrue();
    }
}
