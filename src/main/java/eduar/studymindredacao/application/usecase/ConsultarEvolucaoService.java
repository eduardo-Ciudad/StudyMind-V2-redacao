package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.CompetenciaAvaliada;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.port.AvaliacaoRepositoryPort;
import eduar.studymindredacao.domain.port.RedacaoRepositoryPort;
import eduar.studymindredacao.domain.port.TemaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Monta a evolução do aluno a partir das redações já corrigidas.
 * Redação anulada aparece na linha do tempo, mas não entra em médias nem na melhor nota:
 * os zeros dela não dizem nada sobre o domínio de cada competência.
 */
@Service
public class ConsultarEvolucaoService {
    /** Quantas redações recentes formam a média que define o foco de estudo. */
    static final int JANELA_MEDIA = 3;
    private static final int NOTA_MAXIMA_COMPETENCIA = 200;

    private final RedacaoRepositoryPort redacaoRepository;
    private final AvaliacaoRepositoryPort avaliacaoRepository;
    private final TemaRepositoryPort temaRepository;

    public ConsultarEvolucaoService(
            RedacaoRepositoryPort redacaoRepository,
            AvaliacaoRepositoryPort avaliacaoRepository,
            TemaRepositoryPort temaRepository
    ) {
        this.redacaoRepository = redacaoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.temaRepository = temaRepository;
    }

    public Evolucao consultar(UUID usuarioId) {
        List<Redacao> avaliadas = redacaoRepository.listarPorUsuarioId(usuarioId).stream()
                .filter(r -> r.status() == StatusRedacao.AVALIADA)
                .sorted(Comparator.comparing(Redacao::enviadaEm))
                .toList();
        if (avaliadas.isEmpty()) {
            return new Evolucao(0, null, null, null, List.of());
        }

        Map<UUID, Avaliacao> avaliacaoPorRedacao = avaliacaoRepository
                .buscarPorRedacaoIds(avaliadas.stream().map(Redacao::id).toList())
                .stream()
                .collect(Collectors.toMap(Avaliacao::redacaoId, Function.identity()));
        Map<UUID, String> tituloPorTema = new HashMap<>();

        List<PontoEvolucao> serie = new ArrayList<>();
        List<Avaliacao> validas = new ArrayList<>();
        for (Redacao redacao : avaliadas) {
            Avaliacao avaliacao = avaliacaoPorRedacao.get(redacao.id());
            if (avaliacao == null) {
                continue; // AVALIADA sem avaliação seria inconsistência; não inventa ponto
            }
            serie.add(new PontoEvolucao(
                    redacao.id(),
                    redacao.enviadaEm(),
                    tituloPorTema.computeIfAbsent(redacao.temaId(), this::tituloDoTema),
                    avaliacao.anulada(),
                    avaliacao.notaTotal(),
                    avaliacao.anulada() ? null : NotasPorCompetencia.de(avaliacao)
            ));
            if (!avaliacao.anulada()) {
                validas.add(avaliacao);
            }
        }

        if (validas.isEmpty()) {
            return new Evolucao(0, null, null, null, serie);
        }
        Integer melhorNota = validas.stream().mapToInt(Avaliacao::notaTotal).max().getAsInt();
        List<Avaliacao> recentes = validas.subList(Math.max(0, validas.size() - JANELA_MEDIA), validas.size());
        NotasPorCompetencia media = media(recentes);
        return new Evolucao(validas.size(), melhorNota, media, foco(media, recentes), serie);
    }

    private static NotasPorCompetencia media(List<Avaliacao> avaliacoes) {
        return new NotasPorCompetencia(
                mediaArredondada(avaliacoes, Avaliacao::notaC1),
                mediaArredondada(avaliacoes, Avaliacao::notaC2),
                mediaArredondada(avaliacoes, Avaliacao::notaC3),
                mediaArredondada(avaliacoes, Avaliacao::notaC4),
                mediaArredondada(avaliacoes, Avaliacao::notaC5)
        );
    }

    private static int mediaArredondada(List<Avaliacao> avaliacoes, Function<Avaliacao, Short> nota) {
        double soma = avaliacoes.stream().mapToInt(a -> nota.apply(a)).sum();
        return (int) Math.round(soma / avaliacoes.size());
    }

    /**
     * Menor média; no empate, a competência com mais problemas apontados nas redações recentes;
     * se ainda empatar, a de menor número. Sem foco quando todas estão no máximo.
     */
    private static Integer foco(NotasPorCompetencia media, List<Avaliacao> recentes) {
        Map<Integer, Integer> problemas = new HashMap<>();
        for (Avaliacao avaliacao : recentes) {
            for (CompetenciaAvaliada competencia : avaliacao.competencias()) {
                problemas.merge(competencia.numero(), competencia.problemasIdentificados().size(), Integer::sum);
            }
        }
        Integer foco = null;
        for (int numero = 1; numero <= 5; numero++) {
            int nota = media.daCompetencia(numero);
            if (nota >= NOTA_MAXIMA_COMPETENCIA) {
                continue;
            }
            if (foco == null || nota < media.daCompetencia(foco)
                    || (nota == media.daCompetencia(foco)
                    && problemas.getOrDefault(numero, 0) > problemas.getOrDefault(foco, 0))) {
                foco = numero;
            }
        }
        return foco;
    }

    private String tituloDoTema(UUID temaId) {
        return temaRepository.buscarPorId(temaId).map(Tema::titulo).orElse(null);
    }
}
