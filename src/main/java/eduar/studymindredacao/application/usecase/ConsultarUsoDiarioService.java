package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.UsoIADiario;
import eduar.studymindredacao.domain.port.UsoIADiarioRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Lê o mesmo contador que o EnviarRedacaoService usa para reservar vagas,
 * então o saldo mostrado ao aluno nunca diverge do que dispara o 429.
 * Correções em andamento já contam como usadas (a vaga foi reservada).
 */
@Service
public class ConsultarUsoDiarioService {
    private final UsoIADiarioRepositoryPort usoRepository;
    private final Clock clock;
    private final int limiteDiario;

    public ConsultarUsoDiarioService(
            UsoIADiarioRepositoryPort usoRepository,
            Clock clock,
            @Value("${app.limites.correcoes-diarias-free}") int limiteDiario
    ) {
        this.usoRepository = usoRepository;
        this.clock = clock;
        this.limiteDiario = limiteDiario;
    }

    public UsoDiario consultar(UUID usuarioId) {
        LocalDate hoje = LocalDate.now(clock);
        int usadas = usoRepository.buscarPorUsuarioIdEData(usuarioId, hoje)
                .map(UsoIADiario::qtdCorrecoes)
                .orElse(0);
        int restantes = Math.max(limiteDiario - usadas, 0);
        var renovaEm = hoje.plusDays(1).atStartOfDay(clock.getZone()).toOffsetDateTime();
        return new UsoDiario(usadas, limiteDiario, restantes, renovaEm);
    }
}
