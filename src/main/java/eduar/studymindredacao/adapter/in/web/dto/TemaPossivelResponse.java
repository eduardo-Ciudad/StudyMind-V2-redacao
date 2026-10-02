package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.application.usecase.TemaPossivel;
import eduar.studymindredacao.domain.model.enums.ForcaEvidencia;

import java.util.List;
import java.util.UUID;

/** A lista já vem na ordem do ranking; o índice numérico da pesquisa fica de fora de propósito. */
public record TemaPossivelResponse(
        UUID id,
        String titulo,
        Short ano,
        ForcaEvidencia forca,
        String eixo,
        String grupoSocial,
        String justificativa,
        List<String> argumentos,
        List<String> marcosLegais,
        List<String> agentesIntervencao,
        boolean adicionado
) {
    public static TemaPossivelResponse de(TemaPossivel item) {
        var tema = item.tema();
        var previsao = item.previsao();
        return new TemaPossivelResponse(
                tema.id(),
                tema.titulo(),
                tema.ano(),
                previsao.forca(),
                previsao.eixo(),
                previsao.grupoSocial(),
                previsao.justificativa(),
                previsao.argumentos(),
                previsao.marcosLegais(),
                previsao.agentesIntervencao(),
                item.adicionado()
        );
    }
}
