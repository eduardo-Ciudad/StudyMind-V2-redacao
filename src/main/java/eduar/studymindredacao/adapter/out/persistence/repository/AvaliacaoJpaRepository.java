package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface AvaliacaoJpaRepository extends JpaRepository<AvaliacaoEntity, UUID> {
}
