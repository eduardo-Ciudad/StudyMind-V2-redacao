package eduar.studymindredacao.domain.port;

import java.util.Set;
import java.util.UUID;

/** Temas possíveis que cada aluno adicionou à própria lista. */
public interface UsuarioTemaRepositoryPort {
    /** Idempotente: adicionar um tema que já está na lista não faz nada. */
    void adicionar(UUID usuarioId, UUID temaId);

    /** Idempotente: remover um tema que não está na lista não faz nada. */
    void remover(UUID usuarioId, UUID temaId);

    Set<UUID> listarTemaIds(UUID usuarioId);
}
