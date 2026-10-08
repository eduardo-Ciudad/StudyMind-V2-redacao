package eduar.studymindredacao.domain.exception;


public class ImagemNaoReconhecidaException extends RuntimeException {
    public static final String MENSAGEM =
            "Não encontramos uma redação manuscrita legível nesta foto. "
                    + "Tire outra com a folha inteira no quadro, boa luz e sem sombra.";

    public ImagemNaoReconhecidaException() {
        super(MENSAGEM);
    }
}