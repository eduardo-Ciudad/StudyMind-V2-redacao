package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.PerfilAlunoPersistenceMapper;
import eduar.studymindredacao.domain.model.PerfilAluno;
import eduar.studymindredacao.domain.port.PerfilAlunoRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PerfilAlunoRepositoryAdapter implements PerfilAlunoRepositoryPort {
    private final PerfilAlunoJpaRepository repository;
    private final UsuarioJpaRepository usuarioRepository;

    public PerfilAlunoRepositoryAdapter(
            PerfilAlunoJpaRepository repository,
            UsuarioJpaRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public PerfilAluno salvar(PerfilAluno perfilAluno) {
        var usuario = usuarioRepository.getReferenceById(perfilAluno.usuarioId());
        var entity = PerfilAlunoPersistenceMapper.toEntity(perfilAluno, usuario);
        return PerfilAlunoPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerfilAluno> buscarPorId(UUID id) {
        return repository.findById(id).map(PerfilAlunoPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerfilAluno> buscarPorUsuarioId(UUID usuarioId) {
        return repository.findByUsuario_Id(usuarioId).map(PerfilAlunoPersistenceMapper::toDomain);
    }

    @Override
    public void excluirPorId(UUID id) {
        repository.deleteById(id);
    }
}
