package eduar.studymindredacao.domain.exception;

/** O cadastro está fechado e o e-mail não está na lista de convidados. */
public class CadastroFechadoException extends RuntimeException {
    public CadastroFechadoException() {
        super("Cadastro fechado. Peça um convite.");
    }
}
