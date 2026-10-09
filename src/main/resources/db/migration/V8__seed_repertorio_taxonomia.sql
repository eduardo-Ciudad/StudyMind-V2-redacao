-- Repertório (issue #5, lote 1): taxonomia da pesquisa V3 — 13 macroeixos e 52 problemas sociais.
-- Fonte: docs/repertorio/curadoria-v3.json. "Povos indígenas" e "meio-ambiente" foram acrescentados
-- na conversão da pesquisa (aprovados). Upsert por slug: numa versão nova da pesquisa, basta outra migration.

-- 1) Macroeixos
INSERT INTO macroeixos (slug, nome, ordem) VALUES
    ('saude',                 'Saúde',                                      1),
    ('educacao',              'Educação',                                   2),
    ('infancia-adolescencia', 'Infância e adolescência',                    3),
    ('juventude',             'Juventude',                                  4),
    ('tecnologia-sociedade',  'Tecnologia e sociedade',                     5),
    ('trabalho-cuidado',      'Trabalho e cuidado',                         6),
    ('pcd-neurodiversidade',  'PcD e neurodiversidade',                     7),
    ('invisibilidade-social', 'Invisibilidade social',                      8),
    ('raca-genero',           'Raça e gênero',                              9),
    ('cidades-clima',         'Cidades e clima',                           10),
    ('envelhecimento',        'Envelhecimento',                            11),
    ('violencia-seguranca',   'Violência e segurança',                     12),
    ('povos-indigenas',       'Povos indígenas e comunidades tradicionais', 13)
ON CONFLICT (slug) DO UPDATE
    SET nome = EXCLUDED.nome,
        ordem = EXCLUDED.ordem;

-- 2) Problemas sociais (ligados ao macroeixo pelo slug)
WITH dados (macroeixo_slug, slug, nome) AS (
    VALUES
    -- Saúde
    ('saude', 'desigualdade-saude', 'Desigualdade em saúde'),
    ('saude', 'sus-acesso-saude',   'SUS e acesso à saúde'),
    ('saude', 'saude-mental',       'Saúde mental'),
    ('saude', 'prevencao-saude',    'Prevenção em saúde'),
    -- Educação
    ('educacao', 'alfabetizacao',           'Alfabetização e letramento'),
    ('educacao', 'eja',                     'EJA e educação de adultos'),
    ('educacao', 'evasao-escolar',          'Evasão e permanência escolar'),
    ('educacao', 'inclusao-educacional',    'Inclusão e desigualdade educacional'),
    ('educacao', 'neurodiversidade-escola', 'Neurodiversidade na escola'),
    -- Infância e adolescência
    ('infancia-adolescencia', 'protecao-integral', 'Proteção integral da infância'),
    ('infancia-adolescencia', 'infancia-digital',  'Infância no ambiente digital'),
    ('infancia-adolescencia', 'trabalho-infantil', 'Trabalho infantil'),
    -- Juventude
    ('juventude', 'violencia-juventude',    'Violência contra a juventude'),
    ('juventude', 'trabalho-juventude',     'Juventude e trabalho'),
    ('juventude', 'participacao-juventude', 'Participação juvenil'),
    ('juventude', 'perspectiva-futuro',     'Perspectiva de futuro'),
    -- Tecnologia e sociedade
    ('tecnologia-sociedade', 'plataformas-digitais', 'Plataformas digitais'),
    ('tecnologia-sociedade', 'dados-privacidade',    'Dados e privacidade'),
    ('tecnologia-sociedade', 'algoritmos',           'Algoritmos e IA'),
    ('tecnologia-sociedade', 'inclusao-digital',     'Inclusão digital'),
    -- Trabalho e cuidado
    ('trabalho-cuidado', 'precarizacao-trabalho',    'Precarização do trabalho'),
    ('trabalho-cuidado', 'plataformizacao-trabalho', 'Plataformização do trabalho'),
    ('trabalho-cuidado', 'trabalho-domestico',       'Trabalho doméstico'),
    ('trabalho-cuidado', 'trabalho-cuidado',         'Trabalho de cuidado'),
    -- PcD e neurodiversidade
    ('pcd-neurodiversidade', 'acessibilidade',       'Acessibilidade'),
    ('pcd-neurodiversidade', 'inclusao-escolar-pcd', 'Inclusão escolar de PcD'),
    ('pcd-neurodiversidade', 'trabalho-pcd',         'PcD e trabalho'),
    -- Invisibilidade social
    ('invisibilidade-social', 'populacao-rua',             'População em situação de rua'),
    ('invisibilidade-social', 'trabalhos-invisibilizados', 'Trabalhos invisibilizados'),
    ('invisibilidade-social', 'pobreza',                   'Pobreza'),
    -- Raça e gênero
    ('raca-genero', 'racismo',       'Racismo'),
    ('raca-genero', 'sexismo',       'Sexismo e desigualdade de gênero'),
    ('raca-genero', 'identidade',    'Identidade e representação'),
    ('raca-genero', 'discriminacao', 'Discriminação'),
    -- Cidades e clima
    ('cidades-clima', 'moradia',           'Moradia'),
    ('cidades-clima', 'segregacao-urbana', 'Segregação urbana e direito à cidade'),
    ('cidades-clima', 'seca',              'Seca e deslocamento'),
    ('cidades-clima', 'calor-extremo',     'Calor extremo e eventos climáticos'),
    ('cidades-clima', 'justica-climatica', 'Justiça climática e ambiental'),
    ('cidades-clima', 'meio-ambiente',     'Meio ambiente, resíduos e saneamento'),
    -- Envelhecimento
    ('envelhecimento', 'etarismo',               'Etarismo'),
    ('envelhecimento', 'cuidado-pessoa-idosa',   'Cuidado da pessoa idosa'),
    ('envelhecimento', 'autonomia-pessoa-idosa', 'Autonomia da pessoa idosa'),
    ('envelhecimento', 'inclusao-pessoa-idosa',  'Inclusão da pessoa idosa'),
    -- Violência e segurança
    ('violencia-seguranca', 'violencia-urbana',        'Violência urbana'),
    ('violencia-seguranca', 'violencia-racial',        'Violência racial'),
    ('violencia-seguranca', 'violencia-institucional', 'Violência institucional'),
    ('violencia-seguranca', 'violencia-escolar',       'Violência escolar'),
    -- Povos indígenas e comunidades tradicionais
    ('povos-indigenas', 'territorio-indigena',      'Território indígena'),
    ('povos-indigenas', 'cultura-memoria-indigena', 'Cultura e memória indígena'),
    ('povos-indigenas', 'direitos-indigenas',       'Direitos indígenas'),
    ('povos-indigenas', 'saude-indigena',           'Saúde indígena')
)
INSERT INTO problemas_sociais (macroeixo_id, slug, nome)
SELECT m.id, d.slug, d.nome
FROM dados d
JOIN macroeixos m ON m.slug = d.macroeixo_slug
ON CONFLICT (slug) DO UPDATE
    SET nome = EXCLUDED.nome,
        macroeixo_id = EXCLUDED.macroeixo_id;