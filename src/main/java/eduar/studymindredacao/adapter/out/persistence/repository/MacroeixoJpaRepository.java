package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.MacroeixoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface MacroeixoJpaRepository extends JpaRepository<MacroeixoEntity, UUID> {
    List<MacroeixoEntity> findAllByOrderByOrdemAsc();
}
