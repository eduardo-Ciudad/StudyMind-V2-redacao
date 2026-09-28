package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.OrigemTema;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Tema(
        UUID id,
        String titulo,
        String textosMotivadores,
        OrigemTema origem,
        Short ano,
        Boolean ativo,
        OffsetDateTime criadoEm
) {
    public Tema {
        Validacoes.validarTextoObrigatorio(titulo, "titulo", 255);
        Validacoes.requererNaoNulo(origem, "origem");
        if (ativo == null) {
            ativo = Boolean.TRUE;
        }
    }
}
