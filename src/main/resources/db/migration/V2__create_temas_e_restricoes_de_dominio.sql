-- Banco de temas
CREATE TABLE temas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(255) NOT NULL,
    textos_motivadores TEXT,
    origem VARCHAR(20) NOT NULL,
    ano SMALLINT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_temas_origem CHECK (origem IN ('ENEM_OFICIAL', 'AUTORAL'))
);

-- Toda redação passa a ter um tema (tabela deve estar vazia neste ponto do MVP)
ALTER TABLE redacoes ADD COLUMN tema_id UUID NOT NULL REFERENCES temas(id);
CREATE INDEX idx_redacoes_tema ON redacoes(tema_id);

-- Valores de enum
ALTER TABLE usuarios
    ADD CONSTRAINT ck_usuarios_role CHECK (role IN ('ALUNO', 'ADMIN'));

ALTER TABLE perfis_aluno
    ADD CONSTRAINT ck_perfis_nivel CHECK (nivel_experiencia IN ('INICIANTE', 'INTERMEDIARIO', 'AVANCADO')),
    ADD CONSTRAINT ck_perfis_meta_nota CHECK (meta_nota BETWEEN 0 AND 1000);

ALTER TABLE redacoes
    ADD CONSTRAINT ck_redacoes_tipo CHECK (tipo IN ('DIAGNOSTICA', 'PRATICA')),
    ADD CONSTRAINT ck_redacoes_status CHECK (status IN ('ENVIADA', 'EM_AVALIACAO', 'AVALIADA', 'ERRO'));

-- Notas do ENEM: cada competência só aceita os 6 níveis oficiais, e o total é a soma
ALTER TABLE avaliacoes
    ADD CONSTRAINT ck_avaliacoes_niveis CHECK (
        nota_c1 IN (0, 40, 80, 120, 160, 200) AND
        nota_c2 IN (0, 40, 80, 120, 160, 200) AND
        nota_c3 IN (0, 40, 80, 120, 160, 200) AND
        nota_c4 IN (0, 40, 80, 120, 160, 200) AND
        nota_c5 IN (0, 40, 80, 120, 160, 200)
    ),
    ADD CONSTRAINT ck_avaliacoes_total CHECK (
        nota_total = nota_c1 + nota_c2 + nota_c3 + nota_c4 + nota_c5
    );