package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Usuario(
        UUID id,
        String nome,
        String email,
        String senhaHash,
        Role role,
        OffsetDateTime criadoEm
) {
    public Usuario {
        Validacoes.validarTextoObrigatorio(nome, "nome", 150);
        Validacoes.validarTextoObrigatorio(email, "email", 150);
        Validacoes.validarTextoObrigatorio(senhaHash, "senhaHash", 255);
        email = email.trim().toLowerCase();
        if (role == null) {
            role = Role.ALUNO;
        }
    }
}
