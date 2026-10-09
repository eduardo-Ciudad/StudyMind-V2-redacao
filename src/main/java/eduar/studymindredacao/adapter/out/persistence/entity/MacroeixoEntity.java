package eduar.studymindredacao.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.UUID;

/** BatchSize: ao converter vários problemas, os macroeixos (lazy) são carregados em lote, não um por um. */
@Entity
@Table(name = "macroeixos")
@BatchSize(size = 25)
@Getter
@Setter
@NoArgsConstructor
public class MacroeixoEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 60)
    private String slug;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private Short ordem;
}
