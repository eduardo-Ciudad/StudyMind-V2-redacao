package eduar.studymindredacao.domain.exception;

import java.util.UUID;

public class TemaNaoEncontradoException extends RuntimeException {
    public TemaNaoEncontradoException(UUID temaId) {
        super("Tema não encontrado ou inativo: " + temaId);
    }
}