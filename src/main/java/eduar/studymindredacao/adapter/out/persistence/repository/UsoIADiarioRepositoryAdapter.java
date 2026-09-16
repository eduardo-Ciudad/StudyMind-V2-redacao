package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.UsoIADiarioPersistenceMapper;
import eduar.studymindredacao.domain.model.UsoIADiario;
import eduar.studymindredacao.domain.port.UsoIADiarioRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsoIADiarioRepositoryAdapter implements UsoIADiarioRepositoryPort {
    private final UsoIADiarioJpaRepository repository;
    private final UsuarioJpaRepository usuarioRepository;

    public UsoIADiarioRepositoryAdapter(
            UsoIADiarioJpaRepository repository,
            UsuarioJpaRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public UsoIADiario salvar(UsoIADiario usoIADiario) {
        var usuario = usuarioRepository.getReferenceById(usoIADiario.usuarioId());
        var entity = UsoIADiarioPersistenceMapper.toEntity(usoIADiario, usuario);
        return UsoIADiarioPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsoIADiario> buscarPorId(UUID id) {
        return repository.findById(id).map(UsoIADiarioPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsoIADiario> buscarPorUsuarioIdEData(UUID usuarioId, LocalDate data) {
        return repository.findByUsuario_IdAndData(usuarioId, data)
                .map(UsoIADiarioPersistenceMapper::toDomain);
    }

    @Override
    public void excluirPorId(UUID id) {
        repository.deleteById(id);
    }
}
