package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.ForcaEvidencia;

import java.util.List;
import java.util.UUID;

/** Material de estudo de um tema possível (origem PREVISAO): por que ele merece atenção e com o que argumentar. */
public record PrevisaoTema(
        UUID temaId,
        int ranking,
        ForcaEvidencia forca,
        String eixo,
        String grupoSocial,
        String justificativa,
        List<String> argumentos,
        List<String> marcosLegais,
        List<String> agentesIntervencao
) {
    public PrevisaoTema {
        Validacoes.requererNaoNulo(temaId, "temaId");
        Validacoes.requererNaoNulo(forca, "forca");
        Validacoes.validarTextoObrigatorio(eixo, "eixo", 120);
        Validacoes.validarTextoObrigatorio(grupoSocial, "grupoSocial", 255);
        Validacoes.validarTextoObrigatorio(justificativa, "justificativa", null);
        if (ranking <= 0) {
            throw new IllegalArgumentException("ranking deve ser positivo");
        }
        argumentos = argumentos == null ? List.of() : List.copyOf(argumentos);
        marcosLegais = marcosLegais == null ? List.of() : List.copyOf(marcosLegais);
        agentesIntervencao = agentesIntervencao == null ? List.of() : List.copyOf(agentesIntervencao);
    }
}
