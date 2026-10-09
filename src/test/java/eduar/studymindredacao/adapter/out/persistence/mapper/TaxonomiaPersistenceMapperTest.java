package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.MacroeixoEntity;
import eduar.studymindredacao.adapter.out.persistence.entity.ProblemaSocialEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TaxonomiaPersistenceMapperTest {

    private static MacroeixoEntity saude() {
        var entity = new MacroeixoEntity();
        entity.setId(UUID.randomUUID());
        entity.setSlug("saude");
        entity.setNome("Saúde");
        entity.setOrdem((short) 1);
        return entity;
    }

    @Test
    void converteMacroeixo() {
        var entity = saude();

        var macroeixo = TaxonomiaPersistenceMapper.toDomain(entity);

        assertThat(macroeixo.id()).isEqualTo(entity.getId());
        assertThat(macroeixo.slug()).isEqualTo("saude");
        assertThat(macroeixo.nome()).isEqualTo("Saúde");
        assertThat(macroeixo.ordem()).isEqualTo(1);
    }

    @Test
    void converteProblemaComOSlugDoMacroeixo() {
        var entity = new ProblemaSocialEntity();
        entity.setId(UUID.randomUUID());
        entity.setSlug("saude-mental");
        entity.setNome("Saúde mental");
        entity.setMacroeixo(saude());

        var problema = TaxonomiaPersistenceMapper.toDomain(entity);

        assertThat(problema.id()).isEqualTo(entity.getId());
        assertThat(problema.slug()).isEqualTo("saude-mental");
        assertThat(problema.nome()).isEqualTo("Saúde mental");
        assertThat(problema.macroeixoSlug()).isEqualTo("saude");
    }
}
