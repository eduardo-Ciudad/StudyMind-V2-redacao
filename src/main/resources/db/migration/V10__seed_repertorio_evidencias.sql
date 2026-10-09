-- Repertório (issue #5, lote 3): 13 evidências (EVD-001 a EVD-013).
-- Fonte: docs/repertorio/curadoria-v3.json.
-- Ajustes de conteúdo (out/2026), refletidos no JSON no mesmo commit:
--   * tipo_descricao traduzido (a pesquisa trazia os códigos dos enums, ex.: OFFICIAL_STATISTICS / NATIONAL_SURVEY);
--   * EVD-001 e EVD-003: a frase de "erro comum" era instrução de uso -> como_usar
--     (em EVD-001, "atualizar anualmente" saiu: é recado de curadoria, não do aluno).
-- EVD-006 a 010 estão marcados como "precisa atualizar" na pesquisa, mas entram ativos (dados de 2023 a 2026).
-- EVD-006 a 013 não têm URL na pesquisa: a fonte fica só com a descrição.

-- 1) Repertórios
WITH dados (codigo, nome, tipo_descricao, ideia_central, como_usar, erro_comum, tipos_evidencia, populacao, ano_evidencia, tags) AS (
    VALUES
    ('EVD-001', 'PNAD Contínua Educação 2025', 'ESTATÍSTICA OFICIAL / PESQUISA NACIONAL',
     'A taxa de analfabetismo das pessoas de 15 anos ou mais foi 4,9% em 2025, ante 5,3% em 2024.',
     'Use como evidência temporal.',
     NULL,
     '["OFFICIAL_STATISTICS", "NATIONAL_SURVEY"]'::jsonb, NULL, 2025,
     '["Analfabetismo", "desigualdade regional", "EJA"]'::jsonb),
    ('EVD-002', 'Revisão de escopo: racismo como determinante social da saúde', 'REVISÃO DE ESCOPO',
     'A revisão identifica mecanismos pelos quais racismo estrutural, institucional, vicário e antinegro afetam condições de saúde no Brasil.',
     NULL,
     'Não converter a revisão em causalidade individual automática.',
     '["SCOPING_REVIEW"]'::jsonb, NULL, 2026,
     '["Saúde da população negra", "racismo", "acesso à saúde"]'::jsonb),
    ('EVD-003', 'Vigitel', 'PESQUISA NACIONAL / VIGILÂNCIA EM SAÚDE',
     'Monitora fatores de risco e proteção para DCNT, incluindo tabagismo, alimentação, atividade física, excesso de peso, sono e outros indicadores.',
     'Sempre indicar ano/edição do dado específico usado.',
     NULL,
     '["NATIONAL_SURVEY", "OFFICIAL_HEALTH_SURVEILLANCE"]'::jsonb, NULL, NULL,
     '["Saúde preventiva", "DCNT", "políticas públicas"]'::jsonb),
    ('EVD-004', 'PNAD Trabalho de Crianças e Adolescentes', 'ESTATÍSTICA OFICIAL / PESQUISA NACIONAL',
     'Em 2023, 1,607 milhão de crianças e adolescentes de 5 a 17 anos estavam em situação de trabalho infantil, menor contingente da série iniciada em 2016.',
     NULL,
     'O recuo não significa erradicação; contextualizar faixa etária e definição.',
     '["OFFICIAL_STATISTICS", "NATIONAL_SURVEY"]'::jsonb, NULL, 2023,
     '["Trabalho infantil", "infância", "pobreza"]'::jsonb),
    ('EVD-005', 'Desigualdades de saúde por raça/cor', 'PESQUISA ACADÊMICA',
     'Estudo com dados da PNAD encontrou diferenças raciais no estado de saúde e na frequência de consultas, mesmo após ajustes socioeconômicos em parte das análises.',
     NULL,
     'Estudo usa PNAD 1998; bom para mecanismo histórico, não como dado atual.',
     '["ACADEMIC_RESEARCH"]'::jsonb, NULL, 2007,
     '["Saúde", "raça", "desigualdade de acesso"]'::jsonb),
    ('EVD-006', 'PeNSE 2024 — saúde mental de adolescentes', 'PESQUISA NACIONAL / ESTATÍSTICA OFICIAL',
     'A quinta edição incorpora módulo de saúde mental e permite analisar amizade, ansiedade, tristeza, autoagressão e outros indicadores por sexo/idade.',
     NULL,
     'Usar tabelas específicas e não converter indicadores autorreferidos em diagnóstico clínico.',
     '["NATIONAL_SURVEY", "OFFICIAL_STATISTICS"]'::jsonb, 'Escolares de 13 a 17 anos, amostra nacional', 2024,
     '["saúde mental juvenil"]'::jsonb),
    ('EVD-007', 'Atlas da Violência 2025', 'RELATÓRIO INSTITUCIONAL / ESTATÍSTICA OFICIAL',
     'Em 2023, uma pessoa negra tinha 2,7 vezes mais chance de ser vítima de homicídio do que uma pessoa não negra, segundo o Atlas.',
     NULL,
     'Indicador é risco relativo em violência letal; não generalizar para toda forma de violência.',
     '["INSTITUTIONAL_REPORT", "OFFICIAL_STATISTICS"]'::jsonb, 'População brasileira; homicídios e recortes sociodemográficos', 2023,
     '["violência racial", "juventude", "segurança"]'::jsonb),
    ('EVD-008', 'OBPopRua/UFMG — população em situação de rua 2025', 'RELATÓRIO INSTITUCIONAL / DADOS ADMINISTRATIVOS',
     'O observatório reportou crescimento de cerca de 328 mil pessoas em dezembro de 2024 para mais de 365 mil em dezembro de 2025; cerca de 70% eram negras.',
     NULL,
     'CadÚnico não é censo completo; há subregistro e diferenças metodológicas.',
     '["INSTITUTIONAL_REPORT", "ADMINISTRATIVE_DATA"]'::jsonb, 'Registros de pessoas em situação de rua no CadÚnico', 2025,
     '["população em situação de rua", "raça", "políticas públicas"]'::jsonb),
    ('EVD-009', 'Ondas de calor extremo e mortalidade cardiovascular no Sudeste', 'ESTUDO CIENTÍFICO / SÉRIE HISTÓRICA',
     'Estudo de séries temporais estimou excesso de mortalidade cardiovascular associado a ondas de calor extremo.',
     NULL,
     'Associação populacional; não atribuir causa individual específica.',
     '["SCIENTIFIC_STUDY", "TIME_SERIES"]'::jsonb, 'Mortalidade cardiovascular agregada no Sudeste, 2014-2023', 2026,
     '["calor extremo", "saúde", "clima"]'::jsonb),
    ('EVD-010', 'Desastres hidrológicos e desigualdades em saúde', 'ESTUDO CIENTÍFICO / SÍNTESE INSTITUCIONAL',
     'Impactos humanos e danos à infraestrutura de saúde não se distribuíram uniformemente, com maior vulnerabilidade indicada em municípios pequenos e na região Norte.',
     NULL,
     'Índices e recortes devem ser apresentados conforme o estudo original.',
     '["SCIENTIFIC_STUDY", "INSTITUTIONAL_SUMMARY"]'::jsonb, 'Desastres hidrológicos no Brasil, 2000-2023', 2026,
     '["enchentes", "vulnerabilidade territorial", "saúde"]'::jsonb),
    ('EVD-011', 'Revisão sobre assistência à saúde indígena no Brasil', 'REVISÃO INTEGRATIVA',
     'Revisão identificou desafios e iniquidades persistentes na assistência à saúde indígena e necessidade de respostas adaptadas às especificidades socioculturais.',
     NULL,
     'Revisão incluiu número limitado de estudos elegíveis; não generalizar todas as realidades indígenas.',
     '["INTEGRATIVE_REVIEW"]'::jsonb, 'Artigos sobre assistência à saúde indígena no Brasil, 2013-2023', 2025,
     '["saúde indígena", "acesso", "equidade"]'::jsonb),
    ('EVD-012', 'Publicidade de alimentos em canais infantis no YouTube', 'ESTUDO CIENTÍFICO / ANÁLISE DE CONTEÚDO',
     'Publicidade geral apareceu em 45,6% dos vídeos analisados; publicidade de alimentos foi majoritariamente de ultraprocessados.',
     NULL,
     'Recorte de canais e período específicos; não é estimativa de toda exposição infantil à internet.',
     '["SCIENTIFIC_STUDY", "CONTENT_ANALYSIS"]'::jsonb, '250 vídeos de 25 canais infantis mais assistidos no Brasil, universo de 2018', 2023,
     '["publicidade infantil", "plataformas", "alimentação"]'::jsonb),
    ('EVD-013', 'Uso de serviços de saúde por população em situação de rua em Belo Horizonte', 'ESTUDO CIENTÍFICO / ESTUDO TRANSVERSAL',
     'Estudo analisou associação entre condições sociodemográficas, econômicas, de vida e utilização de serviços de saúde.',
     NULL,
     'Amostra local; não generalizar prevalências para todo o Brasil.',
     '["SCIENTIFIC_STUDY", "CROSS_SECTIONAL"]'::jsonb, '390 pessoas em situação de rua na região central de Belo Horizonte', 2024,
     '["rua", "saúde", "barreiras de acesso"]'::jsonb)
)
INSERT INTO repertorios (
    codigo, tipo_entidade, papel, nome, tipo_descricao, pais,
    ideia_central, como_usar, erro_comum,
    tipos_evidencia, funcoes_argumentativas, tags,
    populacao, ano_evidencia,
    status_verificacao, verificado_em, ativo
)
SELECT
    codigo, 'EVIDENCE', 'EVIDENCE', nome, tipo_descricao, 'Brasil',
    ideia_central, como_usar, erro_comum,
    tipos_evidencia, '["PROVE_PROBLEM"]'::jsonb, tags,
    populacao, ano_evidencia,
    'VERIFIED', DATE '2026-10-07', TRUE
FROM dados
ON CONFLICT (codigo) DO UPDATE
    SET nome = EXCLUDED.nome,
        tipo_descricao = EXCLUDED.tipo_descricao,
        pais = EXCLUDED.pais,
        ideia_central = EXCLUDED.ideia_central,
        como_usar = EXCLUDED.como_usar,
        erro_comum = EXCLUDED.erro_comum,
        tipos_evidencia = EXCLUDED.tipos_evidencia,
        funcoes_argumentativas = EXCLUDED.funcoes_argumentativas,
        tags = EXCLUDED.tags,
        populacao = EXCLUDED.populacao,
        ano_evidencia = EXCLUDED.ano_evidencia,
        status_verificacao = EXCLUDED.status_verificacao,
        verificado_em = EXCLUDED.verificado_em,
        ativo = EXCLUDED.ativo,
        atualizado_em = now();

-- 2) Fontes (EVD-006 a 013 sem URL)
DELETE FROM repertorio_fontes
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo LIKE 'EVD-%');

WITH fontes (codigo, descricao, url) AS (
    VALUES
    ('EVD-001', 'IBGE', 'https://www.ibge.gov.br/indicadores/indicadores-conjunturais/pnad-c.html'),
    ('EVD-002', 'Fiocruz Minas / Ciência & Saúde Coletiva', 'https://www.scielo.br/j/csc/a/3zd6rgkP7RxMnYzPvhp3t3v/'),
    ('EVD-003', 'Ministério da Saúde', 'https://www.gov.br/saude/pt-br/composicao/svsa/inqueritos-de-saude/vigitel'),
    ('EVD-004', 'IBGE', 'https://www.gov.br/mds/pt-br/noticias-e-conteudos/desenvolvimento-social/noticias-desenvolvimento-social/trabalho-infantil-recua-ao-menor-nivel-desde-2016-aponta-ibge'),
    ('EVD-005', 'Rita Barradas Barata et al. / Cadernos de Saúde Pública', 'https://cadernos.ensp.fiocruz.br/ojs/index.php/csp/article/view/3145'),
    ('EVD-006', 'IBGE + Ministério da Saúde', NULL),
    ('EVD-007', 'Ipea + FBSP', NULL),
    ('EVD-008', 'OBPopRua/Polos de Cidadania-UFMG', NULL),
    ('EVD-009', 'Cadernos de Saúde Pública', NULL),
    ('EVD-010', 'Cidacs/Fiocruz Bahia', NULL),
    ('EVD-011', 'Saúde e Sociedade / SciELO', NULL),
    ('EVD-012', 'Revista de Saúde Pública', NULL),
    ('EVD-013', 'UFMG', NULL)
)
INSERT INTO repertorio_fontes (repertorio_id, tipo, descricao, url)
SELECT r.id, 'GENERAL', f.descricao, f.url
FROM fontes f
JOIN repertorios r ON r.codigo = f.codigo;

-- 3) Problemas sociais de cada evidência
DELETE FROM repertorio_problemas
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo LIKE 'EVD-%');

WITH vinculos (codigo, problema_slug) AS (
    VALUES
    ('EVD-001', 'alfabetizacao'), ('EVD-001', 'eja'),
    ('EVD-002', 'desigualdade-saude'), ('EVD-002', 'racismo'), ('EVD-002', 'sus-acesso-saude'),
    ('EVD-003', 'prevencao-saude'),
    ('EVD-004', 'trabalho-infantil'), ('EVD-004', 'protecao-integral'), ('EVD-004', 'pobreza'),
    ('EVD-005', 'racismo'),
    ('EVD-006', 'saude-mental'),
    ('EVD-007', 'violencia-racial'), ('EVD-007', 'violencia-urbana'),
    ('EVD-008', 'populacao-rua'), ('EVD-008', 'racismo'),
    ('EVD-009', 'calor-extremo'), ('EVD-009', 'justica-climatica'),
    ('EVD-010', 'calor-extremo'), ('EVD-010', 'justica-climatica'),
    ('EVD-011', 'saude-indigena'),
    ('EVD-012', 'infancia-digital'), ('EVD-012', 'plataformas-digitais'), ('EVD-012', 'prevencao-saude'),
    ('EVD-013', 'populacao-rua'), ('EVD-013', 'sus-acesso-saude')
)
INSERT INTO repertorio_problemas (repertorio_id, problema_id)
SELECT r.id, p.id
FROM vinculos v
JOIN repertorios r ON r.codigo = v.codigo
JOIN problemas_sociais p ON p.slug = v.problema_slug;