package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.PrevisaoTemaEntity;
import eduar.studymindredacao.domain.model.PrevisaoTema;

/** Só leitura: os temas possíveis entram pelo seed da migration V5. */
public final class PrevisaoTemaPersistenceMapper {
    private PrevisaoTemaPersistenceMapper() {
    }

    public static PrevisaoTema toDomain(PrevisaoTemaEntity entity) {
        return new PrevisaoTema(
                entity.getTemaId(),
                entity.getRanking(),
                entity.getForca(),
                entity.getEixo(),
                entity.getGrupoSocial(),
                entity.getJustificativa(),
                AvaliacaoPersistenceMapper.lerLista(entity.getArgumentos()),
                AvaliacaoPersistenceMapper.lerLista(entity.getMarcosLegais()),
                AvaliacaoPersistenceMapper.lerLista(entity.getAgentesIntervencao())
        );
    }
}
