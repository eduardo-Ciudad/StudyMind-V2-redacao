package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.Dificuldade;
import eduar.studymindredacao.domain.model.enums.Nivel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CuradoriaTest {

    @Test
    void curadoriaVaziaNaoTemNada() {
        var curadoria = Curadoria.vazia();

        assertThat(curadoria.riscoUso()).isNull();
        assertThat(curadoria.saturacao()).isNull();
        assertThat(curadoria.temPontuacao()).isFalse();
    }

    @Test
    void aceitaClassificacaoSemPontuacao() {
        var curadoria = new Curadoria(Nivel.MEDIUM, Dificuldade.INTERMEDIATE, Nivel.LOW, null);

        assertThat(curadoria.saturacao()).isEqualTo(Nivel.LOW);
        assertThat(curadoria.temPontuacao()).isFalse();
    }

    @Test
    void indicaQuandoTemPontuacao() {
        var curadoria = new Curadoria(null, null, null, new Pontuacao(4, 5, 4, 5, 5, 5));

        assertThat(curadoria.temPontuacao()).isTrue();
    }
}
