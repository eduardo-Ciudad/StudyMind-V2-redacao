package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.FuncaoArgumentativa;
import eduar.studymindredacao.domain.model.enums.Nivel;
import eduar.studymindredacao.domain.model.enums.PapelRepertorio;
import eduar.studymindredacao.domain.model.enums.StatusVerificacao;
import eduar.studymindredacao.domain.model.enums.TipoEntidade;
import eduar.studymindredacao.domain.model.enums.TipoEvidencia;
import eduar.studymindredacao.domain.model.enums.TipoFonte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;

class RepertorioTest {

    private static final ProblemaSocial SAUDE_MENTAL =
            new ProblemaSocial(null, "saude-mental", "Saúde mental", "saude");
    private static final Fonte FONTE = new Fonte(TipoFonte.GENERAL, "Fonte", "https://exemplo.org");
    private static final ConteudoDidatico CONTEUDO_COMPLETO = new ConteudoDidatico(
            "Ideia central", "Lembre na prova", "Como usar", "Exemplo de aplicação", "Erro comum");

    private static Repertorio repertorio(
            String codigo,
            TipoEntidade tipo,
            ConteudoDidatico conteudo,
            DadosEvidencia evidencia,
            List<ProblemaSocial> problemas,
            List<Fonte> fontes,
            StatusVerificacao status,
            boolean ativo
    ) {
        return new Repertorio(
                null, codigo, tipo, PapelRepertorio.REPERTOIRE, "Nome", null, "PESQUISADORA", "Área", "Brasil",
                conteudo, new Curadoria(Nivel.MEDIUM, null, Nivel.LOW, null), evidencia,
                List.of(FuncaoArgumentativa.EXPLAIN_CAUSE), List.of("desigualdade estrutural"), List.of("saúde"),
                problemas, fontes, status, LocalDate.of(2026, 10, 7), ativo
        );
    }

    private static Repertorio pessoaAtiva(ConteudoDidatico conteudo) {
        return repertorio("REP-025", TipoEntidade.PERSON, conteudo, null,
                List.of(SAUDE_MENTAL), List.of(FONTE), StatusVerificacao.VERIFIED, true);
    }

    @Test
    void criaRepertorioAtivoCompleto() {
        var repertorio = pessoaAtiva(CONTEUDO_COMPLETO);

        assertThat(repertorio.ativo()).isTrue();
        assertThat(repertorio.problemas()).containsExactly(SAUDE_MENTAL);
        assertThat(repertorio.ehEvidencia()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"REP-025", "EVD-001", "CF-012"})
    void aceitaOsPadroesDeCodigo(String codigo) {
        assertThatNoException().isThrownBy(() -> repertorio(codigo, TipoEntidade.LEGAL_SOURCE, null, null,
                List.of(), List.of(), StatusVerificacao.PARTIAL, false));
    }

    @ParameterizedTest
    @ValueSource(strings = {"REP-25", "rep-025", "ABC-001", "REP-0250", "REP025"})
    void recusaCodigoForaDoPadrao(String codigo) {
        assertThatThrownBy(() -> repertorio(codigo, TipoEntidade.PERSON, null, null,
                List.of(), List.of(), StatusVerificacao.PARTIAL, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("codigo");
    }

    @Test
    void inativoPodeEstarIncompleto() {
        var repertorio = repertorio("REP-001", TipoEntidade.PERSON, null, null,
                null, null, StatusVerificacao.PARTIAL, false);

        assertThat(repertorio.conteudo().temIdeiaCentral()).isFalse();
        assertThat(repertorio.curadoria()).isNotNull();
        assertThat(repertorio.evidencia().tipos()).isEmpty();
        assertThat(repertorio.problemas()).isEmpty();
        assertThat(repertorio.fontes()).isEmpty();
    }

    @Test
    void rejeitadoNuncaFicaAtivo() {
        assertThatThrownBy(() -> repertorio("REP-030", TipoEntidade.PERSON, CONTEUDO_COMPLETO, null,
                List.of(SAUDE_MENTAL), List.of(FONTE), StatusVerificacao.REJECTED, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("REJECTED");
    }

    @Test
    void rejeitadoInativoEhPermitido() {
        assertThatNoException().isThrownBy(() -> repertorio("REP-030", TipoEntidade.PERSON, null, null,
                List.of(), List.of(), StatusVerificacao.REJECTED, false));
    }

    @Test
    void ativoPrecisaDeIdeiaCentral() {
        var semIdeia = new ConteudoDidatico(null, null, null, "Exemplo", null);

        assertThatThrownBy(() -> pessoaAtiva(semIdeia))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ideia central");
    }

    @Test
    void ativoPrecisaDeProblemaSocial() {
        assertThatThrownBy(() -> repertorio("REP-025", TipoEntidade.PERSON, CONTEUDO_COMPLETO, null,
                List.of(), List.of(FONTE), StatusVerificacao.VERIFIED, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("problema social");
    }

    @Test
    void ativoPrecisaDeFonte() {
        assertThatThrownBy(() -> repertorio("REP-025", TipoEntidade.PERSON, CONTEUDO_COMPLETO, null,
                List.of(SAUDE_MENTAL), List.of(), StatusVerificacao.VERIFIED, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fonte");
    }

    @Test
    void evidenciaAtivaPrecisaDeTipoDeEvidencia() {
        var conteudo = new ConteudoDidatico("4,9% de analfabetismo em 2025", null, null, null, null);

        assertThatThrownBy(() -> repertorio("EVD-001", TipoEntidade.EVIDENCE, conteudo, DadosEvidencia.vazio(),
                List.of(SAUDE_MENTAL), List.of(FONTE), StatusVerificacao.VERIFIED, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tipo de evidência");
    }

    @Test
    void evidenciaAtivaSemExemploEhValida() {
        var conteudo = new ConteudoDidatico("4,9% de analfabetismo em 2025", null, null, null, null);
        var dados = new DadosEvidencia(List.of(TipoEvidencia.OFFICIAL_STATISTICS), null, 2025);

        var evidencia = repertorio("EVD-001", TipoEntidade.EVIDENCE, conteudo, dados,
                List.of(SAUDE_MENTAL), List.of(FONTE), StatusVerificacao.VERIFIED, true);

        assertThat(evidencia.ehEvidencia()).isTrue();
    }

    @Test
    void normaAtivaSemExemploEhValida() {
        var conteudo = new ConteudoDidatico("Dignidade da pessoa humana", null, null, null, "Não substitui lei específica");

        assertThatNoException().isThrownBy(() -> repertorio("CF-001", TipoEntidade.LEGAL_SOURCE, conteudo, null,
                List.of(SAUDE_MENTAL), List.of(FONTE), StatusVerificacao.VERIFIED, true));
    }

    @ParameterizedTest
    @EnumSource(value = TipoEntidade.class, names = {"PERSON", "CONCEPT", "CULTURAL_WORK"})
    void pessoaConceitoEObraAtivosPrecisamDeExemplo(TipoEntidade tipo) {
        var semExemplo = new ConteudoDidatico("Ideia central", null, null, null, null);

        assertThatThrownBy(() -> repertorio("REP-025", tipo, semExemplo, null,
                List.of(SAUDE_MENTAL), List.of(FONTE), StatusVerificacao.VERIFIED, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exemplo de aplicação");
    }

    @Test
    void copiaAsListasRecebidas() {
        var problemas = new ArrayList<>(List.of(SAUDE_MENTAL));
        var repertorio = repertorio("REP-025", TipoEntidade.PERSON, CONTEUDO_COMPLETO, null,
                problemas, List.of(FONTE), StatusVerificacao.VERIFIED, true);

        problemas.clear();

        assertThat(repertorio.problemas()).hasSize(1);
    }

    @Test
    void exigeNome() {
        assertThatThrownBy(() -> new Repertorio(
                null, "REP-025", TipoEntidade.PERSON, PapelRepertorio.REPERTOIRE, " ", null, null, null, null,
                null, null, null, null, null, null, null, null, StatusVerificacao.PARTIAL, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nome");
    }

    @Test
    void exigeTipoPapelEStatus() {
        assertThatThrownBy(() -> new Repertorio(
                null, "REP-025", null, PapelRepertorio.REPERTOIRE, "Nome", null, null, null, null,
                null, null, null, null, null, null, null, null, StatusVerificacao.PARTIAL, null, false))
                .hasMessageContaining("tipoEntidade");
        assertThatThrownBy(() -> new Repertorio(
                null, "REP-025", TipoEntidade.PERSON, null, "Nome", null, null, null, null,
                null, null, null, null, null, null, null, null, StatusVerificacao.PARTIAL, null, false))
                .hasMessageContaining("papel");
        assertThatThrownBy(() -> new Repertorio(
                null, "REP-025", TipoEntidade.PERSON, PapelRepertorio.REPERTOIRE, "Nome", null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, false))
                .hasMessageContaining("statusVerificacao");
    }
}
