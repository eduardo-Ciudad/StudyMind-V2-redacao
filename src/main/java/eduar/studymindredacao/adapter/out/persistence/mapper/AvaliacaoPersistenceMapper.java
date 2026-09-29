package eduar.studymindredacao.adapter.out.persistence.mapper;


import eduar.studymindredacao.adapter.out.persistence.entity.AvaliacaoCompetenciaEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.AvaliacaoEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.RedacaoEntity;
import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.CompetenciaAvaliada;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

public final class AvaliacaoPersistenceMapper {
    private static final JsonMapper JSON = JsonMapper.builder().build();
    private static final TypeReference<List<String>> LISTA_DE_TEXTO = new TypeReference<>() {
    };

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
                Boolean.TRUE.equals(entity.getAnulada()),
                entity.getMotivoAnulacao(),
                entity.getCompetencias().stream().map(AvaliacaoPersistenceMapper::toDomain).toList(),
                lerLista(entity.getPontosFortes()),
                lerLista(entity.getPontosDesenvolvimento()),
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
        entity.setAnulada(domain.anulada());
        entity.setMotivoAnulacao(domain.motivoAnulacao());
        entity.setPontosFortes(escreverLista(domain.pontosFortes()));
        entity.setPontosDesenvolvimento(escreverLista(domain.pontosDesenvolvimento()));
        entity.setDiagnostico(domain.diagnostico());
        entity.setModeloIa(domain.modeloIa());
        entity.setTokensEntrada(domain.tokensEntrada());
        entity.setTokensSaida(domain.tokensSaida());
        entity.setRespostaBrutaJson(domain.respostaBrutaJson());
        entity.setAvaliadoEm(domain.avaliadoEm());
        domain.competencias().forEach(c -> entity.adicionarCompetencia(toEntity(c)));
        return entity;
    }

    private static CompetenciaAvaliada toDomain(AvaliacaoCompetenciaEntity entity) {
        return new CompetenciaAvaliada(
                entity.getNumero(),
                entity.getNota(),
                entity.getNivelReferencia(),
                entity.getResumo(),
                lerLista(entity.getProblemas())
        );
    }

    private static AvaliacaoCompetenciaEntity toEntity(CompetenciaAvaliada domain) {
        var entity = new AvaliacaoCompetenciaEntity();
        entity.setNumero((short) domain.numero());
        entity.setNota((short) domain.nota());
        entity.setNivelReferencia(domain.nivelReferencia());
        entity.setResumo(domain.resumo());
        entity.setProblemas(escreverLista(domain.problemasIdentificados()));
        return entity;
    }

    static List<String> lerLista(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        return JSON.readValue(json, LISTA_DE_TEXTO);
    }

    static String escreverLista(List<String> itens) {
        return JSON.writeValueAsString(itens == null ? List.of() : itens);
    }
}