package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Redacao;

/** Linha do histórico: a redação, o título do tema e a nota (null se não avaliada). */
public record RedacaoResumo(Redacao redacao, String temaTitulo, Short notaTotal) {
}
