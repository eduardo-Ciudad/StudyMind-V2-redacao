package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.entity.RepertorioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A listagem roda em 2 etapas: aqui só os ids da página (e o total); o adapter carrega as
 * entidades depois, com fontes e problemas em lote (BatchSize). Assim a paginação fica no banco,
 * sem o Hibernate paginar em memória por causa das coleções.
 * Os parâmetros vão com CAST para o Postgres saber o tipo quando chegam nulos.
 */
interface RepertorioJpaRepository extends JpaRepository<RepertorioEntity, UUID> {

    String FILTROS = """
            WHERE r.ativo
              AND (CAST(:tipo AS text) IS NULL OR r.tipo_entidade = CAST(:tipo AS text))
              AND (CAST(:funcao AS text) IS NULL
                   OR r.funcoes_argumentativas @> jsonb_build_array(CAST(:funcao AS text)))
              AND (CAST(:macroeixo AS text) IS NULL OR EXISTS (
                    SELECT 1 FROM repertorio_problemas rp
                    JOIN problemas_sociais p ON p.id = rp.problema_id
                    JOIN macroeixos m ON m.id = p.macroeixo_id
                    WHERE rp.repertorio_id = r.id AND m.slug = CAST(:macroeixo AS text)))
              AND (CAST(:problema AS text) IS NULL OR EXISTS (
                    SELECT 1 FROM repertorio_problemas rp
                    JOIN problemas_sociais p ON p.id = rp.problema_id
                    WHERE rp.repertorio_id = r.id AND p.slug = CAST(:problema AS text)))
              AND (CAST(:busca AS text) IS NULL OR
                    unaccent(lower(r.nome || ' ' || coalesce(r.ideia_central, '') || ' ' || CAST(r.tags AS text)))
                    LIKE '%' || unaccent(lower(CAST(:busca AS text))) || '%' ESCAPE '\\')
            """;

    /** Ids da página, em ordem alfabética do nome (sem acento) e depois pelo código. */
    @Query(nativeQuery = true, value = "SELECT r.id FROM repertorios r " + FILTROS
            + " ORDER BY unaccent(lower(r.nome)), r.codigo LIMIT :limite OFFSET :inicio")
    List<UUID> buscarIds(@Param("tipo") String tipo,
                         @Param("funcao") String funcao,
                         @Param("macroeixo") String macroeixo,
                         @Param("problema") String problema,
                         @Param("busca") String busca,
                         @Param("limite") int limite,
                         @Param("inicio") long inicio);

    /** Total com os mesmos filtros, para montar a paginação. */
    @Query(nativeQuery = true, value = "SELECT count(*) FROM repertorios r " + FILTROS)
    long contar(@Param("tipo") String tipo,
                @Param("funcao") String funcao,
                @Param("macroeixo") String macroeixo,
                @Param("problema") String problema,
                @Param("busca") String busca);

    Optional<RepertorioEntity> findByCodigoAndAtivoTrue(String codigo);
}
