package eduar.studymindredacao.domain.exception;

public class LimiteDiarioAtingidoException extends RuntimeException {
    private final int limite;

    public LimiteDiarioAtingidoException(int limite) {
        super("Limite diário de " + limite + " correções atingido. Tente novamente amanhã.");
        this.limite = limite;
    }

    public int getLimite() {
        return limite;
    }
}