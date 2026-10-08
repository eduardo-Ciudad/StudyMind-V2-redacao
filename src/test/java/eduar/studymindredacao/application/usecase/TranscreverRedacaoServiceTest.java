package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.ImagemInvalidaException;
import eduar.studymindredacao.domain.exception.ImagemNaoReconhecidaException;
import eduar.studymindredacao.domain.exception.LimiteDiarioAtingidoException;
import eduar.studymindredacao.domain.exception.LimiteGlobalAtingidoException;
import eduar.studymindredacao.domain.exception.TranscricaoIAException;
import eduar.studymindredacao.domain.model.ImagemRedacao;
import eduar.studymindredacao.domain.model.ImagensDeTeste;
import eduar.studymindredacao.domain.model.enums.RecursoIA;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TranscreverRedacaoServiceTest {
    private static final int LIMITE = 3;
    private static final int LIMITE_GLOBAL = 30;
    // 01:30 UTC do dia 29 = 22:30 do dia 28 em São Paulo
    private static final Clock RELOGIO = Clock.fixed(Instant.parse("2026-09-29T01:30:00Z"), ZoneId.of("America/Sao_Paulo"));
    private static final LocalDate HOJE_EM_SP = LocalDate.of(2026, 9, 28);

    private final UUID alunoId = UUID.randomUUID();
    private final UsoIADiarioEmMemoria uso = new UsoIADiarioEmMemoria();
    private final TranscricaoIAFake ia = new TranscricaoIAFake();
    private final TranscreverRedacaoService service = new TranscreverRedacaoService(uso, ia, RELOGIO, LIMITE, LIMITE_GLOBAL);
    private final ImagemRedacao foto = ImagemRedacao.de(ImagensDeTeste.jpeg());

    @Test
    void transcreveEContaAVagaEOsTokensNoDiaDeSaoPaulo() {
        var resultado = service.transcrever(alunoId, List.of(foto));

        assertThat(resultado.linhas()).containsExactly("A educação no Brasil", "enfrenta desafios.");
        var doDia = uso.usoDoDia(alunoId, HOJE_EM_SP);
        assertThat(doDia.qtdTranscricoes()).isEqualTo(1);
        assertThat(doDia.tokensEntrada()).isEqualTo(TranscricaoIAFake.TOKENS_ENTRADA);
        assertThat(doDia.tokensSaida()).isEqualTo(TranscricaoIAFake.TOKENS_SAIDA);
    }

    @Test
    void naoGastaVagaDeCorrecao() {
        service.transcrever(alunoId, List.of(foto));

        assertThat(uso.usoDoDia(alunoId, HOJE_EM_SP).qtdCorrecoes()).isZero();
    }

    @Test
    void aceitaDuasFotosNaOrdemRecebida() {
        var segunda = ImagemRedacao.de(ImagensDeTeste.png());

        service.transcrever(alunoId, List.of(foto, segunda));

        assertThat(ia.chamadas()).containsExactly(List.of(foto, segunda));
    }

    @Test
    void rejeitaNenhumaOuMaisDeDuasFotosSemGastarVaga() {
        assertThatThrownBy(() -> service.transcrever(alunoId, List.of()))
                .isInstanceOfSatisfying(ImagemInvalidaException.class,
                        e -> assertThat(e.getMotivo()).isEqualTo(ImagemInvalidaException.Motivo.QUANTIDADE_INVALIDA));
        assertThatThrownBy(() -> service.transcrever(alunoId, List.of(foto, foto, foto)))
                .isInstanceOf(ImagemInvalidaException.class);

        assertThat(ia.chamadas()).isEmpty();
        assertThat(uso.usoDoDia(alunoId, HOJE_EM_SP).qtdTranscricoes()).isZero();
    }

    @Test
    void bloqueiaAQuartaTranscricaoDoDiaSemChamarAIA() {
        for (int i = 0; i < LIMITE; i++) {
            service.transcrever(alunoId, List.of(foto));
        }

        assertThatThrownBy(() -> service.transcrever(alunoId, List.of(foto)))
                .isInstanceOfSatisfying(LimiteDiarioAtingidoException.class, e -> {
                    assertThat(e.getRecurso()).isEqualTo(RecursoIA.TRANSCRICAO);
                    assertThat(e.getLimite()).isEqualTo(LIMITE);
                });
        assertThat(ia.chamadas()).hasSize(LIMITE);
    }

    @Test
    void tetoGlobalBloqueiaSemReservarVagaNemChamarAIA() {
        var comTetoDeDois = new TranscreverRedacaoService(uso, ia, RELOGIO, LIMITE, 2);
        comTetoDeDois.transcrever(alunoId, List.of(foto));
        comTetoDeDois.transcrever(UUID.randomUUID(), List.of(foto));

        UUID terceiro = UUID.randomUUID();
        assertThatThrownBy(() -> comTetoDeDois.transcrever(terceiro, List.of(foto)))
                .isInstanceOfSatisfying(LimiteGlobalAtingidoException.class,
                        e -> assertThat(e.getRecurso()).isEqualTo(RecursoIA.TRANSCRICAO));
        assertThat(ia.chamadas()).hasSize(2);
        assertThat(uso.usoDoDia(terceiro, HOJE_EM_SP).qtdTranscricoes()).isZero();
    }

    @Test
    void devolveAVagaQuandoAIAFalha() {
        ia.falharCom(new TranscricaoIAException("Gemini indisponível"));

        assertThatThrownBy(() -> service.transcrever(alunoId, List.of(foto)))
                .isInstanceOf(TranscricaoIAException.class);
        assertThat(uso.usoDoDia(alunoId, HOJE_EM_SP).qtdTranscricoes()).isZero();
    }

    @Test
    void devolveAVagaQuandoAFotoNaoERedacao() {
        ia.falharCom(new ImagemNaoReconhecidaException());

        assertThatThrownBy(() -> service.transcrever(alunoId, List.of(foto)))
                .isInstanceOf(ImagemNaoReconhecidaException.class);
        assertThat(uso.usoDoDia(alunoId, HOJE_EM_SP).qtdTranscricoes()).isZero();
    }
}
