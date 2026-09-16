package eduar.studymindredacao.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PerfilAluno(
        UUID id,
        UUID usuarioId,
        Short metaNota,
        String nivelExperiencia,
        Integer tempoDisponivelSemanalMin,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
    public PerfilAluno {
        Validacoes.requererNaoNulo(usuarioId, "usuarioId");
        Validacoes.validarTamanhoMaximo(nivelExperiencia, "nivelExperiencia", 20);
    }
}
