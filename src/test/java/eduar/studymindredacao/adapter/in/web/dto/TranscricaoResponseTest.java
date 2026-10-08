package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.ResultadoTranscricao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TranscricaoResponseTest {

    @Test
    void levaOTextoAsLinhasEOsIlegiveisSemExporModeloNemTokens() {
        var resultado = new ResultadoTranscricao(
                List.of("A educação no Brasil", "", "enfrenta [ilegível] desafios."), "gemini-teste", 1800, 400
        );

        var resposta = TranscricaoResponse.de(resultado);

        assertThat(resposta.texto()).isEqualTo("A educação no Brasil\n\nenfrenta [ilegível] desafios.");
        assertThat(resposta.linhas()).hasSize(3);
        assertThat(resposta.linhasComTrechoIlegivel()).containsExactly(3);
        assertThat(resposta.qtdLinhas()).isEqualTo(2);
        assertThat(TranscricaoResponse.class.getRecordComponents())
                .extracting(c -> c.getName())
                .containsExactly("texto", "linhas", "linhasComTrechoIlegivel", "qtdLinhas");
    }
}
