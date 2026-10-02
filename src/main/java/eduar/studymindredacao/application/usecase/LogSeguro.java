package eduar.studymindredacao.application.usecase;

/**
 * Prepara texto vindo de fora (mensagens do Gemini, de exceções do cliente HTTP) para ir ao log:
 * troca quebras de linha e caracteres de controle por espaço, para ninguém forjar linhas falsas,
 * e corta mensagens longas.
 */
public final class LogSeguro {
    static final int TAMANHO_MAXIMO = 300;

    private LogSeguro() {
    }

    public static String limpar(String mensagem) {
        if (mensagem == null) {
            return "";
        }
        String semControle = mensagem.replaceAll("[\\p{Cntrl}\\u2028\\u2029]+", " ").strip();
        return semControle.length() > TAMANHO_MAXIMO
                ? semControle.substring(0, TAMANHO_MAXIMO) + "…"
                : semControle;
    }
}
