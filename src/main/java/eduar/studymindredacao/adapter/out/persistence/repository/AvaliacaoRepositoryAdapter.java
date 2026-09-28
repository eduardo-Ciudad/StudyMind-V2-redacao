package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.AvaliacaoPersistenceMapper;
import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.port.AvaliacaoRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AvaliacaoRepositoryAdapter implements AvaliacaoRepositoryPort {
    private final AvaliacaoJpaRepository repository;
    private final RedacaoJpaRepository redacaoRepository;

    public AvaliacaoRepositoryAdapter(
            AvaliacaoJpaRepository repository,
            RedacaoJpaRepository redacaoRepository
    ) {
        this.repository = repository;
        this.redacaoRepository = redacaoRepository;
    }

    @Override
    @Transactional
    public Avaliacao salvar(Avaliacao avaliacao) {
        var redacao = redacaoRepository.getReferenceById(avaliacao.redacaoId());
        var entity = AvaliacaoPersistenceMapper.toEntity(avaliacao, redacao);
        return AvaliacaoPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Avaliacao> buscarPorId(UUID id) {
        return repository.findById(id).map(AvaliacaoPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Avaliacao> buscarPorRedacaoId(UUID redacaoId) {
        return repository.findByRedacao_Id(redacaoId).map(AvaliacaoPersistenceMapper::toDomain);
    }

    @Override
    public void excluirPorId(UUID id) {
        repository.deleteById(id);
    }
}
