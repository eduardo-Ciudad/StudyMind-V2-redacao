package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.Avaliacao;

import java.util.Optional;
import java.util.UUID;

public interface AvaliacaoRepositoryPort {
    Avaliacao salvar(Avaliacao avaliacao);

    Optional<Avaliacao> buscarPorId(UUID id);

    void excluirPorId(UUID id);

    Optional<Avaliacao> buscarPorRedacaoId(UUID redacaoId);
}
