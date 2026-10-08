package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.application.usecase.RedacaoDetalhada;
import eduar.studymindredacao.domain.model.enums.OrigemRedacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RedacaoResponse(
        UUID id,
        TemaResponse tema,
        TipoRedacao tipo,
        OrigemRedacao origem,
        StatusRedacao status,
        String texto,
        OffsetDateTime enviadaEm,
        AvaliacaoResponse avaliacao
) {
    public static RedacaoResponse de(RedacaoDetalhada detalhada) {
        var redacao = detalhada.redacao();
        return new RedacaoResponse(
                redacao.id(),
                TemaResponse.de(detalhada.tema()),
                redacao.tipo(),
                redacao.origem(),
                redacao.status(),
                redacao.texto(),
                redacao.enviadaEm(),
                AvaliacaoResponse.de(detalhada.avaliacao())
        );
    }
}
