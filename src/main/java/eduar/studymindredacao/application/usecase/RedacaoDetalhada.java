package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.model.Redacao;
import eduar.studymindredacao.domain.model.Tema;

/** Redação com o tema e a avaliação (null enquanto não avaliada ou se deu erro). */
public record RedacaoDetalhada(Redacao redacao, Tema tema, Avaliacao avaliacao) {
}
