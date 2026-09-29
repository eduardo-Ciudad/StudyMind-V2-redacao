package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.ResultadoAvaliacaoIA;
import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.port.AvaliacaoRepositoryPort;
import eduar.studymindredacao.domain.port.RedacaoRepositoryPort;
import eduar.studymindredacao.domain.port.UsoIADiarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Grava o resultado de uma correção numa única transação: avaliação, status AVALIADA e tokens.
 * Fica numa classe separada do EnviarRedacaoService para que a chamada ao Gemini
 * (lenta) aconteça fora de transação, e só esta parte (rápida) segure conexão com o banco.
 */
@Service
public class ConcluirAvaliacaoService {
    private final AvaliacaoRepositoryPort avaliacaoRepository;
    private final RedacaoRepositoryPort redacaoRepository;
    private final UsoIADiarioRepositoryPort usoRepository;

    public ConcluirAvaliacaoService(
            AvaliacaoRepositoryPort avaliacaoRepository,
            RedacaoRepositoryPort redacaoRepository,
            UsoIADiarioRepositoryPort usoRepository
    ) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.redacaoRepository = redacaoRepository;
        this.usoRepository = usoRepository;
    }

    @Transactional
    public RedacaoDetalhada concluir(Redacao redacao, Tema tema, ResultadoAvaliacaoIA resultado, LocalDate dia) {
        var avaliacao = avaliacaoRepository.salvar(Avaliacao.de(redacao.id(), resultado));
        var avaliada = redacaoRepository.salvar(redacao.comStatus(StatusRedacao.AVALIADA));
        usoRepository.registrarTokens(redacao.usuarioId(), dia, resultado.tokensEntrada(), resultado.tokensSaida());
        return new RedacaoDetalhada(avaliada, tema, avaliacao);
    }
}
