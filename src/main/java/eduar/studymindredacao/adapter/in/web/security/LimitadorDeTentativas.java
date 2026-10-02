package eduar.studymindredacao.adapter.in.web.security;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limit em memória por janela fixa: no máximo {@code limite} tentativas por {@code janela} para cada chave
 * (ex.: "login-ip:203.0.113.7"). Serve para uma instância só; com mais de uma, o contador precisa ir para
 * um armazenamento compartilhado.
 */
@Component
public class LimitadorDeTentativas {
    static final int MAXIMO_DE_CHAVES = 10_000;

    private record Janela(Instant inicio, int tentativas) {
    }

    private final Map<String, Janela> janelas = new ConcurrentHashMap<>();
    private final Clock clock;

    public LimitadorDeTentativas(Clock clock) {
        this.clock = clock;
    }

    /** Conta uma tentativa. Lança {@link TentativasExcedidasException} se a chave já usou o limite da janela. */
    public void consumir(String chave, int limite, Duration janela) {
        Instant agora = clock.instant();
        if (janelas.size() >= MAXIMO_DE_CHAVES) {
            descartarExpiradas(agora, janela);
        }
        Janela atual = janelas.compute(chave, (k, j) -> {
            if (j == null || !agora.isBefore(j.inicio().plus(janela))) {
                return new Janela(agora, 1);
            }
            return new Janela(j.inicio(), j.tentativas() + 1);
        });
        if (atual.tentativas() > limite) {
            long segundos = Math.max(1, Duration.between(agora, atual.inicio().plus(janela)).toSeconds());
            throw new TentativasExcedidasException(segundos);
        }
    }

    private void descartarExpiradas(Instant agora, Duration janela) {
        janelas.entrySet().removeIf(e -> !agora.isBefore(e.getValue().inicio().plus(janela)));
        if (janelas.size() >= MAXIMO_DE_CHAVES) {
            // Ainda cheio (ataque com muitas chaves): recomeça do zero em vez de crescer sem limite
            janelas.clear();
        }
    }
}
