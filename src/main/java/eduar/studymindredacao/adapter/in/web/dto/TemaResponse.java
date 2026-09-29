package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.OrigemTema;

import java.util.UUID;

public record TemaResponse(UUID id, String titulo, String textosMotivadores, OrigemTema origem, Short ano) {
    public static TemaResponse de(Tema tema) {
        if (tema == null) {
            return null;
        }
        return new TemaResponse(tema.id(), tema.titulo(), tema.textosMotivadores(), tema.origem(), tema.ano());
    }
}
