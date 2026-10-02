package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.domain.port.UsuarioTemaRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Repository
public class UsuarioTemaRepositoryAdapter implements UsuarioTemaRepositoryPort {
    private final UsuarioTemaJpaRepository repository;

    public UsuarioTemaRepositoryAdapter(UsuarioTemaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void adicionar(UUID usuarioId, UUID temaId) {
        repository.adicionar(usuarioId, temaId);
    }

    @Override
    @Transactional
    public void remover(UUID usuarioId, UUID temaId) {
        repository.remover(usuarioId, temaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UUID> listarTemaIds(UUID usuarioId) {
        return Set.copyOf(repository.listarTemaIds(usuarioId));
    }
}
