package eduar.studymindredacao.adapter.out.persistence.entity;

import eduar.studymindredacao.domain.model.enums.Dificuldade;
import eduar.studymindredacao.domain.model.enums.Nivel;
import eduar.studymindredacao.domain.model.enums.PapelRepertorio;
import eduar.studymindredacao.domain.model.enums.StatusVerificacao;
import eduar.studymindredacao.domain.model.enums.TipoEntidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Só leitura: o conteúdo entra pelas migrations de seed (V9–V14).
 * Fontes e problemas são Set (e não List) para poderem ser carregados juntos sem
 * MultipleBagFetchException; o BatchSize carrega as coleções da página em lote, sem N+1.
 */
@Entity
@Table(name = "repertorios")
@Getter
@Setter
@NoArgsConstructor
public class RepertorioEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entidade", nullable = false, length = 20)
    private TipoEntidade tipoEntidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PapelRepertorio papel;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 255)
    private String subtitulo;

    @Column(name = "tipo_descricao", length = 120)
    private String tipoDescricao;

    @Column(length = 255)
    private String area;

    @Column(length = 80)
    private String pais;

    // Conteúdo didático
    @Column(name = "ideia_central", columnDefinition = "TEXT")
    private String ideiaCentral;

    @Column(name = "lembre_na_prova", columnDefinition = "TEXT")
    private String lembreNaProva;

    @Column(name = "como_usar", columnDefinition = "TEXT")
    private String comoUsar;

    @Column(name = "exemplo_aplicacao", columnDefinition = "TEXT")
    private String exemploAplicacao;

    @Column(name = "erro_comum", columnDefinition = "TEXT")
    private String erroComum;

    // Curadoria
    @Enumerated(EnumType.STRING)
    @Column(name = "risco_uso", length = 10)
    private Nivel riscoUso;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Dificuldade dificuldade;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Nivel saturacao;

    @Column(name = "nota_versatilidade")
    private Short notaVersatilidade;

    @Column(name = "nota_autoridade")
    private Short notaAutoridade;

    @Column(name = "nota_compreensao")
    private Short notaCompreensao;

    @Column(name = "nota_aplicabilidade")
    private Short notaAplicabilidade;

    @Column(name = "nota_especificidade")
    private Short notaEspecificidade;

    @Column(name = "nota_originalidade")
    private Short notaOriginalidade;

    // Listas em JSONB (lidas pelo mapper)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tipos_evidencia", nullable = false, columnDefinition = "jsonb")
    private String tiposEvidencia;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "funcoes_argumentativas", nullable = false, columnDefinition = "jsonb")
    private String funcoesArgumentativas;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tipos_argumento", nullable = false, columnDefinition = "jsonb")
    private String tiposArgumento;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String tags;

    // Evidência
    @Column(columnDefinition = "TEXT")
    private String populacao;

    @Column(name = "ano_evidencia")
    private Short anoEvidencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_verificacao", nullable = false, length = 10)
    private StatusVerificacao statusVerificacao;

    @Column(name = "verificado_em")
    private LocalDate verificadoEm;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    @OneToMany(mappedBy = "repertorio")
    @OrderBy("descricao ASC")
    @BatchSize(size = 25)
    private Set<RepertorioFonteEntity> fontes = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "repertorio_problemas",
            joinColumns = @JoinColumn(name = "repertorio_id"),
            inverseJoinColumns = @JoinColumn(name = "problema_id")
    )
    @OrderBy("nome ASC")
    @BatchSize(size = 25)
    private Set<ProblemaSocialEntity> problemas = new LinkedHashSet<>();
}
