package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.UsuarioEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.UsoIADiarioEntity;
import eduar.studymindredacao.domain.model.UsoIADiario;

public final class UsoIADiarioPersistenceMapper {
    private UsoIADiarioPersistenceMapper() {
    }

    public static UsoIADiario toDomain(UsoIADiarioEntity entity) {
        return new UsoIADiario(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getData(),
                entity.getTokensEntrada(),
                entity.getTokensSaida(),
                entity.getQtdCorrecoes(),
                entity.getQtdRoadmaps()
        );
    }

    public static UsoIADiarioEntity toEntity(UsoIADiario domain, UsuarioEntity usuario) {
        var entity = new UsoIADiarioEntity();
        entity.setId(domain.id());
        entity.setUsuario(usuario);
        entity.setData(domain.data());
        entity.setTokensEntrada(domain.tokensEntrada());
        entity.setTokensSaida(domain.tokensSaida());
        entity.setQtdCorrecoes(domain.qtdCorrecoes());
        entity.setQtdRoadmaps(domain.qtdRoadmaps());
        return entity;
    }
}
