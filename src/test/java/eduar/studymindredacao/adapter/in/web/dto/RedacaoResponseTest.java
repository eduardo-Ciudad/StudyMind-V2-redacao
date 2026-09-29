package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.application.usecase.RedacaoDetalhada;
import eduar.studymindredacao.application.usecase.RedacaoResumo;
import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.CompetenciaAvaliada;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.ResultadoAvaliacaoIA;
import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.OrigemTema;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RedacaoResponseTest {
    private final Tema tema = new Tema(UUID.randomUUID(), "Democratização do acesso ao cinema no Brasil", null,
            OrigemTema.ENEM_OFICIAL, (short) 2019, true, null);
    private final Redacao redacao = new Redacao(UUID.randomUUID(), UUID.randomUUID(), tema.id(),
            TipoRedacao.PRATICA, "texto", StatusRedacao.AVALIADA, OffsetDateTime.now());

    @Test
    void mapeiaRedacaoAvaliadaComCompetencias() {
        var competencias = List.of(
                new CompetenciaAvaliada(1, 160, "n1", "r1", List.of()),
                new CompetenciaAvaliada(2, 120, "n2", "r2", List.of("repertório de bolso")),
                new CompetenciaAvaliada(3, 120, "n3", "r3", List.of()),
                new CompetenciaAvaliada(4, 160, "n4", "r4", List.of()),
                new CompetenciaAvaliada(5, 80, "n5", "r5", List.of())
        );
        var avaliacao = Avaliacao.de(redacao.id(), new ResultadoAvaliacaoIA(false, null, competencias,
                List.of("tese"), List.of("proposta"), "diag", "gemini-x", 10, 20, "{}"));

        var resposta = RedacaoResponse.de(new RedacaoDetalhada(redacao, tema, avaliacao));

        assertThat(resposta.tema().titulo()).isEqualTo(tema.titulo());
        assertThat(resposta.avaliacao().notaTotal()).isEqualTo((short) 640);
        assertThat(resposta.avaliacao().competencias()).hasSize(5);
        assertThat(resposta.avaliacao().competencias().get(1).problemas()).containsExactly("repertório de bolso");
    }

    @Test
    void redacaoSemAvaliacaoETemaRemovidoViramNull() {
        var resposta = RedacaoResponse.de(new RedacaoDetalhada(redacao.comStatus(StatusRedacao.ERRO), null, null));

        assertThat(resposta.status()).isEqualTo(StatusRedacao.ERRO);
        assertThat(resposta.tema()).isNull();
        assertThat(resposta.avaliacao()).isNull();
    }

    @Test
    void resumoLevaTituloENota() {
        var resumo = RedacaoResumoResponse.de(new RedacaoResumo(redacao, tema.titulo(), (short) 720));

        assertThat(resumo.temaTitulo()).isEqualTo(tema.titulo());
        assertThat(resumo.notaTotal()).isEqualTo((short) 720);
        assertThat(resumo.temaId()).isEqualTo(tema.id());
    }
}
