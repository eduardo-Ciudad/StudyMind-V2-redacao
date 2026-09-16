package eduar.studymindredacao.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Redacao(
        UUID id,
        UUID usuarioId,
        String tipo,
        String texto,
        String status,
        OffsetDateTime enviadaEm
) {
    public Redacao {
        Validacoes.requererNaoNulo(usuarioId, "usuarioId");
        Validacoes.validarTextoObrigatorio(tipo, "tipo", 20);
        Validacoes.validarTextoObrigatorio(texto, "texto", null);
        Validacoes.validarTextoObrigatorio(status, "status", 20);
    }
}
