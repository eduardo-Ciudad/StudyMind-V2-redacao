package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.OrigemTema;
import eduar.studymindredacao.domain.port.TemaRepositoryPort;
import eduar.studymindredacao.domain.port.UsuarioTemaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarTemasService {
    private final TemaRepositoryPort temaRepository;
    private final UsuarioTemaRepositoryPort usuarioTemaRepository;

    public ListarTemasService(TemaRepositoryPort temaRepository, UsuarioTemaRepositoryPort usuarioTemaRepository) {
        this.temaRepository = temaRepository;
        this.usuarioTemaRepository = usuarioTemaRepository;
    }

    public List<Tema> listarAtivos() {
        return temaRepository.listarAtivos();
    }

    /** Temas oficiais e autorais ativos, mais os temas possíveis que este aluno adicionou à lista. */
    public List<Tema> listarDisponiveis(UUID usuarioId) {
        var adicionados = usuarioTemaRepository.listarTemaIds(usuarioId);
        return temaRepository.listarAtivos().stream()
                .filter(t -> t.origem() != OrigemTema.PREVISAO || adicionados.contains(t.id()))
                .toList();
    }
}
