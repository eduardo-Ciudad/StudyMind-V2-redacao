package eduar.studymindredacao.adapter.out.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptSenhaEncoderAdapterTest {
    private final BCryptSenhaEncoderAdapter encoder = new BCryptSenhaEncoderAdapter();

    @Test
    void codificaEConfereSenha() {
        String hash = encoder.codificar("senhaSegura123");

        assertThat(hash).isNotEqualTo("senhaSegura123").startsWith("$2");
        assertThat(encoder.confere("senhaSegura123", hash)).isTrue();
        assertThat(encoder.confere("senhaErrada", hash)).isFalse();
    }

    @Test
    void naoConfereComValoresNulos() {
        assertThat(encoder.confere(null, "qualquer")).isFalse();
        assertThat(encoder.confere("qualquer", null)).isFalse();
    }
}
