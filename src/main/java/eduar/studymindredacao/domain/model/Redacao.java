package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.OrigemRedacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;

import java.time.OffsetDateTime;
import java.util.UUID;


public record Redacao(
        UUID id,
        UUID usuarioId,
        UUID temaId,
        TipoRedacao tipo,
        String texto,
        StatusRedacao status,
        OffsetDateTime enviadaEm,
        OrigemRedacao origem
) {
    public Redacao {
        Validacoes.requererNaoNulo(usuarioId, "usuarioId");
        Validacoes.requererNaoNulo(temaId, "temaId");
        Validacoes.requererNaoNulo(tipo, "tipo");
        Validacoes.validarTextoObrigatorio(texto, "texto", null);
        if (status == null) {
            status = StatusRedacao.ENVIADA;
        }
        // sem origem informada, o texto veio do editor (todas as redações antigas, inclusive)
        if (origem == null) {
            origem = OrigemRedacao.DIGITADO;
        }
    }

    public Redacao comStatus(StatusRedacao novoStatus) {
        return new Redacao(id, usuarioId, temaId, tipo, texto, novoStatus, enviadaEm, origem);
    }
}
