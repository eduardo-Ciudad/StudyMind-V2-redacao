package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.UsoIADiario;
import eduar.studymindredacao.domain.port.UsoIADiarioRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

/**
 * Lê os mesmos contadores que o EnviarRedacaoService e o TranscreverRedacaoService usam para reservar vagas,
 * então o saldo mostrado ao aluno nunca diverge do que dispara o 429.
 * Correções e transcrições em andamento já contam como usadas (a vaga foi reservada).
 */
@Service
public class ConsultarUsoDiarioService {
    private final UsoIADiarioRepositoryPort usoRepository;
    private final Clock clock;
    private final int limiteDiario;
    private final int limiteTranscricoes;

    public ConsultarUsoDiarioService(
            UsoIADiarioRepositoryPort usoRepository,
            Clock clock,
            @Value("${app.limites.correcoes-diarias-free}") int limiteDiario,
            @Value("${app.limites.transcricoes-diarias-free}") int limiteTranscricoes
    ) {
        this.usoRepository = usoRepository;
        this.clock = clock;
        this.limiteDiario = limiteDiario;
        this.limiteTranscricoes = limiteTranscricoes;
    }

    public UsoDiario consultar(UUID usuarioId) {
        return saldo(usuarioId, UsoIADiario::qtdCorrecoes, limiteDiario);
    }

    /** Saldo de transcrições de foto: cota própria, separada das correções (#16). */
    public UsoDiario consultarTranscricoes(UUID usuarioId) {
        return saldo(usuarioId, UsoIADiario::qtdTranscricoes, limiteTranscricoes);
    }

    private UsoDiario saldo(UUID usuarioId, Function<UsoIADiario, Integer> contador, int limite) {
        LocalDate hoje = LocalDate.now(clock);
        int usadas = usoRepository.buscarPorUsuarioIdEData(usuarioId, hoje)
                .map(contador)
                .orElse(0);
        int restantes = Math.max(limite - usadas, 0);
        var renovaEm = hoje.plusDays(1).atStartOfDay(clock.getZone()).toOffsetDateTime();
        return new UsoDiario(usadas, limite, restantes, renovaEm);
    }
}
