package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.PerfilAlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface PerfilAlunoJpaRepository extends JpaRepository<PerfilAlunoEntity, UUID> {
    Optional<PerfilAlunoEntity> findByUsuario_Id(UUID usuarioId);
}
