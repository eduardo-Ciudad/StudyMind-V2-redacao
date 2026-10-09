package eduar.studymindredacao.domain.model.enums;

/**
 * O que um repertório é: uma pessoa, um conceito, uma obra, uma norma, um dado...
 * Os valores seguem a pesquisa V3 (docs/repertorio/curadoria-v3.json) e o CHECK da tabela repertorios.
 * Os rótulos em português ficam no frontend.
 */
public enum TipoEntidade {
    /** Pensador, pesquisador ou especialista (ex.: Rita Barradas Barata). */
    PERSON,
    /** Conceito teórico (ex.: racismo estrutural). */
    CONCEPT,
    /** Filme, livro, música ou outra obra (ex.: Quarto de Despejo). */
    CULTURAL_WORK,
    /** Constituição, lei ou norma (ex.: CF, art. 227). */
    LEGAL_SOURCE,
    /** Dado, pesquisa ou estudo (ex.: PNAD Contínua). */
    EVIDENCE,
    /** Órgão ou organização. Previsto na pesquisa, ainda sem registros. */
    INSTITUTION,
    /** Fato histórico ou acontecimento. Previsto na pesquisa, ainda sem registros. */
    EVENT
}
