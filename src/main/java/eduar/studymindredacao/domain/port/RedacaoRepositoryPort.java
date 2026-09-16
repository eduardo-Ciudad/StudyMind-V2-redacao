package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.Redacao;

import java.util.Optional;
import java.util.UUID;

public interface RedacaoRepositoryPort {
    Redacao salvar(Redacao redacao);

    Optional<Redacao> buscarPorId(UUID id);

    void excluirPorId(UUID id);
}
