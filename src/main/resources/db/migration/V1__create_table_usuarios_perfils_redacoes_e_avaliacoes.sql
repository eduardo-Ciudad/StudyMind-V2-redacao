CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ALUNO',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE perfis_aluno (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL UNIQUE REFERENCES usuarios(id),
    meta_nota SMALLINT,
    nivel_experiencia VARCHAR(20),
    tempo_disponivel_semanal_min INT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE redacoes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    tipo VARCHAR(20) NOT NULL,           -- DIAGNOSTICA, PRATICA
    texto TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ENVIADA', -- ENVIADA, EM_AVALIACAO, AVALIADA, ERRO
    enviada_em TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_redacoes_usuario ON redacoes(usuario_id, enviada_em);

CREATE TABLE avaliacoes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    redacao_id UUID NOT NULL UNIQUE REFERENCES redacoes(id),
    nota_c1 SMALLINT NOT NULL CHECK (nota_c1 BETWEEN 0 AND 200),
    nota_c2 SMALLINT NOT NULL CHECK (nota_c2 BETWEEN 0 AND 200),
    nota_c3 SMALLINT NOT NULL CHECK (nota_c3 BETWEEN 0 AND 200),
    nota_c4 SMALLINT NOT NULL CHECK (nota_c4 BETWEEN 0 AND 200),
    nota_c5 SMALLINT NOT NULL CHECK (nota_c5 BETWEEN 0 AND 200),
    nota_total SMALLINT NOT NULL,
    pontos_fortes TEXT,
    pontos_desenvolvimento TEXT,
    diagnostico TEXT,
    modelo_ia VARCHAR(50),
    tokens_entrada INT,
    tokens_saida INT,
    resposta_bruta_json JSONB,
    avaliado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE uso_ia_diario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    data DATE NOT NULL,
    tokens_entrada INT NOT NULL DEFAULT 0,
    tokens_saida INT NOT NULL DEFAULT 0,
    qtd_correcoes INT NOT NULL DEFAULT 0,
    qtd_roadmaps INT NOT NULL DEFAULT 0,
    UNIQUE(usuario_id, data)
);