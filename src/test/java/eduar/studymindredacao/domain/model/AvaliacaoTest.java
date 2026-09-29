package eduar.studymindredacao.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AvaliacaoTest {
    private final UUID redacaoId = UUID.randomUUID();

    private static CompetenciaAvaliada competencia(int numero, int nota) {
        return new CompetenciaAvaliada(numero, nota, "descritor " + nota, "resumo C" + numero, List.of());
    }

    private static ResultadoAvaliacaoIA resultado(boolean anulada, String motivo, List<CompetenciaAvaliada> competencias) {
        return new ResultadoAvaliacaoIA(
                anulada, motivo, competencias,
                List.of("boa estrutura"), List.of("aprofundar repertório"),
                "diagnóstico", "gemini-teste", 1200, 800, "{\"anulada\":false}"
        );
    }

    @Test
    void montaAPartirDoResultadoDaIADerivandoNotasETotal() {
        var competencias = List.of(
                competencia(3, 120), competencia(1, 160), competencia(5, 80), competencia(2, 120), competencia(4, 160)
        );

        var avaliacao = Avaliacao.de(redacaoId, resultado(false, null, competencias));

        assertThat(avaliacao.notaC1()).isEqualTo((short) 160);
        assertThat(avaliacao.notaC3()).isEqualTo((short) 120);
        assertThat(avaliacao.notaC5()).isEqualTo((short) 80);
        assertThat(avaliacao.notaTotal()).isEqualTo((short) 640);
        assertThat(avaliacao.competencias()).extracting(CompetenciaAvaliada::numero).containsExactly(1, 2, 3, 4, 5);
        assertThat(avaliacao.pontosFortes()).containsExactly("boa estrutura");
        assertThat(avaliacao.tokensEntrada()).isEqualTo(1200);
    }

    @Test
    void redacaoAnuladaGuardaMotivoENotaZero() {
        var zeradas = List.of(competencia(1, 0), competencia(2, 0), competencia(3, 0), competencia(4, 0), competencia(5, 0));

        var avaliacao = Avaliacao.de(redacaoId, resultado(true, "Fuga total ao tema", zeradas));

        assertThat(avaliacao.anulada()).isTrue();
        assertThat(avaliacao.motivoAnulacao()).isEqualTo("Fuga total ao tema");
        assertThat(avaliacao.notaTotal()).isZero();
    }

    @Test
    void rejeitaAnulacaoSemMotivo() {
        short zero = 0;
        assertThatThrownBy(() -> new Avaliacao(
                null, redacaoId, zero, zero, zero, zero, zero, null, true, " ",
                null, null, null, null, null, null, null, null, null
        )).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("motivoAnulacao");
    }

    @Test
    void rejeitaCompetenciaComNotaDiferenteDaColuna() {
        var competencias = List.of(competencia(1, 160), competencia(2, 120), competencia(3, 120), competencia(4, 160), competencia(5, 80));
        short c1Divergente = 200;

        assertThatThrownBy(() -> new Avaliacao(
                null, redacaoId, c1Divergente, (short) 120, (short) 120, (short) 160, (short) 80, null, false, null,
                competencias, null, null, null, null, null, null, null, null
        )).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("C1");
    }

    @Test
    void aceitaAvaliacaoSemDetalheDeCompetencias() {
        short n = 120;
        var avaliacao = new Avaliacao(null, redacaoId, n, n, n, n, n, null, false, null,
                null, null, null, null, null, null, null, null, null);

        assertThat(avaliacao.competencias()).isEmpty();
        assertThat(avaliacao.pontosFortes()).isEmpty();
        assertThat(avaliacao.notaTotal()).isEqualTo((short) 600);
    }
}