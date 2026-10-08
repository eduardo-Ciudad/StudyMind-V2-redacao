package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.exception.ImagemInvalidaException;
import eduar.studymindredacao.domain.exception.ImagemInvalidaException.Motivo;
import eduar.studymindredacao.domain.model.enums.TipoImagem;

import java.util.Arrays;

public final class ImagemRedacao {
    public static final int TAMANHO_MAXIMO_BYTES = 5 * 1024 * 1024;

    private final byte[] bytes;
    private final TipoImagem tipo;

    private ImagemRedacao(byte[] bytes, TipoImagem tipo) {
        this.bytes = bytes;
        this.tipo = tipo;
    }

    public static ImagemRedacao de(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new ImagemInvalidaException(Motivo.VAZIA, "A imagem enviada está vazia.");
        }
        if (bytes.length > TAMANHO_MAXIMO_BYTES) {
            throw new ImagemInvalidaException(
                    Motivo.GRANDE_DEMAIS,
                    "A imagem deve ter no máximo " + TAMANHO_MAXIMO_BYTES / (1024 * 1024) + " MB."
            );
        }
        TipoImagem tipo = TipoImagem.detectar(bytes).orElseThrow(() -> new ImagemInvalidaException(
                Motivo.TIPO_INVALIDO,
                "Formato de imagem não suportado. Envie uma foto em JPEG, PNG ou WebP."
        ));
        return new ImagemRedacao(bytes.clone(), tipo);
    }

    public byte[] bytes() {
        return bytes.clone();
    }

    public TipoImagem tipo() {
        return tipo;
    }

    public int tamanhoBytes() {
        return bytes.length;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof ImagemRedacao imagem)) {
            return false;
        }
        return tipo == imagem.tipo && Arrays.equals(bytes, imagem.bytes);
    }

    @Override
    public int hashCode() {
        return 31 * tipo.hashCode() + Arrays.hashCode(bytes);
    }

    @Override
    public String toString() {
        return "ImagemRedacao[tipo=" + tipo + ", tamanhoBytes=" + bytes.length + "]";
    }
}
