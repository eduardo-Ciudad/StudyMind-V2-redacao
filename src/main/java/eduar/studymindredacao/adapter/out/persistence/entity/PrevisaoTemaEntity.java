package eduar.studymindredacao.adapter.out.persistence.entity;

import eduar.studymindredacao.domain.model.enums.ForcaEvidencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "temas_previsao")
@Getter
@Setter
@NoArgsConstructor
public class PrevisaoTemaEntity {
    @Id
    @Column(name = "tema_id")
    private UUID temaId;

    @Column(nullable = false)
    private Short ranking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ForcaEvidencia forca;

    @Column(nullable = false, length = 120)
    private String eixo;

    @Column(name = "grupo_social", nullable = false, length = 255)
    private String grupoSocial;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String argumentos;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "marcos_legais", nullable = false, columnDefinition = "jsonb")
    private String marcosLegais;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "agentes_intervencao", nullable = false, columnDefinition = "jsonb")
    private String agentesIntervencao;
}
