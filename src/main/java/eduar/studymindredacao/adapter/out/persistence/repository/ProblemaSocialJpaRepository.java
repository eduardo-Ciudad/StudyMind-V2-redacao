package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.ProblemaSocialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface ProblemaSocialJpaRepository extends JpaRepository<ProblemaSocialEntity, UUID> {
    /** Join fetch: carrega o macroeixo junto, numa consulta só. */
    @Query("select p from ProblemaSocialEntity p join fetch p.macroeixo m order by m.ordem, p.nome")
    List<ProblemaSocialEntity> listarComMacroeixo();
}
