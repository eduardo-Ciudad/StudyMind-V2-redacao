package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.Avaliacao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvaliacaoRepositoryPort {
    Avaliacao salvar(Avaliacao avaliacao);

    Optional<Avaliacao> buscarPorId(UUID id);

    void excluirPorId(UUID id);

    Optional<Avaliacao> buscarPorRedacaoId(UUID redacaoId);

    /** Busca as avaliações de várias redações numa única consulta (evita N+1 no histórico). */
    List<Avaliacao> buscarPorRedacaoIds(Collection<UUID> redacaoIds);
}
