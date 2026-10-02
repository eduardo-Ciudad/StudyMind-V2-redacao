package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.PrevisaoTema;

import java.util.List;

public interface PrevisaoTemaRepositoryPort {
    /** Todas as previsões, em ordem de ranking. */
    List<PrevisaoTema> listarPorRanking();
}
