package eduar.studymindredacao.domain.exception;

/** O teto de correções do sistema inteiro (soma de todos os usuários) foi atingido hoje. */
public class LimiteGlobalAtingidoException extends RuntimeException {
    public LimiteGlobalAtingidoException() {
        super("Limite de correções do sistema atingido hoje. Tente novamente amanhã.");
    }
}
