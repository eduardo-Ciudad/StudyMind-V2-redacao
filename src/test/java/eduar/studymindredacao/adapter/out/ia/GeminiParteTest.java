package eduar.studymindredacao.adapter.out.ia;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeminiParteTest {

    @Test
    void textoRejeitaPromptVazio() {
        assertThatThrownBy(() -> new GeminiParte.Texto(" ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new GeminiParte.Texto(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void imagemConverteOsBytesParaBase64() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x01};

        var imagem = GeminiParte.Imagem.de("image/jpeg", bytes);

        assertThat(imagem.mimeType()).isEqualTo("image/jpeg");
        assertThat(Base64.getDecoder().decode(imagem.dadosBase64())).isEqualTo(bytes);
    }

    @Test
    void imagemRejeitaDadosOuTipoAusentes() {
        assertThatThrownBy(() -> GeminiParte.Imagem.de("image/jpeg", new byte[0]))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> GeminiParte.Imagem.de(null, new byte[]{1}))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void toStringDaImagemNaoExpoeOsDados() {
        var imagem = GeminiParte.Imagem.de("image/png", new byte[]{1, 2, 3});

        assertThat(imagem.toString())
                .isEqualTo("Imagem[mimeType=image/png, tamanhoBase64=4]")
                .doesNotContain(imagem.dadosBase64());
    }
}