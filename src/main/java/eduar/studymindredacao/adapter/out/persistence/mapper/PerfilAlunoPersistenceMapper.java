package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.PerfilAlunoEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.UsuarioEntity;
import eduar.studymindredacao.domain.model.PerfilAluno;

public final class PerfilAlunoPersistenceMapper {
    private PerfilAlunoPersistenceMapper() {
    }

    public static PerfilAluno toDomain(PerfilAlunoEntity entity) {
        return new PerfilAluno(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getMetaNota(),
                entity.getNivelExperiencia(),
                entity.getTempoDisponivelSemanalMin(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public static PerfilAlunoEntity toEntity(PerfilAluno domain, UsuarioEntity usuario) {
        var entity = new PerfilAlunoEntity();
        entity.setId(domain.id());
        entity.setUsuario(usuario);
        entity.setMetaNota(domain.metaNota());
        entity.setNivelExperiencia(domain.nivelExperiencia());
        entity.setTempoDisponivelSemanalMin(domain.tempoDisponivelSemanalMin());
        entity.setCriadoEm(domain.criadoEm());
        entity.setAtualizadoEm(domain.atualizadoEm());
        return entity;
    }
}
