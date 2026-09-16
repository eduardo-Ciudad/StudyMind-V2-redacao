package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.RedacaoPersistenceMapper;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.port.RedacaoRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RedacaoRepositoryAdapter implements RedacaoRepositoryPort {
    private final RedacaoJpaRepository repository;
    private final UsuarioJpaRepository usuarioRepository;

    public RedacaoRepositoryAdapter(
            RedacaoJpaRepository repository,
            UsuarioJpaRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Redacao salvar(Redacao redacao) {
        var usuario = usuarioRepository.getReferenceById(redacao.usuarioId());
        var entity = RedacaoPersistenceMapper.toEntity(redacao, usuario);
        return RedacaoPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Redacao> buscarPorId(UUID id) {
        return repository.findById(id).map(RedacaoPersistenceMapper::toDomain);
    }

    @Override
    public void excluirPorId(UUID id) {
        repository.deleteById(id);
    }
}
