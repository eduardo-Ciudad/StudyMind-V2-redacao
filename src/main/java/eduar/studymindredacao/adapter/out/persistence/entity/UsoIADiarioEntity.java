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

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "uso_ia_diario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "data"})
)
@Getter
@Setter
@NoArgsConstructor
public class UsoIADiarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "tokens_entrada", nullable = false)
    private Integer tokensEntrada;

    @Column(name = "tokens_saida", nullable = false)
    private Integer tokensSaida;

    @Column(name = "qtd_correcoes", nullable = false)
    private Integer qtdCorrecoes;

    @Column(name = "qtd_roadmaps", nullable = false)
    private Integer qtdRoadmaps;

    @Column(name = "qtd_transcricoes", nullable = false)
    private Integer qtdTranscricoes;
}
