package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.application.usecase.RedacaoResumo;
import eduar.studymindredacao.domain.model.enums.OrigemRedacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RedacaoResumoResponse(
        UUID id,
        UUID temaId,
        String temaTitulo,
        TipoRedacao tipo,
        OrigemRedacao origem,
        StatusRedacao status,
        OffsetDateTime enviadaEm,
        Short notaTotal
) {
    public static RedacaoResumoResponse de(RedacaoResumo resumo) {
        var redacao = resumo.redacao();
        return new RedacaoResumoResponse(
                redacao.id(),
                redacao.temaId(),
                resumo.temaTitulo(),
                redacao.tipo(),
                redacao.origem(),
                redacao.status(),
                redacao.enviadaEm(),
                resumo.notaTotal()
        );
    }
}
