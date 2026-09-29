package eduar.studymindredacao.adapter.out.persistence.mapper;

import eduar.studymindredacao.adapter.out.persistence.entity.RedacaoEntity;
import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.CompetenciaAvaliada;
import eduar.studymindredacao.domain.model.ResultadoAvaliacaoIA;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AvaliacaoPersistenceMapperTest {

    @Test
    void idaEVoltaPreservaCompetenciasEListas() {
        var redacao = new RedacaoEntity();
        redacao.setId(UUID.randomUUID());
        var competencias = List.of(
                new CompetenciaAvaliada(1, 160, "desvios pontuais", "Poucos desvios.", List.of("vírgula antes de 'que'")),
                new CompetenciaAvaliada(2, 120, "repertório pertinente", "Repertório de bolso.", List.of()),
                new CompetenciaAvaliada(3, 120, "argumentos previsíveis", "Argumentos genéricos.", List.of("sem autoria")),
                new CompetenciaAvaliada(4, 160, "coesão adequada", "Bons conectivos.", List.of()),
                new CompetenciaAvaliada(5, 80, "proposta insuficiente", "Falta agente e meio.", List.of("sem agente", "sem meio"))
        );
        var original = Avaliacao.de(redacao.getId(), new ResultadoAvaliacaoIA(
                false, null, competencias,
                List.of("estrutura clara", "tese explícita"), List.of("detalhar a proposta"),
                "Maior fragilidade na C5.", "gemini-teste", 1500, 900, "{\"anulada\":false}"
        ));

        var entity = AvaliacaoPersistenceMapper.toEntity(original, redacao);

        assertThat(entity.getPontosFortes()).isEqualTo("[\"estrutura clara\",\"tese explícita\"]");
        assertThat(entity.getCompetencias()).hasSize(5);
        assertThat(entity.getCompetencias()).allSatisfy(c -> assertThat(c.getAvaliacao()).isSameAs(entity));
        assertThat(entity.getCompetencias().get(4).getProblemas()).isEqualTo("[\"sem agente\",\"sem meio\"]");

        var devolta = AvaliacaoPersistenceMapper.toDomain(entity);

        assertThat(devolta.notaTotal()).isEqualTo((short) 640);
        assertThat(devolta.pontosFortes()).containsExactly("estrutura clara", "tese explícita");
        assertThat(devolta.competencias()).isEqualTo(original.competencias());
    }

    @Test
    void listaNulaOuVaziaNoBancoViraListaVazia() {
        assertThat(AvaliacaoPersistenceMapper.lerLista(null)).isEmpty();
        assertThat(AvaliacaoPersistenceMapper.lerLista("[]")).isEmpty();
        assertThat(AvaliacaoPersistenceMapper.escreverLista(null)).isEqualTo("[]");
    }
}
