package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.PrevisaoTemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface PrevisaoTemaJpaRepository extends JpaRepository<PrevisaoTemaEntity, UUID> {
    List<PrevisaoTemaEntity> findAllByOrderByRankingAsc();
}
