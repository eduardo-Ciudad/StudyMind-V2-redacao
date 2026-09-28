package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.RedacaoEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.TemaEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.UsuarioEntity;
import eduar.studymindredacao.domain.model.Redacao;

public final class RedacaoPersistenceMapper {
    private RedacaoPersistenceMapper() {
    }

    public static Redacao toDomain(RedacaoEntity entity) {
        return new Redacao(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getTema().getId(),
                entity.getTipo(),
                entity.getTexto(),
                entity.getStatus(),
                entity.getEnviadaEm()
        );
    }

    public static RedacaoEntity toEntity(Redacao domain, UsuarioEntity usuario, TemaEntity tema) {
        var entity = new RedacaoEntity();
        entity.setId(domain.id());
        entity.setUsuario(usuario);
        entity.setTema(tema);
        entity.setTipo(domain.tipo());
        entity.setTexto(domain.texto());
        entity.setStatus(domain.status());
        entity.setEnviadaEm(domain.enviadaEm());
        return entity;
    }
}
