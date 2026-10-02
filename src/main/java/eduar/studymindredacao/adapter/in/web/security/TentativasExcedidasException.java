package eduar.studymindredacao.adapter.in.web.security;

/** Tentativas demais em pouco tempo (login ou cadastro). Vira 429 com Retry-After. */
public class TentativasExcedidasException extends RuntimeException {
    private final long segundosParaTentarDeNovo;

    public TentativasExcedidasException(long segundosParaTentarDeNovo) {
        super("Muitas tentativas. Aguarde um pouco e tente novamente.");
        this.segundosParaTentarDeNovo = segundosParaTentarDeNovo;
    }

    public long getSegundosParaTentarDeNovo() {
        return segundosParaTentarDeNovo;
    }
}
