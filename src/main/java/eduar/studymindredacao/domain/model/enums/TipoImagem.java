package eduar.studymindredacao.domain.model.enums;

import java.util.Optional;


public enum TipoImagem {
    JPEG("image/jpeg"),
    PNG("image/png"),
    WEBP("image/webp");

    private static final byte[] ASSINATURA_JPEG = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] ASSINATURA_PNG = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};
    private static final byte[] ASSINATURA_RIFF = {'R', 'I', 'F', 'F'};
    private static final byte[] ASSINATURA_WEBP = {'W', 'E', 'B', 'P'};

    private final String mimeType;

    TipoImagem(String mimeType) {
        this.mimeType = mimeType;
    }

    public String mimeType() {
        return mimeType;
    }

    public static Optional<TipoImagem> detectar(byte[] bytes) {
        if (bytes == null) {
            return Optional.empty();
        }
        if (comecaCom(bytes, ASSINATURA_JPEG, 0)) {
            return Optional.of(JPEG);
        }
        if (comecaCom(bytes, ASSINATURA_PNG, 0)) {
            return Optional.of(PNG);
        }
        if (comecaCom(bytes, ASSINATURA_RIFF, 0) && comecaCom(bytes, ASSINATURA_WEBP, 8)) {
            return Optional.of(WEBP);
        }
        return Optional.empty();
    }

    private static boolean comecaCom(byte[] bytes, byte[] assinatura, int deslocamento) {
        if (bytes.length < deslocamento + assinatura.length) {
            return false;
        }
        for (int i = 0; i < assinatura.length; i++) {
            if (bytes[deslocamento + i] != assinatura[i]) {
                return false;
            }
        }
        return true;
    }
}