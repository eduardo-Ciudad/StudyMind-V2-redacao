package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.RedacaoEntity;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

interface RedacaoJpaRepository extends JpaRepository<RedacaoEntity, UUID> {
    List<RedacaoEntity> findByUsuario_IdOrderByEnviadaEmDesc(UUID usuarioId);

    List<RedacaoEntity> findByStatusAndEnviadaEmBefore(StatusRedacao status, OffsetDateTime limite);

}
