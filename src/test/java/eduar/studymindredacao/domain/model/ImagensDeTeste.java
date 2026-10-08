package eduar.studymindredacao.domain.model;

public final class ImagensDeTeste {
    private static final byte[] CABECALHO_JPEG = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0};
    private static final byte[] CABECALHO_PNG = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};

    private ImagensDeTeste() {
    }

    public static byte[] jpeg() {
        return jpegComTamanho(64);
    }

    public static byte[] jpegComTamanho(int tamanho) {
        return comCabecalho(CABECALHO_JPEG, tamanho);
    }

    public static byte[] png() {
        return comCabecalho(CABECALHO_PNG, 64);
    }

    private static byte[] comCabecalho(byte[] cabecalho, int tamanho) {
        byte[] bytes = new byte[tamanho];
        System.arraycopy(cabecalho, 0, bytes, 0, cabecalho.length);
        return bytes;
    }
}