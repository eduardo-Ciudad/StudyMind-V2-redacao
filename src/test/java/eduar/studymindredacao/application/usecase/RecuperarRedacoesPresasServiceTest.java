package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RecuperarRedacoesPresasServiceTest {
    private static final int LIMITE = 3;
    // 03:05 UTC do dia 29 = 00:05 do dia 29 em São Paulo (cinco minutos depois da virada)
    private static final Clock RELOGIO = Clock.fixed(Instant.parse("2026-09-29T03:05:00Z"), ZoneId.of("America/Sao_Paulo"));
    private static final OffsetDateTime AGORA = OffsetDateTime.now(RELOGIO);
    private static final LocalDate ONTEM_EM_SP = LocalDate.of(2026, 9, 28);
    private static final LocalDate HOJE_EM_SP = LocalDate.of(2026, 9, 29);

    private final UUID alunoId = UUID.randomUUID();
    private final RedacaoRepositoryEmMemoria redacoes = new RedacaoRepositoryEmMemoria();
    private final UsoIADiarioEmMemoria uso = new UsoIADiarioEmMemoria();
    private final RecuperarRedacoesPresasService service = new RecuperarRedacoesPresasService(redacoes, RELOGIO, uso);

    private Redacao redacao(StatusRedacao status, OffsetDateTime enviadaEm) {
        return redacoes.salvar(new Redacao(null, alunoId, UUID.randomUUID(), TipoRedacao.PRATICA, "texto", status, enviadaEm));
    }

    private StatusRedacao statusDe(Redacao redacao) {
        return redacoes.buscarPorId(redacao.id()).orElseThrow().status();
    }

    @Test
    void redacaoPresaHaMaisDeDezMinutos_viraErroEDevolveAVagaDoDiaDoEnvio() {
        uso.reservarCorrecao(alunoId, HOJE_EM_SP, LIMITE);
        uso.reservarCorrecao(alunoId, ONTEM_EM_SP, LIMITE);
        // enviada às 23:52 de ontem (13 min atrás): a vaga devolvida é a de ontem, não a de hoje
        var presa = redacao(StatusRedacao.EM_AVALIACAO, AGORA.minusMinutes(13));

        int recuperadas = service.recuperar();

        assertThat(recuperadas).isEqualTo(1);
        assertThat(statusDe(presa)).isEqualTo(StatusRedacao.ERRO);
        assertThat(uso.usoDoDia(alunoId, ONTEM_EM_SP).qtdCorrecoes()).isZero();
        assertThat(uso.usoDoDia(alunoId, HOJE_EM_SP).qtdCorrecoes()).isEqualTo(1);
    }

    @Test
    void redacaoEmAvaliacaoHaPoucoTempo_naoEhTocada() {
        uso.reservarCorrecao(alunoId, HOJE_EM_SP, LIMITE);
        var emAndamento = redacao(StatusRedacao.EM_AVALIACAO, AGORA.minusMinutes(4));

        int recuperadas = service.recuperar();

        assertThat(recuperadas).isZero();
        assertThat(statusDe(emAndamento)).isEqualTo(StatusRedacao.EM_AVALIACAO);
        assertThat(uso.usoDoDia(alunoId, HOJE_EM_SP).qtdCorrecoes()).isEqualTo(1);
    }

    @Test
    void redacaoAvaliadaAntiga_naoEhTocada() {
        var avaliada = redacao(StatusRedacao.AVALIADA, AGORA.minusHours(2));

        int recuperadas = service.recuperar();

        assertThat(recuperadas).isZero();
        assertThat(statusDe(avaliada)).isEqualTo(StatusRedacao.AVALIADA);
    }

    @Test
    void variasPresas_saoTodasRecuperadas() {
        uso.reservarCorrecao(alunoId, ONTEM_EM_SP, LIMITE);
        uso.reservarCorrecao(alunoId, ONTEM_EM_SP, LIMITE);
        var primeira = redacao(StatusRedacao.EM_AVALIACAO, AGORA.minusMinutes(40));
        var segunda = redacao(StatusRedacao.EM_AVALIACAO, AGORA.minusMinutes(25));

        int recuperadas = service.recuperar();

        assertThat(recuperadas).isEqualTo(2);
        assertThat(statusDe(primeira)).isEqualTo(StatusRedacao.ERRO);
        assertThat(statusDe(segunda)).isEqualTo(StatusRedacao.ERRO);
        assertThat(uso.usoDoDia(alunoId, ONTEM_EM_SP).qtdCorrecoes()).isZero();
    }
}
