package eduar.studymindredacao.domain.exception;


public class ImagemInvalidaException extends RuntimeException {

    public enum Motivo {
        VAZIA,
        GRANDE_DEMAIS,
        TIPO_INVALIDO,
        QUANTIDADE_INVALIDA
    }

    private final Motivo motivo;

    public ImagemInvalidaException(Motivo motivo, String mensagem) {
        super(mensagem);
        this.motivo = motivo;
    }

    public Motivo getMotivo() {
        return motivo;
    }
}