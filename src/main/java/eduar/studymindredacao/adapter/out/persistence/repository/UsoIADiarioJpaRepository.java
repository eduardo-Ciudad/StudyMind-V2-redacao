package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.UsoIADiarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

interface UsoIADiarioJpaRepository extends JpaRepository<UsoIADiarioEntity, UUID> {
    Optional<UsoIADiarioEntity> findByUsuario_IdAndData(UUID usuarioId, LocalDate data);
}
