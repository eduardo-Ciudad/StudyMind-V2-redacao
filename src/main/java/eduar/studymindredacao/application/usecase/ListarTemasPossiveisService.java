package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.OrigemTema;
import eduar.studymindredacao.domain.port.PrevisaoTemaRepositoryPort;
import eduar.studymindredacao.domain.port.TemaRepositoryPort;
import eduar.studymindredacao.domain.port.UsuarioTemaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ListarTemasPossiveisService {
    private final TemaRepositoryPort temaRepository;
    private final PrevisaoTemaRepositoryPort previsaoRepository;
    private final UsuarioTemaRepositoryPort usuarioTemaRepository;

    public ListarTemasPossiveisService(
            TemaRepositoryPort temaRepository,
            PrevisaoTemaRepositoryPort previsaoRepository,
            UsuarioTemaRepositoryPort usuarioTemaRepository
    ) {
        this.temaRepository = temaRepository;
        this.previsaoRepository = previsaoRepository;
        this.usuarioTemaRepository = usuarioTemaRepository;
    }

    /** Temas possíveis ativos, em ordem de ranking, marcando os que o aluno já adicionou. */
    public List<TemaPossivel> listar(UUID usuarioId) {
        Map<UUID, Tema> temasPrevisao = temaRepository.listarAtivos().stream()
                .filter(t -> t.origem() == OrigemTema.PREVISAO)
                .collect(Collectors.toMap(Tema::id, Function.identity()));
        var adicionados = usuarioTemaRepository.listarTemaIds(usuarioId);

        return previsaoRepository.listarPorRanking().stream()
                .filter(p -> temasPrevisao.containsKey(p.temaId()))
                .map(p -> new TemaPossivel(temasPrevisao.get(p.temaId()), p, adicionados.contains(p.temaId())))
                .toList();
    }
}
