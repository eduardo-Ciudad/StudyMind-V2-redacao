package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.NivelExperiencia;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PerfilAluno(
        UUID id,
        UUID usuarioId,
        Short metaNota,
        NivelExperiencia nivelExperiencia,
        Integer tempoDisponivelSemanalMin,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
    public PerfilAluno {
        Validacoes.requererNaoNulo(usuarioId, "usuarioId");
        if (metaNota != null && (metaNota < 0 || metaNota > 1000)) {
            throw new IllegalArgumentException("metaNota deve estar entre 0 e 1000");
        }
        if (tempoDisponivelSemanalMin != null && tempoDisponivelSemanalMin < 0) {
            throw new IllegalArgumentException("tempoDisponivelSemanalMin não pode ser negativo");
        }
    }
}
