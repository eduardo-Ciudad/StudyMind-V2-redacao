package eduar.studymindredacao.domain.model.enums;

/** Resultado da checagem das fontes de um repertório na pesquisa. */
public enum StatusVerificacao {
    /** Fontes conferidas. */
    VERIFIED,
    /** Conferido em parte (ex.: REP-001 a 024, que só existem resumidos). */
    PARTIAL,
    /** Reprovado na checagem. Nunca pode ficar ativo. */
    REJECTED
}
