package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.ImagemRedacao;
import eduar.studymindredacao.domain.model.ResultadoTranscricao;
import eduar.studymindredacao.domain.port.TranscricaoIAPort;

import java.util.ArrayList;
import java.util.List;

/** IA de transcrição de mentira: devolve um resultado fixo ou lança a falha configurada. */
class TranscricaoIAFake implements TranscricaoIAPort {
    static final int TOKENS_ENTRADA = 1800;
    static final int TOKENS_SAIDA = 400;

    private final List<List<ImagemRedacao>> chamadas = new ArrayList<>();
    private RuntimeException falha;

    @Override
    public ResultadoTranscricao transcrever(List<ImagemRedacao> imagens) {
        chamadas.add(List.copyOf(imagens));
        if (falha != null) {
            throw falha;
        }
        return new ResultadoTranscricao(
                List.of("A educação no Brasil", "enfrenta desafios."), "gemini-teste", TOKENS_ENTRADA, TOKENS_SAIDA
        );
    }

    void falharCom(RuntimeException falha) {
        this.falha = falha;
    }

    List<List<ImagemRedacao>> chamadas() {
        return chamadas;
    }
}
