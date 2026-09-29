-- 1) Resultado de anulação (nota zero) vindo da IA
ALTER TABLE avaliacoes
    ADD COLUMN anulada BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN motivo_anulacao TEXT;

-- 2) Pontos fortes/desenvolvimento passam a ser listas (JSON), não texto corrido
ALTER TABLE avaliacoes
    ALTER COLUMN pontos_fortes TYPE JSONB
        USING CASE WHEN pontos_fortes IS NULL THEN NULL ELSE jsonb_build_array(pontos_fortes) END,
    ALTER COLUMN pontos_desenvolvimento TYPE JSONB
        USING CASE WHEN pontos_desenvolvimento IS NULL THEN NULL ELSE jsonb_build_array(pontos_desenvolvimento) END;

-- 3) Detalhe por competência (base do estudo adaptativo)
CREATE TABLE avaliacao_competencias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    avaliacao_id UUID NOT NULL REFERENCES avaliacoes(id) ON DELETE CASCADE,
    numero SMALLINT NOT NULL CHECK (numero BETWEEN 1 AND 5),
    nota SMALLINT NOT NULL CHECK (nota IN (0, 40, 80, 120, 160, 200)),
    nivel_referencia TEXT,
    resumo TEXT NOT NULL,
    problemas JSONB NOT NULL DEFAULT '[]'::jsonb,
    CONSTRAINT uk_avaliacao_competencia UNIQUE (avaliacao_id, numero)
);

-- 4) Temas oficiais do ENEM (só o título; textos motivadores ficam para depois)
INSERT INTO temas (titulo, origem, ano) VALUES
    ('Desafios para a valorização da herança africana no Brasil', 'ENEM_OFICIAL', 2024),
    ('Desafios para o enfrentamento da invisibilidade do trabalho de cuidado realizado pela mulher no Brasil', 'ENEM_OFICIAL', 2023),
    ('Desafios para a valorização de comunidades e povos tradicionais no Brasil', 'ENEM_OFICIAL', 2022),
    ('Invisibilidade e registro civil: garantia de acesso à cidadania no Brasil', 'ENEM_OFICIAL', 2021),
    ('O estigma associado às doenças mentais na sociedade brasileira', 'ENEM_OFICIAL', 2020),
    ('Democratização do acesso ao cinema no Brasil', 'ENEM_OFICIAL', 2019),
    ('Manipulação do comportamento do usuário pelo controle de dados na internet', 'ENEM_OFICIAL', 2018),
    ('Desafios para a formação educacional de surdos no Brasil', 'ENEM_OFICIAL', 2017);