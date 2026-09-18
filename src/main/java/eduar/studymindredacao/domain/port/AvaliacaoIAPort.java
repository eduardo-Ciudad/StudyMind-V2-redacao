package eduar.studymindredacao.domain.port;
import eduar.studymindredacao.domain.model.ResultadoAvaliacaoIA;
import eduar.studymindredacao.domain.model.SolicitacaoAvaliacao;

public interface AvaliacaoIAPort {

    ResultadoAvaliacaoIA avaliar(SolicitacaoAvaliacao solicitacao);
}
