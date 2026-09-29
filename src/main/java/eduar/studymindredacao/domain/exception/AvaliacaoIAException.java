package eduar.studymindredacao.domain.exception;

public class AvaliacaoIAException extends RuntimeException {
    public AvaliacaoIAException(String mensagem) {
        super(mensagem);
    }

    public AvaliacaoIAException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}