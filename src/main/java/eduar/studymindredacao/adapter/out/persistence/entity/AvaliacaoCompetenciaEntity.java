package eduar.studymindredacao.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(
        name = "avaliacao_competencias",
        uniqueConstraints = @UniqueConstraint(name = "uk_avaliacao_competencia", columnNames = {"avaliacao_id", "numero"})
)
@Getter
@Setter
@NoArgsConstructor
public class AvaliacaoCompetenciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private AvaliacaoEntity avaliacao;

    @Column(nullable = false)
    private Short numero;

    @Column(nullable = false)
    private Short nota;

    @Column(name = "nivel_referencia", columnDefinition = "TEXT")
    private String nivelReferencia;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String resumo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String problemas;
}