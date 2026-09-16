package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.UsoIADiario;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface UsoIADiarioRepositoryPort {
    UsoIADiario salvar(UsoIADiario usoIADiario);

    Optional<UsoIADiario> buscarPorId(UUID id);

    Optional<UsoIADiario> buscarPorUsuarioIdEData(UUID usuarioId, LocalDate data);

    void excluirPorId(UUID id);
}
