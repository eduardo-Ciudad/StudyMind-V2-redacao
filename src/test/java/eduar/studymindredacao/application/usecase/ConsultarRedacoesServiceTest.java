package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.RedacaoNaoEncontradaException;
import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.OrigemTema;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConsultarRedacoesServiceTest {
    private final UUID alunoId = UUID.randomUUID();
    private final UUID outroAlunoId = UUID.randomUUID();
    private final TemaRepositoryEmMemoria temas = new TemaRepositoryEmMemoria();
    private final RedacaoRepositoryEmMemoria redacoes = new RedacaoRepositoryEmMemoria();
    private final AvaliacaoRepositoryEmMemoria avaliacoes = new AvaliacaoRepositoryEmMemoria();
    private final ConsultarRedacoesService service = new ConsultarRedacoesService(redacoes, avaliacoes, temas);

    private Tema cinema;
    private Tema surdos;

    @BeforeEach
    void setUp() {
        cinema = temas.salvar(new Tema(null, "Democratização do acesso ao cinema no Brasil", null, OrigemTema.ENEM_OFICIAL, (short) 2019, true, null));
        surdos = temas.salvar(new Tema(null, "Desafios para a formação educacional de surdos no Brasil", null, OrigemTema.ENEM_OFICIAL, (short) 2017, true, null));
    }

    private Redacao redacao(UUID usuarioId, Tema tema, StatusRedacao status, int diasAtras) {
        return redacoes.salvar(new Redacao(null, usuarioId, tema.id(), TipoRedacao.PRATICA, "texto", status,
                OffsetDateTime.now().minusDays(diasAtras)));
    }

    private void avaliar(Redacao redacao, int notaPorCompetencia) {
        short n = (short) notaPorCompetencia;
        avaliacoes.salvar(new Avaliacao(null, redacao.id(), n, n, n, n, n, null, false, null,
                null, null, null, "diagnóstico", null, null, null, null, null));
    }

    @Test
    void listaHistoricoDoAlunoComTituloENota() {
        var antiga = redacao(alunoId, surdos, StatusRedacao.AVALIADA, 2);
        var recente = redacao(alunoId, cinema, StatusRedacao.AVALIADA, 0);
        avaliar(antiga, 120);
        avaliar(recente, 160);

        var historico = service.listar(alunoId);

        assertThat(historico).extracting(r -> r.redacao().id()).containsExactly(recente.id(), antiga.id());
        assertThat(historico.get(0).temaTitulo()).isEqualTo(cinema.titulo());
        assertThat(historico.get(0).notaTotal()).isEqualTo((short) 800);
        assertThat(historico.get(1).notaTotal()).isEqualTo((short) 600);
    }

    @Test
    void redacaoComErroApareceSemNota() {
        redacao(alunoId, cinema, StatusRedacao.ERRO, 0);

        var historico = service.listar(alunoId);

        assertThat(historico).singleElement().satisfies(r -> {
            assertThat(r.redacao().status()).isEqualTo(StatusRedacao.ERRO);
            assertThat(r.notaTotal()).isNull();
        });
    }

    @Test
    void naoMisturaRedacoesDeOutroAluno() {
        redacao(outroAlunoId, cinema, StatusRedacao.AVALIADA, 0);

        assertThat(service.listar(alunoId)).isEmpty();
    }

    @Test
    void detalheTrazTemaEAvaliacao() {
        var redacao = redacao(alunoId, cinema, StatusRedacao.AVALIADA, 0);
        avaliar(redacao, 160);

        var detalhe = service.buscar(alunoId, redacao.id());

        assertThat(detalhe.tema().titulo()).isEqualTo(cinema.titulo());
        assertThat(detalhe.avaliacao().notaTotal()).isEqualTo((short) 800);
    }

    @Test
    void detalheDeRedacaoAindaNaoAvaliadaVemSemAvaliacao() {
        var redacao = redacao(alunoId, cinema, StatusRedacao.ERRO, 0);

        assertThat(service.buscar(alunoId, redacao.id()).avaliacao()).isNull();
    }

    @Test
    void redacaoDeOutroAlunoRespondeComoInexistente() {
        var alheia = redacao(outroAlunoId, cinema, StatusRedacao.AVALIADA, 0);

        assertThatThrownBy(() -> service.buscar(alunoId, alheia.id()))
                .isInstanceOf(RedacaoNaoEncontradaException.class);
    }

    @Test
    void redacaoInexistente() {
        assertThatThrownBy(() -> service.buscar(alunoId, UUID.randomUUID()))
                .isInstanceOf(RedacaoNaoEncontradaException.class);
    }
}
