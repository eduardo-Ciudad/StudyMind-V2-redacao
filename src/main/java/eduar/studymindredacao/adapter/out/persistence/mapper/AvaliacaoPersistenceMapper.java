package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.AvaliacaoEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.RedacaoEntity;
import eduar.studymindredacao.domain.model.Avaliacao;

public final class AvaliacaoPersistenceMapper {
    private AvaliacaoPersistenceMapper() {
    }

    public static Avaliacao toDomain(AvaliacaoEntity entity) {
        return new Avaliacao(
                entity.getId(),
                entity.getRedacao().getId(),
                entity.getNotaC1(),
                entity.getNotaC2(),
                entity.getNotaC3(),
                entity.getNotaC4(),
                entity.getNotaC5(),
                entity.getNotaTotal(),
                entity.getPontosFortes(),
                entity.getPontosDesenvolvimento(),
                entity.getDiagnostico(),
                entity.getModeloIa(),
                entity.getTokensEntrada(),
                entity.getTokensSaida(),
                entity.getRespostaBrutaJson(),
                entity.getAvaliadoEm()
        );
    }

    public static AvaliacaoEntity toEntity(Avaliacao domain, RedacaoEntity redacao) {
        var entity = new AvaliacaoEntity();
        entity.setId(domain.id());
        entity.setRedacao(redacao);
        entity.setNotaC1(domain.notaC1());
        entity.setNotaC2(domain.notaC2());
        entity.setNotaC3(domain.notaC3());
        entity.setNotaC4(domain.notaC4());
        entity.setNotaC5(domain.notaC5());
        entity.setNotaTotal(domain.notaTotal());
        entity.setPontosFortes(domain.pontosFortes());
        entity.setPontosDesenvolvimento(domain.pontosDesenvolvimento());
        entity.setDiagnostico(domain.diagnostico());
        entity.setModeloIa(domain.modeloIa());
        entity.setTokensEntrada(domain.tokensEntrada());
        entity.setTokensSaida(domain.tokensSaida());
        entity.setRespostaBrutaJson(domain.respostaBrutaJson());
        entity.setAvaliadoEm(domain.avaliadoEm());
        return entity;
    }
}
