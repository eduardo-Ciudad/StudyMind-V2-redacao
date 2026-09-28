package eduar.studymindredacao.adapter.in.web.security;

import eduar.studymindredacao.domain.model.enums.Role;

import java.util.UUID;

public record UsuarioAutenticado(UUID id, String email, Role role) {
}
