package eduar.studymindredacao.domain.model.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TipoImagemTest {

    private static byte[] arquivo(byte[] cabecalho) {
        byte[] bytes = new byte[cabecalho.length + 32];
        System.arraycopy(cabecalho, 0, bytes, 0, cabecalho.length);
        return bytes;
    }

    private static byte[] ascii(String texto) {
        return texto.getBytes(StandardCharsets.US_ASCII);
    }

    private static byte[] jpeg() {
        return arquivo(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0});
    }

    private static byte[] png() {
        return arquivo(new byte[]{(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'});
    }

    private static byte[] webp() {
        return arquivo(ascii("RIFF\u0024\u0000\u0000\u0000WEBPVP8 "));
    }

    static Stream<Arguments> formatosAceitos() {
        return Stream.of(
                Arguments.of(jpeg(), TipoImagem.JPEG, "image/jpeg"),
                Arguments.of(png(), TipoImagem.PNG, "image/png"),
                Arguments.of(webp(), TipoImagem.WEBP, "image/webp")
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("formatosAceitos")
    void detectaOFormatoPelosPrimeirosBytes(byte[] bytes, TipoImagem esperado, String mimeType) {
        assertThat(TipoImagem.detectar(bytes)).contains(esperado);
        assertThat(esperado.mimeType()).isEqualTo(mimeType);
    }

    @Test
    void riffQueNaoEWebpNaoEAceito() {
        assertThat(TipoImagem.detectar(arquivo(ascii("RIFF\u0024\u0000\u0000\u0000WAVEfmt ")))).isEmpty();
    }

    @Test
    void outrosFormatosNaoSaoAceitos() {
        assertThat(TipoImagem.detectar(arquivo(ascii("%PDF-1.7")))).isEmpty();
        assertThat(TipoImagem.detectar(arquivo(ascii("GIF89a")))).isEmpty();
        assertThat(TipoImagem.detectar(arquivo(ascii("\u0000\u0000\u0000\u0018ftypheic")))).isEmpty();
    }

    @Test
    void textoComExtensaoDeImagemNaoEAceito() {
        assertThat(TipoImagem.detectar(ascii("isto não é uma foto.jpg"))).isEmpty();
    }

    @Test
    void arquivoMenorQueAAssinaturaNaoQuebra() {
        assertThat(TipoImagem.detectar(new byte[]{(byte) 0xFF, (byte) 0xD8})).isEmpty();
        assertThat(TipoImagem.detectar(ascii("RIFF\u0024\u0000\u0000\u0000WEB"))).isEmpty();
    }

    @Test
    void vazioOuNuloNaoEAceito() {
        assertThat(TipoImagem.detectar(new byte[0])).isEmpty();
        assertThat(TipoImagem.detectar(null)).isEmpty();
    }
}