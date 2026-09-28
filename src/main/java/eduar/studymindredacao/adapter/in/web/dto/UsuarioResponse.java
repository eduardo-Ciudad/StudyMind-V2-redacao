package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.Usuario;
import eduar.studymindredacao.domain.model.enums.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioResponse(UUID id, String nome, String email, Role role, OffsetDateTime criadoEm) {
    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.id(),
                usuario.nome(),
                usuario.email(),
                usuario.role(),
                usuario.criadoEm()
        );
    }
}
