package eduar.studymindredacao.adapter.out.ia;


class GeminiException extends RuntimeException {
    private final boolean podeRepetir;

    private GeminiException(String mensagem, boolean podeRepetir, Throwable causa) {
        super(mensagem, causa);
        this.podeRepetir = podeRepetir;
    }

    static GeminiException repetivel(String mensagem) {
        return new GeminiException(mensagem, true, null);
    }

    static GeminiException repetivel(String mensagem, Throwable causa) {
        return new GeminiException(mensagem, true, causa);
    }

    static GeminiException definitiva(String mensagem, Throwable causa) {
        return new GeminiException(mensagem, false, causa);
    }

    boolean podeRepetir() {
        return podeRepetir;
    }
}