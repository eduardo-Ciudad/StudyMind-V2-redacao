package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.MacroeixoEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.ProblemaSocialEntity;
import eduar.studymindredacao.domain.model.Macroeixo;
import eduar.studymindredacao.domain.model.ProblemaSocial;

/** Só leitura: macroeixos e problemas sociais entram pelas migrations de seed. */
public final class TaxonomiaPersistenceMapper {
    private TaxonomiaPersistenceMapper() {
    }

    public static Macroeixo toDomain(MacroeixoEntity entity) {
        return new Macroeixo(entity.getId(), entity.getSlug(), entity.getNome(), entity.getOrdem());
    }

    /** O macroeixo precisa estar carregado (join fetch) para não disparar uma consulta por problema. */
    public static ProblemaSocial toDomain(ProblemaSocialEntity entity) {
        return new ProblemaSocial(entity.getId(), entity.getSlug(), entity.getNome(), entity.getMacroeixo().getSlug());
    }
}
