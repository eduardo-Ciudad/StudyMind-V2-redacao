package eduar.studymindredacao.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "avaliacoes")
@Getter
@Setter
@NoArgsConstructor
public class AvaliacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "redacao_id", nullable = false, unique = true)
    private RedacaoEntity redacao;

    @Column(name = "nota_c1", nullable = false)
    private Short notaC1;

    @Column(name = "nota_c2", nullable = false)
    private Short notaC2;

    @Column(name = "nota_c3", nullable = false)
    private Short notaC3;

    @Column(name = "nota_c4", nullable = false)
    private Short notaC4;

    @Column(name = "nota_c5", nullable = false)
    private Short notaC5;

    @Column(name = "nota_total", nullable = false)
    private Short notaTotal;

    @Column(name = "pontos_fortes", columnDefinition = "TEXT")
    private String pontosFortes;

    @Column(name = "pontos_desenvolvimento", columnDefinition = "TEXT")
    private String pontosDesenvolvimento;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "modelo_ia", length = 50)
    private String modeloIa;

    @Column(name = "tokens_entrada")
    private Integer tokensEntrada;

    @Column(name = "tokens_saida")
    private Integer tokensSaida;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resposta_bruta_json", columnDefinition = "jsonb")
    private String respostaBrutaJson;

    @CreationTimestamp
    @Column(name = "avaliado_em", nullable = false, updatable = false)
    private OffsetDateTime avaliadoEm;
}
