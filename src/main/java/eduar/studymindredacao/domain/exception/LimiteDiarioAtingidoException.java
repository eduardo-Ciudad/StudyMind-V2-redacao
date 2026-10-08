package eduar.studymindredacao.domain.exception;

import eduar.studymindredacao.domain.model.enums.RecursoIA;

/** O aluno usou todas as vagas do dia de um recurso de IA (correção ou transcrição). */
public class LimiteDiarioAtingidoException extends RuntimeException {
    private final RecursoIA recurso;
    private final int limite;

    public LimiteDiarioAtingidoException(RecursoIA recurso, int limite) {
        super("Limite diário de " + limite + " " + recurso.rotuloPlural() + " atingido. Tente novamente amanhã.");
        this.recurso = recurso;
        this.limite = limite;
    }

    public RecursoIA getRecurso() {
        return recurso;
    }

    public int getLimite() {
        return limite;
    }
}