package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.PerfilAluno;

import java.util.Optional;
import java.util.UUID;

public interface PerfilAlunoRepositoryPort {
    PerfilAluno salvar(PerfilAluno perfilAluno);

    Optional<PerfilAluno> buscarPorId(UUID id);

    void excluirPorId(UUID id);
}
