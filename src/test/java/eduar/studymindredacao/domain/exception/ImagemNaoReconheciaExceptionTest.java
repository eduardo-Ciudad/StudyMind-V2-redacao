package eduar.studymindredacao.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImagemNaoReconhecidaExceptionTest {

    @Test
    void mensagemOrientaOAlunoATirarOutraFoto() {
        var excecao = new ImagemNaoReconhecidaException();

        assertThat(excecao)
                .hasMessage(ImagemNaoReconhecidaException.MENSAGEM)
                .hasMessageContaining("Tire outra")
                .hasNoCause();
    }
}