-- Repertório (issue #5, lote 2): 12 artigos da Constituição Federal (CF-001 a CF-012).
-- Fonte: docs/repertorio/curadoria-v3.json.
-- Ajuste de conteúdo (out/2026): na pesquisa, o campo "erro comum" dos CF misturava alertas e dicas de uso.
-- As dicas (CF-002, 004, 007, 008, 009, 010) foram para como_usar; os alertas (CF-001, 003, 005, 006)
-- ficaram em erro_comum. O JSON foi corrigido no mesmo commit.
-- Upsert por código; fontes e vínculos dos CF são apagados e reinseridos para refletir exatamente este arquivo.

-- 1) Repertórios
WITH dados (codigo, nome, ideia_central, como_usar, erro_comum, tags) AS (
    VALUES
    ('CF-001', 'Constituição Federal, Art. 1º',
     'Dignidade da pessoa humana entre os fundamentos da República',
     NULL,
     'Fundamenta a centralidade da pessoa, mas não substitui norma específica.',
     '["dignidade", "invisibilidade", "violência", "discriminação"]'::jsonb),
    ('CF-002', 'Constituição Federal, Art. 3º',
     'Objetivos fundamentais incluem reduzir desigualdades e promover o bem de todos sem discriminação',
     'Bom para mostrar a distância entre a lei e a realidade e o dever do Estado de reduzir desigualdades.',
     NULL,
     '["desigualdade", "racismo", "gênero", "território"]'::jsonb),
    ('CF-003', 'Constituição Federal, Art. 5º',
     'Igualdade e direitos fundamentais',
     NULL,
     'Evitar dizer apenas ''todos são iguais''; explicar a violação concreta.',
     '["discriminação", "liberdade", "segurança", "privacidade"]'::jsonb),
    ('CF-004', 'Constituição Federal, Art. 6º',
     'Direitos sociais',
     'Excelente para mostrar distância entre direito social e acesso efetivo.',
     NULL,
     '["educação", "saúde", "alimentação", "trabalho", "moradia", "transporte"]'::jsonb),
    ('CF-005', 'Constituição Federal, Art. 7º',
     'Direitos dos trabalhadores',
     NULL,
     'Aplicar com cuidado a relações de plataforma conforme enquadramento jurídico.',
     '["precarização", "jornada", "proteção do trabalho"]'::jsonb),
    ('CF-006', 'Constituição Federal, Art. 196',
     'Saúde como direito de todos e dever do Estado',
     NULL,
     'Não implica que toda prestação de saúde seja ilimitada ou automática.',
     '["SUS", "acesso", "prevenção", "desigualdade em saúde"]'::jsonb),
    ('CF-007', 'Constituição Federal, Art. 205',
     'Educação como direito de todos e dever do Estado e da família',
     'Conectar direito à educação a acesso, permanência e desenvolvimento.',
     NULL,
     '["evasão", "alfabetização", "inclusão", "EJA"]'::jsonb),
    ('CF-008', 'Constituição Federal, Art. 215',
     'Direitos culturais e acesso às fontes da cultura nacional',
     'Útil para acesso e valorização cultural.',
     NULL,
     '["patrimônio", "cultura afro-brasileira", "povos tradicionais"]'::jsonb),
    ('CF-009', 'Constituição Federal, Art. 225',
     'Direito ao meio ambiente ecologicamente equilibrado',
     'Conectar ambiente a qualidade de vida e deveres coletivos/estatais.',
     NULL,
     '["clima", "resíduos", "saneamento", "justiça ambiental"]'::jsonb),
    ('CF-010', 'Constituição Federal, Art. 227',
     'Dever de assegurar com absoluta prioridade direitos de crianças e adolescentes',
     'Um dos dispositivos mais fortes para proteção integral da infância.',
     NULL,
     '["violência", "trabalho infantil", "exploração online", "educação"]'::jsonb),
    ('CF-011', 'Constituição Federal, Art. 231',
     'Reconhece aos povos indígenas organização social, costumes, línguas, crenças, tradições e direitos originários sobre terras tradicionalmente ocupadas.',
     NULL,
     NULL,
     '["território", "cultura", "identidade", "meio ambiente", "autodeterminação"]'::jsonb),
    ('CF-012', 'Constituição Federal, Art. 232',
     'Reconhece legitimidade de indígenas, comunidades e organizações para ingressar em juízo na defesa de direitos e interesses.',
     NULL,
     NULL,
     '["acesso à justiça", "direitos coletivos", "participação"]'::jsonb)
)
INSERT INTO repertorios (
    codigo, tipo_entidade, papel, nome, tipo_descricao, area, pais,
    ideia_central, como_usar, erro_comum,
    funcoes_argumentativas, tags,
    status_verificacao, verificado_em, ativo
)
SELECT
    codigo, 'LEGAL_SOURCE', 'REPERTOIRE', nome, 'CONSTITUIÇÃO FEDERAL', 'Direito constitucional', 'Brasil',
    ideia_central, como_usar, erro_comum,
    '["LEGAL_GAP"]'::jsonb, tags,
    'VERIFIED', DATE '2026-10-07', TRUE
FROM dados
ON CONFLICT (codigo) DO UPDATE
    SET nome = EXCLUDED.nome,
        tipo_descricao = EXCLUDED.tipo_descricao,
        area = EXCLUDED.area,
        pais = EXCLUDED.pais,
        ideia_central = EXCLUDED.ideia_central,
        como_usar = EXCLUDED.como_usar,
        erro_comum = EXCLUDED.erro_comum,
        funcoes_argumentativas = EXCLUDED.funcoes_argumentativas,
        tags = EXCLUDED.tags,
        status_verificacao = EXCLUDED.status_verificacao,
        verificado_em = EXCLUDED.verificado_em,
        ativo = EXCLUDED.ativo,
        atualizado_em = now();

-- 2) Fontes: todos os CF citam a mesma fonte (texto da Constituição no Planalto)
DELETE FROM repertorio_fontes
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo LIKE 'CF-%');

INSERT INTO repertorio_fontes (repertorio_id, tipo, descricao, url)
SELECT id, 'GENERAL',
       'Constituição da República Federativa do Brasil de 1988 — Portal Planalto',
       'https://www.planalto.gov.br/ccivil_03/constituicao/constituicao.htm'
FROM repertorios
WHERE codigo LIKE 'CF-%';

-- 3) Problemas sociais de cada artigo
DELETE FROM repertorio_problemas
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo LIKE 'CF-%');

WITH vinculos (codigo, problema_slug) AS (
    VALUES
    ('CF-001', 'trabalhos-invisibilizados'), ('CF-001', 'discriminacao'),
    ('CF-002', 'racismo'), ('CF-002', 'sexismo'),
    ('CF-003', 'discriminacao'), ('CF-003', 'violencia-urbana'), ('CF-003', 'dados-privacidade'),
    ('CF-004', 'prevencao-saude'), ('CF-004', 'moradia'), ('CF-004', 'segregacao-urbana'),
    ('CF-005', 'precarizacao-trabalho'),
    ('CF-006', 'sus-acesso-saude'), ('CF-006', 'prevencao-saude'), ('CF-006', 'desigualdade-saude'),
    ('CF-007', 'evasao-escolar'), ('CF-007', 'alfabetizacao'), ('CF-007', 'inclusao-educacional'), ('CF-007', 'eja'),
    ('CF-008', 'identidade'), ('CF-008', 'racismo'), ('CF-008', 'direitos-indigenas'),
    ('CF-009', 'justica-climatica'), ('CF-009', 'meio-ambiente'),
    ('CF-010', 'trabalho-infantil'), ('CF-010', 'infancia-digital'), ('CF-010', 'protecao-integral'),
    ('CF-011', 'territorio-indigena'), ('CF-011', 'direitos-indigenas'), ('CF-011', 'cultura-memoria-indigena'),
    ('CF-011', 'identidade'), ('CF-011', 'meio-ambiente'),
    ('CF-012', 'direitos-indigenas'), ('CF-012', 'participacao-juventude')
)
INSERT INTO repertorio_problemas (repertorio_id, problema_id)
SELECT r.id, p.id
FROM vinculos v
JOIN repertorios r ON r.codigo = v.codigo
JOIN problemas_sociais p ON p.slug = v.problema_slug;