package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.RedacaoPersistenceMapper;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.port.RedacaoRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RedacaoRepositoryAdapter implements RedacaoRepositoryPort {
    private final RedacaoJpaRepository repository;
    private final UsuarioJpaRepository usuarioRepository;
    private final TemaJpaRepository temaRepository;

    public RedacaoRepositoryAdapter(
            RedacaoJpaRepository repository,
            UsuarioJpaRepository usuarioRepository,
            TemaJpaRepository temaRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    @Transactional
    public Redacao salvar(Redacao redacao) {
        var usuario = usuarioRepository.getReferenceById(redacao.usuarioId());
        var tema = temaRepository.getReferenceById(redacao.temaId());
        var entity = RedacaoPersistenceMapper.toEntity(redacao, usuario, tema);
        return RedacaoPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Redacao> buscarPorId(UUID id) {
        return repository.findById(id).map(RedacaoPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Redacao> listarPorUsuarioId(UUID usuarioId) {
        return repository.findByUsuario_IdOrderByEnviadaEmDesc(usuarioId).stream()
                .map(RedacaoPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void excluirPorId(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Redacao> listarPorStatusEnviadasAntesDe(StatusRedacao status, OffsetDateTime limite) {
        return repository.findByStatusAndEnviadaEmBefore(status, limite).stream()
                .map(RedacaoPersistenceMapper::toDomain)
                .toList();
    }
}
