package eduar.studymindredacao.adapter.out.persistence.entity;

import eduar.studymindredacao.domain.model.enums.TipoFonte;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "repertorio_fontes")
@Getter
@Setter
@NoArgsConstructor
public class RepertorioFonteEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repertorio_id", nullable = false)
    private RepertorioEntity repertorio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoFonte tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 500)
    private String url;
}
