package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.config.SchedulingConfig;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.port.RedacaoRepositoryPort;
import eduar.studymindredacao.domain.port.UsoIADiarioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class RecuperarRedacoesPresasService {
    private static final Logger log = LoggerFactory.getLogger(RecuperarRedacoesPresasService.class);
    private final RedacaoRepositoryPort redacaoRepository;
    private final Clock clock;
    private final UsoIADiarioRepositoryPort usoRepository;
    static final Duration TEMPO_MAXIMO_EM_AVALIACAO = Duration.ofMinutes(10);

    public RecuperarRedacoesPresasService(RedacaoRepositoryPort redacaoRepository, Clock clock, UsoIADiarioRepositoryPort usoRepository) {
        this.redacaoRepository = redacaoRepository;
        this.clock = clock;
        this.usoRepository = usoRepository;
    }

    @Scheduled(fixedDelayString = "PT5M", initialDelayString = "PT1M")
    @Transactional
    public int recuperar() {
        var corte = OffsetDateTime.now(clock).minus(TEMPO_MAXIMO_EM_AVALIACAO);
        var presas = redacaoRepository.listarPorStatusEnviadasAntesDe(StatusRedacao.EM_AVALIACAO, corte);
        for (Redacao presa : presas) {
            LocalDate dia = presa.enviadaEm().atZoneSameInstant(clock.getZone()).toLocalDate();
            redacaoRepository.salvar(presa.comStatus(StatusRedacao.ERRO));
            usoRepository.liberarCorrecao(presa.usuarioId(), dia);
        }
        if (!presas.isEmpty()) {
            log.warn("{} redação(ões) presa(s) em EM_AVALIACAO marcadas como ERRO", presas.size());
        }
        return presas.size();
    }
}
