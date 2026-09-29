package eduar.studymindredacao.domain.exception;

import java.util.UUID;

public class RedacaoNaoEncontradaException extends RuntimeException {
    public RedacaoNaoEncontradaException(UUID redacaoId) {
        super("Redação não encontrada: " + redacaoId);
    }
}