package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AvaliacaoJpaRepository extends JpaRepository<AvaliacaoEntity, UUID> {
    Optional<AvaliacaoEntity> findByRedacao_Id(UUID redacaoId);

    List<AvaliacaoEntity> findByRedacao_IdIn(Collection<UUID> redacaoIds);
}
