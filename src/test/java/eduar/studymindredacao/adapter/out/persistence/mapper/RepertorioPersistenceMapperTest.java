package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.MacroeixoEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.ProblemaSocialEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.RepertorioEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.RepertorioFonteEntity;
import eduar.studymindredacao.domain.model.enums.Dificuldade;
import eduar.studymindredacao.domain.model.enums.FuncaoArgumentativa;
import eduar.studymindredacao.domain.model.enums.Nivel;
import eduar.studymindredacao.domain.model.enums.PapelRepertorio;
import eduar.studymindredacao.domain.model.enums.StatusVerificacao;
import eduar.studymindredacao.domain.model.enums.TipoEntidade;
import eduar.studymindredacao.domain.model.enums.TipoEvidencia;
import eduar.studymindredacao.domain.model.enums.TipoFonte;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RepertorioPersistenceMapperTest {

    private static ProblemaSocialEntity problema(String slug, String nome) {
        var macroeixo = new MacroeixoEntity();
        macroeixo.setId(UUID.randomUUID());
        macroeixo.setSlug("saude");
        macroeixo.setNome("Saúde");
        macroeixo.setOrdem((short) 1);

        var entity = new ProblemaSocialEntity();
        entity.setId(UUID.randomUUID());
        entity.setSlug(slug);
        entity.setNome(nome);
        entity.setMacroeixo(macroeixo);
        return entity;
    }

    private static RepertorioEntity base(String codigo, TipoEntidade tipo) {
        var entity = new RepertorioEntity();
        entity.setId(UUID.randomUUID());
        entity.setCodigo(codigo);
        entity.setTipoEntidade(tipo);
        entity.setPapel(tipo == TipoEntidade.EVIDENCE ? PapelRepertorio.EVIDENCE : PapelRepertorio.REPERTOIRE);
        entity.setNome("Nome");
        entity.setIdeiaCentral("Ideia central");
        entity.setTiposEvidencia("[]");
        entity.setFuncoesArgumentativas("[\"EXPLAIN_CAUSE\", \"SHOW_INEQUALITY\"]");
        entity.setTiposArgumento("[\"desigualdade estrutural\"]");
        entity.setTags("[\"saúde\", \"racismo\"]");
        entity.setStatusVerificacao(StatusVerificacao.VERIFIED);
        entity.setVerificadoEm(LocalDate.of(2026, 10, 7));
        entity.setAtivo(true);

        var fonte = new RepertorioFonteEntity();
        fonte.setId(UUID.randomUUID());
        fonte.setRepertorio(entity);
        fonte.setTipo(TipoFonte.GENERAL);
        fonte.setDescricao("Fonte");
        fonte.setUrl("https://exemplo.org");
        entity.getFontes().add(fonte);
        entity.getProblemas().add(problema("saude-mental", "Saúde mental"));
        return entity;
    }

    @Test
    void convertePessoaComNotasEListas() {
        var entity = base("REP-025", TipoEntidade.PERSON);
        entity.setExemploAplicacao("Exemplo");
        entity.setRiscoUso(Nivel.MEDIUM);
        entity.setDificuldade(Dificuldade.INTERMEDIATE);
        entity.setSaturacao(Nivel.LOW);
        entity.setNotaVersatilidade((short) 4);
        entity.setNotaAutoridade((short) 5);
        entity.setNotaCompreensao((short) 4);
        entity.setNotaAplicabilidade((short) 5);
        entity.setNotaEspecificidade((short) 5);
        entity.setNotaOriginalidade((short) 3);

        var repertorio = RepertorioPersistenceMapper.toDomain(entity);

        assertThat(repertorio.codigo()).isEqualTo("REP-025");
        assertThat(repertorio.funcoesArgumentativas())
                .containsExactly(FuncaoArgumentativa.EXPLAIN_CAUSE, FuncaoArgumentativa.SHOW_INEQUALITY);
        assertThat(repertorio.tiposArgumento()).containsExactly("desigualdade estrutural");
        assertThat(repertorio.tags()).containsExactly("saúde", "racismo");
        assertThat(repertorio.curadoria().saturacao()).isEqualTo(Nivel.LOW);
        assertThat(repertorio.curadoria().pontuacao().aplicabilidade()).isEqualTo(5);
        assertThat(repertorio.curadoria().pontuacao().originalidade()).isEqualTo(3);
        assertThat(repertorio.problemas()).singleElement()
                .satisfies(p -> {
                    assertThat(p.slug()).isEqualTo("saude-mental");
                    assertThat(p.macroeixoSlug()).isEqualTo("saude");
                });
        assertThat(repertorio.fontes()).singleElement()
                .satisfies(f -> assertThat(f.url()).isEqualTo("https://exemplo.org"));
        assertThat(repertorio.ativo()).isTrue();
    }

    @Test
    void semNotasAPontuacaoFicaNula() {
        var entity = base("REP-048", TipoEntidade.PERSON);
        entity.setExemploAplicacao("Exemplo");

        var repertorio = RepertorioPersistenceMapper.toDomain(entity);

        assertThat(repertorio.curadoria().temPontuacao()).isFalse();
    }

    @Test
    void converteEvidenciaComTiposEAno() {
        var entity = base("EVD-001", TipoEntidade.EVIDENCE);
        entity.setTiposEvidencia("[\"OFFICIAL_STATISTICS\", \"NATIONAL_SURVEY\"]");
        entity.setPopulacao("Pessoas de 15 anos ou mais");
        entity.setAnoEvidencia((short) 2025);

        var repertorio = RepertorioPersistenceMapper.toDomain(entity);

        assertThat(repertorio.ehEvidencia()).isTrue();
        assertThat(repertorio.evidencia().tipos())
                .containsExactly(TipoEvidencia.OFFICIAL_STATISTICS, TipoEvidencia.NATIONAL_SURVEY);
        assertThat(repertorio.evidencia().ano()).isEqualTo(2025);
        assertThat(repertorio.evidencia().populacao()).isEqualTo("Pessoas de 15 anos ou mais");
    }

    @Test
    void fonteSemUrlContinuaSemUrl() {
        var entity = base("EVD-006", TipoEntidade.EVIDENCE);
        entity.setTiposEvidencia("[\"NATIONAL_SURVEY\"]");
        entity.getFontes().iterator().next().setUrl(null);

        var repertorio = RepertorioPersistenceMapper.toDomain(entity);

        assertThat(repertorio.fontes()).singleElement().satisfies(f -> assertThat(f.url()).isNull());
    }
}
