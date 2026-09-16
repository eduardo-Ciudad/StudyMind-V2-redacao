package eduar.studymindredacao.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Usuario(
        UUID id,
        String nome,
        String email,
        String senhaHash,
        String role,
    OffsetDateTime criadoEm
) {
    public Usuario {
        Validacoes.validarTextoObrigatorio(nome, "nome", 150);
        Validacoes.validarTextoObrigatorio(email, "email", 150);
        Validacoes.validarTextoObrigatorio(senhaHash, "senhaHash", 255);
        Validacoes.validarTextoObrigatorio(role, "role", 20);
    }
}
