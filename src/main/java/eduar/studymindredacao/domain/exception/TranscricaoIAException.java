package eduar.studymindredacao.domain.exception;


public class TranscricaoIAException extends RuntimeException {
    public TranscricaoIAException(String mensagem) {
        super(mensagem);
    }

    public TranscricaoIAException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}