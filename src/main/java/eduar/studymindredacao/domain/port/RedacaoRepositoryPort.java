package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedacaoRepositoryPort {
    Redacao salvar(Redacao redacao);

    Optional<Redacao> buscarPorId(UUID id);

    void excluirPorId(UUID id);

    List<Redacao> listarPorUsuarioId(UUID usuarioId);

    List<Redacao> listarPorStatusEnviadasAntesDe(StatusRedacao status, OffsetDateTime limite);

}
