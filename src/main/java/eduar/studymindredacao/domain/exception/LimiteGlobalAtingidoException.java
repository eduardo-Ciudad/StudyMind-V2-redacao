package eduar.studymindredacao.domain.exception;

import eduar.studymindredacao.domain.model.enums.RecursoIA;

/** O teto do sistema inteiro (soma de todos os usuários) de um recurso de IA foi atingido hoje. */
public class LimiteGlobalAtingidoException extends RuntimeException {
    private final RecursoIA recurso;

    public LimiteGlobalAtingidoException(RecursoIA recurso) {
        super("Limite de " + recurso.rotuloPlural() + " do sistema atingido hoje. Tente novamente amanhã.");
        this.recurso = recurso;
    }

    public RecursoIA getRecurso() {
        return recurso;
    }
}
