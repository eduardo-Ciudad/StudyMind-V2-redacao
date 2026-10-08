package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.ResultadoTranscricao;

import java.util.List;

/**
 * Texto lido da foto, para a tela de revisão (#25). As linhas seguem a folha; os números em
 * linhasComTrechoIlegivel começam em 1 e apontam onde a IA escreveu [ilegível].
 * Modelo e tokens ficam de fora de propósito: são detalhe interno de custo.
 */
public record TranscricaoResponse(
        String texto,
        List<String> linhas,
        List<Integer> linhasComTrechoIlegivel,
        int qtdLinhas
) {
    public static TranscricaoResponse de(ResultadoTranscricao resultado) {
        return new TranscricaoResponse(
                resultado.texto(),
                resultado.linhas(),
                resultado.linhasComTrechoIlegivel(),
                resultado.qtdLinhasEscritas()
        );
    }
}
