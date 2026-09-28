package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.TemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface TemaJpaRepository extends JpaRepository<TemaEntity, UUID> {
    List<TemaEntity> findByAtivoTrueOrderByAnoDescTituloAsc();
}
