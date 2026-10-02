package eduar.studymindredacao.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/** Inserções e remoções vão por query nativa (idempotentes); a entidade existe para o Spring Data e o ddl validate. */
@Entity
@Table(name = "usuario_temas")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioTemaEntity {
    @EmbeddedId
    private UsuarioTemaId id;

    @Column(name = "adicionado_em", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime adicionadoEm;
}
