-- REFERÊNCIA (issue #2) — rascunho usado só para testar o seed gerado.
-- A migration real é da issue #2; este arquivo mostra os nomes de coluna que o gerador espera.

CREATE EXTENSION IF NOT EXISTS unaccent;

CREATE TABLE macroeixos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(60) NOT NULL UNIQUE,
    nome VARCHAR(120) NOT NULL,
    ordem SMALLINT NOT NULL
);

CREATE TABLE problemas_sociais (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    macroeixo_id UUID NOT NULL REFERENCES macroeixos(id),
    slug VARCHAR(60) NOT NULL UNIQUE,
    nome VARCHAR(160) NOT NULL
);
CREATE INDEX idx_problemas_macroeixo ON problemas_sociais(macroeixo_id);

CREATE TABLE repertorios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(10) NOT NULL UNIQUE,
    tipo_entidade VARCHAR(20) NOT NULL,
    papel VARCHAR(20) NOT NULL,
    tipos_evidencia JSONB NOT NULL DEFAULT '[]'::jsonb,
    nome VARCHAR(255) NOT NULL,
    subtitulo VARCHAR(255),
    tipo_descricao VARCHAR(120),
    area VARCHAR(255),
    pais VARCHAR(80),
    ideia_central TEXT,
    lembre_na_prova TEXT,
    como_usar TEXT,
    exemplo_aplicacao TEXT,
    erro_comum TEXT,
    risco_uso VARCHAR(10),
    dificuldade VARCHAR(15),
    saturacao VARCHAR(10),
    nota_versatilidade SMALLINT,
    nota_autoridade SMALLINT,
    nota_compreensao SMALLINT,
    nota_aplicabilidade SMALLINT,
    nota_especificidade SMALLINT,
    nota_originalidade SMALLINT,
    funcoes_argumentativas JSONB NOT NULL DEFAULT '[]'::jsonb,
    tipos_argumento JSONB NOT NULL DEFAULT '[]'::jsonb,
    tags JSONB NOT NULL DEFAULT '[]'::jsonb,
    populacao TEXT,
    ano_evidencia SMALLINT,
    status_verificacao VARCHAR(10) NOT NULL,
    verificado_em DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_rep_tipo CHECK (tipo_entidade IN ('PERSON','CONCEPT','CULTURAL_WORK','LEGAL_SOURCE','EVIDENCE','INSTITUTION','EVENT')),
    CONSTRAINT ck_rep_papel CHECK (papel IN ('REPERTOIRE','EVIDENCE','BOTH')),
    CONSTRAINT ck_rep_risco CHECK (risco_uso IN ('LOW','MEDIUM','HIGH')),
    CONSTRAINT ck_rep_saturacao CHECK (saturacao IN ('LOW','MEDIUM','HIGH')),
    CONSTRAINT ck_rep_dificuldade CHECK (dificuldade IN ('BASIC','INTERMEDIATE','ADVANCED')),
    CONSTRAINT ck_rep_verificacao CHECK (status_verificacao IN ('VERIFIED','PARTIAL','REJECTED')),
    CONSTRAINT ck_rep_rejeitado_inativo CHECK (NOT (ativo AND status_verificacao = 'REJECTED')),
    CONSTRAINT ck_rep_notas CHECK (
        coalesce(nota_versatilidade, 1) BETWEEN 1 AND 5 AND coalesce(nota_autoridade, 1) BETWEEN 1 AND 5 AND
        coalesce(nota_compreensao, 1) BETWEEN 1 AND 5 AND coalesce(nota_aplicabilidade, 1) BETWEEN 1 AND 5 AND
        coalesce(nota_especificidade, 1) BETWEEN 1 AND 5 AND coalesce(nota_originalidade, 1) BETWEEN 1 AND 5)
);
CREATE INDEX idx_rep_tipo ON repertorios(tipo_entidade) WHERE ativo;
CREATE INDEX idx_rep_funcoes ON repertorios USING GIN (funcoes_argumentativas);

CREATE TABLE repertorio_fontes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    repertorio_id UUID NOT NULL REFERENCES repertorios(id) ON DELETE CASCADE,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('PROFILE','CONCEPT','GENERAL')),
    descricao TEXT NOT NULL,
    url VARCHAR(500)
);
CREATE INDEX idx_rep_fontes ON repertorio_fontes(repertorio_id);

CREATE TABLE repertorio_problemas (
    repertorio_id UUID NOT NULL REFERENCES repertorios(id) ON DELETE CASCADE,
    problema_id UUID NOT NULL REFERENCES problemas_sociais(id),
    PRIMARY KEY (repertorio_id, problema_id)
);
CREATE INDEX idx_rep_problemas_problema ON repertorio_problemas(problema_id);

CREATE TABLE tema_problemas (
    tema_id UUID NOT NULL REFERENCES temas(id) ON DELETE CASCADE,
    problema_id UUID NOT NULL REFERENCES problemas_sociais(id),
    PRIMARY KEY (tema_id, problema_id)
);
CREATE INDEX idx_tema_problemas_problema ON tema_problemas(problema_id);
