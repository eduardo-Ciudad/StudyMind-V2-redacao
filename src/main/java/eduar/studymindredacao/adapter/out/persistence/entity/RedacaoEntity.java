package eduar.studymindredacao.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import eduar.studymindredacao.domain.model.enums.OrigemRedacao;
import eduar.studymindredacao.domain.model.enums.StatusRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "redacoes",
        indexes = {
                @Index(name = "idx_redacoes_usuario", columnList = "usuario_id, enviada_em"),
                @Index(name = "idx_redacoes_tema", columnList = "tema_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class RedacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tema_id", nullable = false)
    private TemaEntity tema;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoRedacao tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusRedacao status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigemRedacao origem;

    @CreationTimestamp
    @Column(name = "enviada_em", nullable = false, updatable = false)
    private OffsetDateTime enviadaEm;
}
