package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.PrevisaoTemaPersistenceMapper;
import eduar.studymindredacao.domain.model.PrevisaoTema;
import eduar.studymindredacao.domain.port.PrevisaoTemaRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PrevisaoTemaRepositoryAdapter implements PrevisaoTemaRepositoryPort {
    private final PrevisaoTemaJpaRepository repository;

    public PrevisaoTemaRepositoryAdapter(PrevisaoTemaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrevisaoTema> listarPorRanking() {
        return repository.findAllByOrderByRankingAsc().stream()
                .map(PrevisaoTemaPersistenceMapper::toDomain)
                .toList();
    }
}
