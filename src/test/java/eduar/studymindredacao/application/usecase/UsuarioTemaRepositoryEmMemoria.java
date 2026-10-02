package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.port.UsuarioTemaRepositoryPort;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

class UsuarioTemaRepositoryEmMemoria implements UsuarioTemaRepositoryPort {
    private final Map<UUID, Set<UUID>> porUsuario = new HashMap<>();

    @Override
    public void adicionar(UUID usuarioId, UUID temaId) {
        porUsuario.computeIfAbsent(usuarioId, id -> new HashSet<>()).add(temaId);
    }

    @Override
    public void remover(UUID usuarioId, UUID temaId) {
        porUsuario.getOrDefault(usuarioId, new HashSet<>()).remove(temaId);
    }

    @Override
    public Set<UUID> listarTemaIds(UUID usuarioId) {
        return Set.copyOf(porUsuario.getOrDefault(usuarioId, Set.of()));
    }
}
