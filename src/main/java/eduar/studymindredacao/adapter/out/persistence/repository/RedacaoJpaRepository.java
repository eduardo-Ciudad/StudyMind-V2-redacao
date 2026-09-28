package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.RedacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface RedacaoJpaRepository extends JpaRepository<RedacaoEntity, UUID> {
    List<RedacaoEntity> findByUsuario_IdOrderByEnviadaEmDesc(UUID usuarioId);
}
