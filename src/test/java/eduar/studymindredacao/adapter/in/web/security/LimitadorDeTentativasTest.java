package eduar.studymindredacao.adapter.in.web.security;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LimitadorDeTentativasTest {
    private static final Duration MINUTO = Duration.ofMinutes(1);

    private final RelogioAjustavel relogio = new RelogioAjustavel(Instant.parse("2026-10-02T20:00:00Z"));
    private final LimitadorDeTentativas limitador = new LimitadorDeTentativas(relogio);

    @Test
    void bloqueiaDepoisDoLimiteComRetryAfterAteOFimDaJanela() {
        for (int i = 0; i < 5; i++) {
            limitador.consumir("login-email:a@b.com", 5, MINUTO);
        }
        relogio.avancar(Duration.ofSeconds(20));

        assertThatThrownBy(() -> limitador.consumir("login-email:a@b.com", 5, MINUTO))
                .isInstanceOfSatisfying(TentativasExcedidasException.class,
                        e -> assertThat(e.getSegundosParaTentarDeNovo()).isEqualTo(40));
    }

    @Test
    void liberaQuandoAJanelaTermina() {
        for (int i = 0; i < 5; i++) {
            limitador.consumir("k", 5, MINUTO);
        }
        relogio.avancar(MINUTO);

        assertThatCode(() -> limitador.consumir("k", 5, MINUTO)).doesNotThrowAnyException();
    }

    @Test
    void chavesDiferentesTemContadoresSeparados() {
        for (int i = 0; i < 5; i++) {
            limitador.consumir("login-ip:1.1.1.1", 5, MINUTO);
        }

        assertThatCode(() -> limitador.consumir("login-ip:2.2.2.2", 5, MINUTO)).doesNotThrowAnyException();
    }

    private static final class RelogioAjustavel extends Clock {
        private Instant agora;

        RelogioAjustavel(Instant agora) {
            this.agora = agora;
        }

        void avancar(Duration duracao) {
            agora = agora.plus(duracao);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return agora;
        }
    }
}
