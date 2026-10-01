package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.port.RedacaoRepositoryPort;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class RedacaoRepositoryEmMemoria implements RedacaoRepositoryPort {
    private final Map<UUID, Redacao> redacoes = new LinkedHashMap<>();

    @Override
    public Redacao salvar(Redacao redacao) {
        UUID id = redacao.id() != null ? redacao.id() : UUID.randomUUID();
        OffsetDateTime enviadaEm = redacao.enviadaEm() != null ? redacao.enviadaEm() : OffsetDateTime.now();
        var salva = new Redacao(id, redacao.usuarioId(), redacao.temaId(), redacao.tipo(), redacao.texto(), redacao.status(), enviadaEm);
        redacoes.put(id, salva);
        return salva;
    }

    @Override
    public Optional<Redacao> buscarPorId(UUID id) {
        return Optional.ofNullable(redacoes.get(id));
    }

    @Override
    public List<Redacao> listarPorUsuarioId(UUID usuarioId) {
        return redacoes.values().stream()
                .filter(r -> r.usuarioId().equals(usuarioId))
                .sorted(Comparator.comparing(Redacao::enviadaEm).reversed())
                .toList();
    }

    @Override
    public void excluirPorId(UUID id) {
        redacoes.remove(id);
    }

    List<Redacao> todas() {
        return List.copyOf(redacoes.values());
    }

    @Override
    public List<Redacao> listarPorStatusEnviadasAntesDe(StatusRedacao status, OffsetDateTime limite) {
        return redacoes.values().stream()
                .filter(r -> r.status() == status && r.enviadaEm().isBefore(limite))
                .toList();
    }
}
