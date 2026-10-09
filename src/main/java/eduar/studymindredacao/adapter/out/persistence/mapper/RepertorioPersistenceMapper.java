package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.RepertorioEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.RepertorioFonteEntity;
import eduar.studymindredacao.domain.model.ConteudoDidatico;
import eduar.studymindredacao.domain.model.Curadoria;
import eduar.studymindredacao.domain.model.DadosEvidencia;
import eduar.studymindredacao.domain.model.Fonte;
import eduar.studymindredacao.domain.model.Pontuacao;
import eduar.studymindredacao.domain.model.Repertorio;
import eduar.studymindredacao.domain.model.enums.FuncaoArgumentativa;
import eduar.studymindredacao.domain.model.enums.TipoEvidencia;

import java.util.List;
import java.util.function.Function;

/**
 * Só leitura: os repertórios entram pelas migrations de seed (V9–V14).
 * A escrita pelo Java está registrada como issue futura (admin).
 */
public final class RepertorioPersistenceMapper {
    private RepertorioPersistenceMapper() {
    }

    public static Repertorio toDomain(RepertorioEntity entity) {
        return new Repertorio(
                entity.getId(),
                entity.getCodigo(),
                entity.getTipoEntidade(),
                entity.getPapel(),
                entity.getNome(),
                entity.getSubtitulo(),
                entity.getTipoDescricao(),
                entity.getArea(),
                entity.getPais(),
                new ConteudoDidatico(
                        entity.getIdeiaCentral(),
                        entity.getLembreNaProva(),
                        entity.getComoUsar(),
                        entity.getExemploAplicacao(),
                        entity.getErroComum()
                ),
                new Curadoria(entity.getRiscoUso(), entity.getDificuldade(), entity.getSaturacao(), pontuacao(entity)),
                new DadosEvidencia(
                        lerEnums(entity.getTiposEvidencia(), TipoEvidencia::valueOf),
                        entity.getPopulacao(),
                        entity.getAnoEvidencia() == null ? null : entity.getAnoEvidencia().intValue()
                ),
                lerEnums(entity.getFuncoesArgumentativas(), FuncaoArgumentativa::valueOf),
                AvaliacaoPersistenceMapper.lerLista(entity.getTiposArgumento()),
                AvaliacaoPersistenceMapper.lerLista(entity.getTags()),
                entity.getProblemas().stream().map(TaxonomiaPersistenceMapper::toDomain).toList(),
                entity.getFontes().stream().map(RepertorioPersistenceMapper::toDomain).toList(),
                entity.getStatusVerificacao(),
                entity.getVerificadoEm(),
                Boolean.TRUE.equals(entity.getAtivo())
        );
    }

    static Fonte toDomain(RepertorioFonteEntity entity) {
        return new Fonte(entity.getTipo(), entity.getDescricao(), entity.getUrl());
    }

    /** O CHECK da V7 garante: as 6 notas vêm todas preenchidas ou todas nulas. */
    private static Pontuacao pontuacao(RepertorioEntity entity) {
        if (entity.getNotaVersatilidade() == null) {
            return null;
        }
        return new Pontuacao(
                entity.getNotaVersatilidade(),
                entity.getNotaAutoridade(),
                entity.getNotaCompreensao(),
                entity.getNotaAplicabilidade(),
                entity.getNotaEspecificidade(),
                entity.getNotaOriginalidade()
        );
    }

    private static <E extends Enum<E>> List<E> lerEnums(String json, Function<String, E> conversor) {
        return AvaliacaoPersistenceMapper.lerLista(json).stream().map(conversor).toList();
    }
}
