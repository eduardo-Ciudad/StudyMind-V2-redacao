package eduar.studymindredacao.application.usecase;

import java.util.List;

/**
 * Visão de evolução do aluno.
 * mediaRecente e competenciaFoco são null enquanto não houver redação corrigida e não anulada;
 * competenciaFoco também é null quando todas as médias estão em 200.
 */
public record Evolucao(
        int totalCorrigidas,
        Integer melhorNota,
        NotasPorCompetencia mediaRecente,
        Integer competenciaFoco,
        List<PontoEvolucao> serie
) {
    public Evolucao {
        serie = List.copyOf(serie);
    }
}
