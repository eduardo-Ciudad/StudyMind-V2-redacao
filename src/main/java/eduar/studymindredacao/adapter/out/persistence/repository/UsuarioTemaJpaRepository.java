package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.UsuarioTemaEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.UsuarioTemaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

interface UsuarioTemaJpaRepository extends JpaRepository<UsuarioTemaEntity, UsuarioTemaId> {

    @Modifying
    @Query(value = """
            INSERT INTO usuario_temas (usuario_id, tema_id)
            VALUES (:usuarioId, :temaId)
            ON CONFLICT (usuario_id, tema_id) DO NOTHING
            """, nativeQuery = true)
    int adicionar(@Param("usuarioId") UUID usuarioId, @Param("temaId") UUID temaId);

    @Modifying
    @Query(value = "DELETE FROM usuario_temas WHERE usuario_id = :usuarioId AND tema_id = :temaId", nativeQuery = true)
    int remover(@Param("usuarioId") UUID usuarioId, @Param("temaId") UUID temaId);

    @Query("SELECT u.id.temaId FROM UsuarioTemaEntity u WHERE u.id.usuarioId = :usuarioId")
    List<UUID> listarTemaIds(@Param("usuarioId") UUID usuarioId);
}
