package eduar.studymindredacao.domain.model.enums;

/**
 * Natureza de um dado ou estudo (registros com papel EVIDENCE).
 * Um registro pode combinar mais de um tipo (ex.: NATIONAL_SURVEY + OFFICIAL_STATISTICS).
 */
public enum TipoEvidencia {
    /** Estatística oficial (ex.: IBGE). */
    OFFICIAL_STATISTICS,
    /** Pesquisa nacional por amostragem (ex.: PNAD). */
    NATIONAL_SURVEY,
    /** Vigilância oficial em saúde (ex.: PeNSE, Vigitel). */
    OFFICIAL_HEALTH_SURVEILLANCE,
    /** Pesquisa acadêmica. */
    ACADEMIC_RESEARCH,
    /** Estudo científico publicado. */
    SCIENTIFIC_STUDY,
    /** Revisão de escopo. */
    SCOPING_REVIEW,
    /** Revisão sistemática. */
    SYSTEMATIC_REVIEW,
    /** Revisão integrativa. */
    INTEGRATIVE_REVIEW,
    /** Relatório institucional (ex.: Atlas da Violência). */
    INSTITUTIONAL_REPORT,
    /** Resumo ou síntese institucional. */
    INSTITUTIONAL_SUMMARY,
    /** Dado administrativo (registros de órgãos públicos). */
    ADMINISTRATIVE_DATA,
    /** Série histórica. */
    TIME_SERIES,
    /** Estudo transversal (retrato de um momento). */
    CROSS_SECTIONAL,
    /** Análise de conteúdo. */
    CONTENT_ANALYSIS
}
