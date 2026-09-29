package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.Avaliacao;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * O que o aluno vê da avaliação. Ficam de fora dados internos:
 * modelo da IA, tokens e a resposta bruta do Gemini.
 */
public record AvaliacaoResponse(
        short notaTotal,
        boolean anulada,
        String motivoAnulacao,
        List<CompetenciaResponse> competencias,
        List<String> pontosFortes,
        List<String> pontosDesenvolvimento,
        String diagnostico,
        OffsetDateTime avaliadoEm
) {
    public static AvaliacaoResponse de(Avaliacao avaliacao) {
        if (avaliacao == null) {
            return null;
        }
        return new AvaliacaoResponse(
                avaliacao.notaTotal(),
                avaliacao.anulada(),
                avaliacao.motivoAnulacao(),
                avaliacao.competencias().stream().map(CompetenciaResponse::de).toList(),
                avaliacao.pontosFortes(),
                avaliacao.pontosDesenvolvimento(),
                avaliacao.diagnostico(),
                avaliacao.avaliadoEm()
        );
    }
}
