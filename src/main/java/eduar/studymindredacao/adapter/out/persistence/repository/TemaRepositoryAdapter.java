package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.TemaPersistenceMapper;
import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.port.TemaRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TemaRepositoryAdapter implements TemaRepositoryPort {
    private final TemaJpaRepository repository;

    public TemaRepositoryAdapter(TemaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Tema salvar(Tema tema) {
        var entity = TemaPersistenceMapper.toEntity(tema);
        return TemaPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tema> buscarPorId(UUID id) {
        return repository.findById(id).map(TemaPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tema> listarAtivos() {
        return repository.findByAtivoTrueOrderByAnoDescTituloAsc().stream()
                .map(TemaPersistenceMapper::toDomain)
                .toList();
    }
}
