package eduar.studymindredacao.domain.exception;

import eduar.studymindredacao.domain.exception.ImagemInvalidaException.Motivo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImagemInvalidaExceptionTest {

    @Test
    void guardaOMotivoEAMensagem() {
        var excecao = new ImagemInvalidaException(Motivo.QUANTIDADE_INVALIDA, "Envie 1 ou 2 fotos.");

        assertThat(excecao.getMotivo()).isEqualTo(Motivo.QUANTIDADE_INVALIDA);
        assertThat(excecao).hasMessage("Envie 1 ou 2 fotos.");
    }

    /** Os nomes dos motivos são o contrato com o front (campo "motivo" da resposta 400). */
    @Test
    void motivosSaoOsCombinadosComOFront() {
        assertThat(Motivo.values())
                .extracting(Enum::name)
                .containsExactly("VAZIA", "GRANDE_DEMAIS", "TIPO_INVALIDO", "QUANTIDADE_INVALIDA");
    }
}