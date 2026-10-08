package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.ImagemInvalidaException;
import eduar.studymindredacao.domain.exception.ImagemInvalidaException.Motivo;
import eduar.studymindredacao.domain.exception.LimiteDiarioAtingidoException;
import eduar.studymindredacao.domain.exception.LimiteGlobalAtingidoException;
import eduar.studymindredacao.domain.model.ImagemRedacao;
import eduar.studymindredacao.domain.model.ResultadoTranscricao;
import eduar.studymindredacao.domain.model.enums.RecursoIA;
import eduar.studymindredacao.domain.port.TranscricaoIAPort;
import eduar.studymindredacao.domain.port.UsoIADiarioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Transcreve a foto de uma redação manuscrita:
 * valida as imagens → teto global → reserva a vaga do dia → IA → registra os tokens.
 * Se a IA falhar, ou não encontrar redação na foto, a vaga é devolvida: o aluno não recebeu nada útil.
 * Nada é persistido além do contador: a imagem e o texto ficam só em memória (#12).
 * Sem @Transactional de propósito, como no EnviarRedacaoService: a chamada à IA não segura conexão.
 */
@Service
public class TranscreverRedacaoService {
    static final int MAX_IMAGENS = 2;
    private static final Logger log = LoggerFactory.getLogger(TranscreverRedacaoService.class);

    private final UsoIADiarioRepositoryPort usoRepository;
    private final TranscricaoIAPort transcricaoIA;
    private final Clock clock;
    private final int limiteDiario;
    private final int limiteGlobal;

    public TranscreverRedacaoService(
            UsoIADiarioRepositoryPort usoRepository,
            TranscricaoIAPort transcricaoIA,
            Clock clock,
            @Value("${app.limites.transcricoes-diarias-free}") int limiteDiario,
            @Value("${app.limites.transcricoes-diarias-globais}") int limiteGlobal
    ) {
        this.usoRepository = usoRepository;
        this.transcricaoIA = transcricaoIA;
        this.clock = clock;
        this.limiteDiario = limiteDiario;
        this.limiteGlobal = limiteGlobal;
    }

    public ResultadoTranscricao transcrever(UUID usuarioId, List<ImagemRedacao> imagens) {
        validarQuantidade(imagens);

        LocalDate hoje = LocalDate.now(clock);
        // Teto de custo do sistema, checado antes da reserva do aluno (aproximado sob concorrência, como na correção)
        if (usoRepository.somarTranscricoesDoDia(hoje) >= limiteGlobal) {
            throw new LimiteGlobalAtingidoException(RecursoIA.TRANSCRICAO);
        }
        if (!usoRepository.reservarTranscricao(usuarioId, hoje, limiteDiario)) {
            throw new LimiteDiarioAtingidoException(RecursoIA.TRANSCRICAO, limiteDiario);
        }

        ResultadoTranscricao resultado;
        try {
            resultado = transcricaoIA.transcrever(imagens);
        } catch (RuntimeException e) {
            log.warn("Transcrição falhou ({}): {}", e.getClass().getSimpleName(), LogSeguro.limpar(e.getMessage()));
            usoRepository.liberarTranscricao(usuarioId, hoje);
            throw e;
        }

        registrarTokens(usuarioId, hoje, resultado);
        return resultado;
    }

    /** Contabilidade de custo: se falhar, o aluno não perde a transcrição que já foi feita. */
    private void registrarTokens(UUID usuarioId, LocalDate dia, ResultadoTranscricao resultado) {
        try {
            usoRepository.registrarTokens(usuarioId, dia, resultado.tokensEntrada(), resultado.tokensSaida());
        } catch (RuntimeException e) {
            log.warn("Não foi possível registrar os tokens da transcrição: {}", LogSeguro.limpar(e.getMessage()));
        }
    }

    private static void validarQuantidade(List<ImagemRedacao> imagens) {
        if (imagens == null || imagens.isEmpty() || imagens.size() > MAX_IMAGENS) {
            throw new ImagemInvalidaException(
                    Motivo.QUANTIDADE_INVALIDA, "Envie 1 ou " + MAX_IMAGENS + " fotos da redação."
            );
        }
    }
}
