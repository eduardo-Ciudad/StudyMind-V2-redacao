package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {
    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    void excluirPorId(UUID id);

    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);
}
