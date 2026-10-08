package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.CompetenciaAvaliada;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.OrigemRedacao;
import eduar.studymindredacao.domain.model.enums.OrigemTema;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultarEvolucaoServiceTest {
    private final UUID alunoId = UUID.randomUUID();
    private final TemaRepositoryEmMemoria temas = new TemaRepositoryEmMemoria();
    private final RedacaoRepositoryEmMemoria redacoes = new RedacaoRepositoryEmMemoria();
    private final AvaliacaoRepositoryEmMemoria avaliacoes = new AvaliacaoRepositoryEmMemoria();
    private final ConsultarEvolucaoService service = new ConsultarEvolucaoService(redacoes, avaliacoes, temas);

    private Tema tema;

    @BeforeEach
    void setUp() {
        tema = temas.salvar(new Tema(null, "Democratização do acesso ao cinema no Brasil", null, OrigemTema.ENEM_OFICIAL, (short) 2019, true, null));
    }

    private Redacao redacao(StatusRedacao status, int diasAtras) {
        return redacoes.salvar(new Redacao(null, alunoId, tema.id(), TipoRedacao.PRATICA, "texto", status,
                OffsetDateTime.now().minusDays(diasAtras), OrigemRedacao.DIGITADO));
    }

    private void avaliar(Redacao redacao, int c1, int c2, int c3, int c4, int c5) {
        avaliar(redacao, new int[]{c1, c2, c3, c4, c5}, Map.of());
    }

    /** problemas: número da competência → quantidade de problemas apontados nela. */
    private void avaliar(Redacao redacao, int[] notas, Map<Integer, Integer> problemas) {
        List<CompetenciaAvaliada> competencias = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int numero = i + 1;
            List<String> lista = new ArrayList<>();
            for (int p = 0; p < problemas.getOrDefault(numero, 0); p++) {
                lista.add("problema " + p);
            }
            competencias.add(new CompetenciaAvaliada(numero, notas[i], "n", "r", lista));
        }
        avaliacoes.salvar(new Avaliacao(null, redacao.id(), (short) notas[0], (short) notas[1], (short) notas[2],
                (short) notas[3], (short) notas[4], null, false, null, competencias, null, null, "diagnóstico",
                null, null, null, null, null));
    }

    private void anular(Redacao redacao) {
        avaliacoes.salvar(new Avaliacao(null, redacao.id(), (short) 0, (short) 0, (short) 0, (short) 0, (short) 0,
                null, true, "Fuga ao tema", null, null, null, "diagnóstico", null, null, null, null, null));
    }

    @Test
    void alunoSemRedacoes_retornaEvolucaoVazia() {
        var evolucao = service.consultar(alunoId);

        assertThat(evolucao.totalCorrigidas()).isZero();
        assertThat(evolucao.melhorNota()).isNull();
        assertThat(evolucao.mediaRecente()).isNull();
        assertThat(evolucao.competenciaFoco()).isNull();
        assertThat(evolucao.serie()).isEmpty();
    }

    @Test
    void serieEmOrdemCronologica_ignorandoRedacoesNaoAvaliadas() {
        var antiga = redacao(StatusRedacao.AVALIADA, 5);
        var recente = redacao(StatusRedacao.AVALIADA, 1);
        redacao(StatusRedacao.ERRO, 3);
        redacao(StatusRedacao.EM_AVALIACAO, 0);
        avaliar(recente, 160, 160, 160, 160, 160);
        avaliar(antiga, 120, 120, 120, 120, 120);

        var evolucao = service.consultar(alunoId);

        assertThat(evolucao.serie()).extracting(PontoEvolucao::redacaoId).containsExactly(antiga.id(), recente.id());
        assertThat(evolucao.serie().get(0).temaTitulo()).isEqualTo(tema.titulo());
        assertThat(evolucao.totalCorrigidas()).isEqualTo(2);
        assertThat(evolucao.melhorNota()).isEqualTo(800);
    }

    @Test
    void comMenosDeTresRedacoes_mediaUsaAsQueExistem() {
        avaliar(redacao(StatusRedacao.AVALIADA, 2), 120, 160, 80, 160, 120);
        avaliar(redacao(StatusRedacao.AVALIADA, 1), 160, 160, 120, 160, 80);

        var media = service.consultar(alunoId).mediaRecente();

        assertThat(media).isEqualTo(new NotasPorCompetencia(140, 160, 100, 160, 100));
        assertThat(media.total()).isEqualTo(660);
    }

    @Test
    void mediaUsaSomenteAsTresMaisRecentes_eArredonda() {
        avaliar(redacao(StatusRedacao.AVALIADA, 10), 0, 0, 0, 0, 0);
        avaliar(redacao(StatusRedacao.AVALIADA, 3), 160, 120, 80, 160, 120);
        avaliar(redacao(StatusRedacao.AVALIADA, 2), 160, 160, 120, 120, 120);
        avaliar(redacao(StatusRedacao.AVALIADA, 1), 160, 120, 80, 160, 120);

        var evolucao = service.consultar(alunoId);

        // C2: (120 + 160 + 120) / 3 = 133,3 → 133; C3: (80 + 120 + 80) / 3 = 93,3 → 93
        assertThat(evolucao.mediaRecente()).isEqualTo(new NotasPorCompetencia(160, 133, 93, 147, 120));
        assertThat(evolucao.competenciaFoco()).isEqualTo(3);
        assertThat(evolucao.totalCorrigidas()).isEqualTo(4);
    }

    @Test
    void redacaoAnulada_apareceNaSerieMasNaoEntraNaMediaNemNaMelhorNota() {
        var anulada = redacao(StatusRedacao.AVALIADA, 2);
        anular(anulada);
        avaliar(redacao(StatusRedacao.AVALIADA, 1), 120, 120, 120, 120, 120);

        var evolucao = service.consultar(alunoId);

        assertThat(evolucao.serie()).hasSize(2);
        assertThat(evolucao.serie().get(0).anulada()).isTrue();
        assertThat(evolucao.serie().get(0).notas()).isNull();
        assertThat(evolucao.totalCorrigidas()).isEqualTo(1);
        assertThat(evolucao.melhorNota()).isEqualTo(600);
        assertThat(evolucao.mediaRecente()).isEqualTo(new NotasPorCompetencia(120, 120, 120, 120, 120));
    }

    @Test
    void somenteRedacoesAnuladas_semMediaESemFoco() {
        anular(redacao(StatusRedacao.AVALIADA, 1));

        var evolucao = service.consultar(alunoId);

        assertThat(evolucao.serie()).hasSize(1);
        assertThat(evolucao.mediaRecente()).isNull();
        assertThat(evolucao.competenciaFoco()).isNull();
    }

    @Test
    void empateNaMedia_focoVaiParaQuemTemMaisProblemas() {
        avaliar(redacao(StatusRedacao.AVALIADA, 1), new int[]{160, 120, 160, 160, 120}, Map.of(2, 1, 5, 3));

        assertThat(service.consultar(alunoId).competenciaFoco()).isEqualTo(5);
    }

    @Test
    void empateTotal_focoVaiParaAMenorCompetencia() {
        avaliar(redacao(StatusRedacao.AVALIADA, 1), 160, 120, 160, 120, 160);

        assertThat(service.consultar(alunoId).competenciaFoco()).isEqualTo(2);
    }

    @Test
    void todasNoMaximo_semFoco() {
        avaliar(redacao(StatusRedacao.AVALIADA, 1), 200, 200, 200, 200, 200);

        assertThat(service.consultar(alunoId).competenciaFoco()).isNull();
    }
}
