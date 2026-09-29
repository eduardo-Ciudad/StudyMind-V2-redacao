package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.port.TemaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarTemasService {
    private final TemaRepositoryPort temaRepository;

    public ListarTemasService(TemaRepositoryPort temaRepository) {
        this.temaRepository = temaRepository;
    }

    public List<Tema> listarAtivos() {
        return temaRepository.listarAtivos();
    }
}
