package eduar.studymindredacao.application.usecase;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultarUsoDiarioServiceTest {
    private static final int LIMITE = 3;
    // 01:30 UTC do dia 29 = 22:30 do dia 28 em São Paulo
    private static final Clock RELOGIO = Clock.fixed(Instant.parse("2026-09-29T01:30:00Z"), ZoneId.of("America/Sao_Paulo"));
    private static final LocalDate HOJE_EM_SP = LocalDate.of(2026, 9, 28);

    private final UUID alunoId = UUID.randomUUID();
    private final UsoIADiarioEmMemoria uso = new UsoIADiarioEmMemoria();
    private final ConsultarUsoDiarioService service = new ConsultarUsoDiarioService(uso, RELOGIO, LIMITE);

    @Test
    void semUsoNoDia_retornaLimiteInteiro() {
        var resultado = service.consultar(alunoId);

        assertThat(resultado.usadas()).isZero();
        assertThat(resultado.limite()).isEqualTo(LIMITE);
        assertThat(resultado.restantes()).isEqualTo(LIMITE);
    }

    @Test
    void contaAsVagasReservadasNoDiaDeSaoPaulo() {
        uso.reservarCorrecao(alunoId, HOJE_EM_SP, LIMITE);
        uso.reservarCorrecao(alunoId, HOJE_EM_SP, LIMITE);
        // uso do dia seguinte em UTC não pode entrar na conta
        uso.reservarCorrecao(alunoId, HOJE_EM_SP.plusDays(1), LIMITE);

        var resultado = service.consultar(alunoId);

        assertThat(resultado.usadas()).isEqualTo(2);
        assertThat(resultado.restantes()).isEqualTo(1);
    }

    @Test
    void restantesNuncaFicaNegativo_seOLimiteDiminuir() {
        for (int i = 0; i < LIMITE; i++) {
            uso.reservarCorrecao(alunoId, HOJE_EM_SP, LIMITE);
        }
        var comLimiteMenor = new ConsultarUsoDiarioService(uso, RELOGIO, 1);

        var resultado = comLimiteMenor.consultar(alunoId);

        assertThat(resultado.usadas()).isEqualTo(LIMITE);
        assertThat(resultado.restantes()).isZero();
    }

    @Test
    void renovaNaMeiaNoiteDeSaoPaulo() {
        var resultado = service.consultar(alunoId);

        assertThat(resultado.renovaEm())
                .isEqualTo(OffsetDateTime.of(2026, 9, 29, 0, 0, 0, 0, ZoneOffset.ofHours(-3)));
    }
}
