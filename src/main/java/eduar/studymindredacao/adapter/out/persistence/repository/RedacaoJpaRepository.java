package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.RedacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface RedacaoJpaRepository extends JpaRepository<RedacaoEntity, UUID> {
}
