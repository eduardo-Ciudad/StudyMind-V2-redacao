package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.RedacaoNaoEncontradaException;
import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.port.AvaliacaoRepositoryPort;
import eduar.studymindredacao.domain.port.RedacaoRepositoryPort;
import eduar.studymindredacao.domain.port.TemaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConsultarRedacoesService {
    private final RedacaoRepositoryPort redacaoRepository;
    private final AvaliacaoRepositoryPort avaliacaoRepository;
    private final TemaRepositoryPort temaRepository;

    public ConsultarRedacoesService(
            RedacaoRepositoryPort redacaoRepository,
            AvaliacaoRepositoryPort avaliacaoRepository,
            TemaRepositoryPort temaRepository
    ) {
        this.redacaoRepository = redacaoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.temaRepository = temaRepository;
    }

    /** Histórico do aluno, da mais recente para a mais antiga. */
    public List<RedacaoResumo> listar(UUID usuarioId) {
        List<Redacao> redacoes = redacaoRepository.listarPorUsuarioId(usuarioId);
        if (redacoes.isEmpty()) {
            return List.of();
        }

        Map<UUID, Short> notaPorRedacao = avaliacaoRepository
                .buscarPorRedacaoIds(redacoes.stream().map(Redacao::id).toList())
                .stream()
                .collect(Collectors.toMap(Avaliacao::redacaoId, Avaliacao::notaTotal));

        Map<UUID, String> tituloPorTema = new HashMap<>();
        return redacoes.stream()
                .map(r -> new RedacaoResumo(
                        r,
                        tituloPorTema.computeIfAbsent(r.temaId(), this::tituloDoTema),
                        notaPorRedacao.get(r.id())
                ))
                .toList();
    }

    /** Detalhe de uma redação do próprio aluno; redação de outro aluno responde como inexistente. */
    public RedacaoDetalhada buscar(UUID usuarioId, UUID redacaoId) {
        Redacao redacao = redacaoRepository.buscarPorId(redacaoId)
                .filter(r -> r.usuarioId().equals(usuarioId))
                .orElseThrow(() -> new RedacaoNaoEncontradaException(redacaoId));

        Tema tema = temaRepository.buscarPorId(redacao.temaId()).orElse(null);
        Avaliacao avaliacao = avaliacaoRepository.buscarPorRedacaoId(redacao.id()).orElse(null);
        return new RedacaoDetalhada(redacao, tema, avaliacao);
    }

    private String tituloDoTema(UUID temaId) {
        return temaRepository.buscarPorId(temaId).map(Tema::titulo).orElse(null);
    }
}
