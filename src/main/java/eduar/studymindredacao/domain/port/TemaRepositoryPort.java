package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.Tema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemaRepositoryPort {
    Tema salvar(Tema tema);

    Optional<Tema> buscarPorId(UUID id);

    List<Tema> listarAtivos();
}