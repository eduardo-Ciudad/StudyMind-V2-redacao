-- Repertório (issue #2): taxonomia (macroeixos → problemas sociais), repertórios, fontes e vínculos.
-- Conteúdo entra depois, pelo seed (#5). A ligação com os temas (tema_problemas) fica para quando a #3 voltar.

-- Busca sem acento ("constituicao" encontra "Constituição")
CREATE EXTENSION IF NOT EXISTS unaccent;

-- 1) Taxonomia
CREATE TABLE macroeixos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(60) NOT NULL UNIQUE,
    nome VARCHAR(120) NOT NULL,
    ordem SMALLINT NOT NULL,
    CONSTRAINT ck_macroeixos_slug CHECK (slug ~ '^[a-z0-9]+(-[a-z0-9]+)*$'),
    CONSTRAINT ck_macroeixos_ordem CHECK (ordem > 0)
);

CREATE TABLE problemas_sociais (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    macroeixo_id UUID NOT NULL REFERENCES macroeixos(id),
    slug VARCHAR(60) NOT NULL UNIQUE,
    nome VARCHAR(160) NOT NULL,
    CONSTRAINT ck_problemas_slug CHECK (slug ~ '^[a-z0-9]+(-[a-z0-9]+)*$')
);
CREATE INDEX idx_problemas_macroeixo ON problemas_sociais(macroeixo_id);

-- 2) Repertórios
CREATE TABLE repertorios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(10) NOT NULL UNIQUE,
    tipo_entidade VARCHAR(20) NOT NULL,
    papel VARCHAR(20) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    subtitulo VARCHAR(255),
    tipo_descricao VARCHAR(120),
    area VARCHAR(255),
    pais VARCHAR(80),

    -- Conteúdo didático (opcional em registros inativos e em CF/EVD)
    ideia_central TEXT,
    lembre_na_prova TEXT,
    como_usar TEXT,
    exemplo_aplicacao TEXT,
    erro_comum TEXT,

    -- Curadoria: nula em CF/EVD; notas nulas até a pesquisa de prioridade
    risco_uso VARCHAR(10),
    dificuldade VARCHAR(15),
    saturacao VARCHAR(10),
    nota_versatilidade SMALLINT,
    nota_autoridade SMALLINT,
    nota_compreensao SMALLINT,
    nota_aplicabilidade SMALLINT,
    nota_especificidade SMALLINT,
    nota_originalidade SMALLINT,

    -- Listas em JSONB (mesmo padrão de temas_previsao)
    tipos_evidencia JSONB NOT NULL DEFAULT '[]'::jsonb,
    funcoes_argumentativas JSONB NOT NULL DEFAULT '[]'::jsonb,
    tipos_argumento JSONB NOT NULL DEFAULT '[]'::jsonb,
    tags JSONB NOT NULL DEFAULT '[]'::jsonb,

    -- Evidência
    populacao TEXT,
    ano_evidencia SMALLINT,

    status_verificacao VARCHAR(10) NOT NULL,
    verificado_em DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT ck_rep_codigo CHECK (codigo ~ '^(REP|EVD|CF)-[0-9]{3}$'),
    CONSTRAINT ck_rep_tipo CHECK (tipo_entidade IN ('PERSON', 'CONCEPT', 'CULTURAL_WORK', 'LEGAL_SOURCE', 'EVIDENCE', 'INSTITUTION', 'EVENT')),
    CONSTRAINT ck_rep_papel CHECK (papel IN ('REPERTOIRE', 'EVIDENCE', 'BOTH')),
    CONSTRAINT ck_rep_risco CHECK (risco_uso IN ('LOW', 'MEDIUM', 'HIGH')),
    CONSTRAINT ck_rep_saturacao CHECK (saturacao IN ('LOW', 'MEDIUM', 'HIGH')),
    CONSTRAINT ck_rep_dificuldade CHECK (dificuldade IN ('BASIC', 'INTERMEDIATE', 'ADVANCED')),
    CONSTRAINT ck_rep_verificacao CHECK (status_verificacao IN ('VERIFIED', 'PARTIAL', 'REJECTED')),
    -- Mesma regra do domínio: REJECTED nunca fica ativo
    CONSTRAINT ck_rep_rejeitado_inativo CHECK (NOT (ativo AND status_verificacao = 'REJECTED')),
    -- Notas: todas nulas ou todas preenchidas, de 1 a 5
    CONSTRAINT ck_rep_notas_completas CHECK (
        num_nulls(nota_versatilidade, nota_autoridade, nota_compreensao,
                  nota_aplicabilidade, nota_especificidade, nota_originalidade) IN (0, 6)
    ),
    CONSTRAINT ck_rep_notas_escala CHECK (
        nota_versatilidade BETWEEN 1 AND 5 AND nota_autoridade BETWEEN 1 AND 5 AND
        nota_compreensao BETWEEN 1 AND 5 AND nota_aplicabilidade BETWEEN 1 AND 5 AND
        nota_especificidade BETWEEN 1 AND 5 AND nota_originalidade BETWEEN 1 AND 5
    ),
    CONSTRAINT ck_rep_ano CHECK (ano_evidencia BETWEEN 1900 AND 2100),
    CONSTRAINT ck_rep_listas CHECK (
        jsonb_typeof(tipos_evidencia) = 'array' AND jsonb_typeof(funcoes_argumentativas) = 'array' AND
        jsonb_typeof(tipos_argumento) = 'array' AND jsonb_typeof(tags) = 'array'
    )
);
-- Listagem padrão: só ativos, filtrando por tipo
CREATE INDEX idx_rep_tipo ON repertorios(tipo_entidade) WHERE ativo;
-- Filtro por função argumentativa (funcoes_argumentativas @> '["EXPLAIN_CAUSE"]')
CREATE INDEX idx_rep_funcoes ON repertorios USING GIN (funcoes_argumentativas);

-- 3) Fontes de cada repertório
CREATE TABLE repertorio_fontes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    repertorio_id UUID NOT NULL REFERENCES repertorios(id) ON DELETE CASCADE,
    tipo VARCHAR(10) NOT NULL,
    descricao TEXT NOT NULL,
    url VARCHAR(500),
    CONSTRAINT ck_rep_fontes_tipo CHECK (tipo IN ('PROFILE', 'CONCEPT', 'GENERAL')),
    -- O front exibe a url como link: só http(s)
    CONSTRAINT ck_rep_fontes_url CHECK (url ~* '^https?://')
);
CREATE INDEX idx_rep_fontes_repertorio ON repertorio_fontes(repertorio_id);

-- 4) Problemas sociais que cada repertório ajuda a explicar
CREATE TABLE repertorio_problemas (
    repertorio_id UUID NOT NULL REFERENCES repertorios(id) ON DELETE CASCADE,
    problema_id UUID NOT NULL REFERENCES problemas_sociais(id),
    PRIMARY KEY (repertorio_id, problema_id)
);
-- Filtro por problema (a PK já cobre a busca por repertório)
CREATE INDEX idx_rep_problemas_problema ON repertorio_problemas(problema_id);