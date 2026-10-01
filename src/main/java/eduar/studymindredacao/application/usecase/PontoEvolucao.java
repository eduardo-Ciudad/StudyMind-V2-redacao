package eduar.studymindredacao.application.usecase;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Uma redação corrigida na linha do tempo. Redação anulada não tem notas por competência. */
public record PontoEvolucao(
        UUID redacaoId,
        OffsetDateTime enviadaEm,
        String temaTitulo,
        boolean anulada,
        int notaTotal,
        NotasPorCompetencia notas
) {
}
