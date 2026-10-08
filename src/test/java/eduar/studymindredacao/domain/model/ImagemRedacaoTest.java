package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.exception.ImagemInvalidaException;
import eduar.studymindredacao.domain.exception.ImagemInvalidaException.Motivo;
import eduar.studymindredacao.domain.model.enums.TipoImagem;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static eduar.studymindredacao.domain.model.ImagensDeTeste.jpeg;
import static eduar.studymindredacao.domain.model.ImagensDeTeste.jpegComTamanho;
import static eduar.studymindredacao.domain.model.ImagensDeTeste.png;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImagemRedacaoTest {

    @Test
    void criaAPartirDosBytesComOTipoDetectado() {
        var imagem = ImagemRedacao.de(png());

        assertThat(imagem.tipo()).isEqualTo(TipoImagem.PNG);
        assertThat(imagem.tamanhoBytes()).isEqualTo(64);
        assertThat(imagem.bytes()).isEqualTo(png());
    }

    @Test
    void rejeitaImagemVaziaOuNula() {
        assertThatThrownBy(() -> ImagemRedacao.de(new byte[0]))
                .isInstanceOf(ImagemInvalidaException.class)
                .extracting("motivo").isEqualTo(Motivo.VAZIA);
        assertThatThrownBy(() -> ImagemRedacao.de(null))
                .isInstanceOf(ImagemInvalidaException.class)
                .extracting("motivo").isEqualTo(Motivo.VAZIA);
    }

    @Test
    void aceitaImagemExatamenteNoLimite() {
        var imagem = ImagemRedacao.de(jpegComTamanho(ImagemRedacao.TAMANHO_MAXIMO_BYTES));

        assertThat(imagem.tamanhoBytes()).isEqualTo(ImagemRedacao.TAMANHO_MAXIMO_BYTES);
    }

    @Test
    void rejeitaImagemAcimaDoLimite() {
        assertThatThrownBy(() -> ImagemRedacao.de(jpegComTamanho(ImagemRedacao.TAMANHO_MAXIMO_BYTES + 1)))
                .isInstanceOf(ImagemInvalidaException.class)
                .hasMessageContaining("5 MB")
                .extracting("motivo").isEqualTo(Motivo.GRANDE_DEMAIS);
    }

    @Test
    void rejeitaArquivoQueNaoEImagemSuportada() {
        byte[] pdf = "%PDF-1.7 conteúdo".getBytes(StandardCharsets.UTF_8);

        assertThatThrownBy(() -> ImagemRedacao.de(pdf))
                .isInstanceOf(ImagemInvalidaException.class)
                .extracting("motivo").isEqualTo(Motivo.TIPO_INVALIDO);
    }

    @Test
    void alterarOArrayOriginalNaoAlteraAImagem() {
        byte[] original = jpeg();
        var imagem = ImagemRedacao.de(original);

        original[0] = 0;

        assertThat(imagem.bytes()[0]).isEqualTo((byte) 0xFF);
    }

    @Test
    void alterarOsBytesDevolvidosNaoAlteraAImagem() {
        var imagem = ImagemRedacao.de(jpeg());

        imagem.bytes()[0] = 0;

        assertThat(imagem.bytes()[0]).isEqualTo((byte) 0xFF);
    }

    @Test
    void imagensComOMesmoConteudoSaoIguais() {
        var primeira = ImagemRedacao.de(jpeg());
        var segunda = ImagemRedacao.de(jpeg());

        assertThat(primeira).isEqualTo(segunda).hasSameHashCodeAs(segunda);
        assertThat(primeira).isNotEqualTo(ImagemRedacao.de(png()));
    }

    @Test
    void toStringNaoExpoeOConteudo() {
        var imagem = ImagemRedacao.de(jpeg());

        assertThat(imagem).hasToString("ImagemRedacao[tipo=JPEG, tamanhoBytes=64]");
    }
}