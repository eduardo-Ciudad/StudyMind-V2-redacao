package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.exception.ImagemNaoReconhecidaException;
import eduar.studymindredacao.domain.exception.TranscricaoIAException;
import eduar.studymindredacao.domain.model.ImagemRedacao;
import eduar.studymindredacao.domain.model.ResultadoTranscricao;

import java.util.List;

public interface TranscricaoIAPort {

    ResultadoTranscricao transcrever(List<ImagemRedacao> imagens);
}