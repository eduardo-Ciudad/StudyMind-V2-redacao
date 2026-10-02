package eduar.studymindredacao.domain.exception;

import java.util.UUID;

/** Só temas possíveis (origem PREVISAO) entram e saem da lista do aluno; os demais já aparecem para todos. */
public class TemaNaoAdicionavelException extends RuntimeException {
    public TemaNaoAdicionavelException(UUID temaId) {
        super("Só temas possíveis podem ser adicionados ou removidos da sua lista: " + temaId);
    }
}
