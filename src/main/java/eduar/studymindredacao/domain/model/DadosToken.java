package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.Role;

import java.util.UUID;

public record DadosToken(UUID usuarioId, String email, Role role) {
    public DadosToken {
        Validacoes.requererNaoNulo(usuarioId, "usuarioId");
        Validacoes.validarTextoObrigatorio(email, "email", 150);
        Validacoes.requererNaoNulo(role, "role");
    }
}
