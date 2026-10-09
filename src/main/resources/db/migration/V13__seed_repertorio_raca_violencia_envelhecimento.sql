-- Repertório (issue #5, lote 6): 9 repertórios — Raça e gênero + Violência e segurança + Envelhecimento.
-- Fonte: docs/repertorio/curadoria-v3.json.
-- As notas da rubrica só servem para curadoria/ordenação (não vão na API); REP-034, REP-035, REP-041, REP-044, REP-046, REP-047, REP-040, REP-038 têm notas, os demais ficam nulos.
-- Upsert por código; fontes e vínculos destes códigos são apagados e reinseridos
-- (por lista de códigos, e não por LIKE 'REP-%', para não apagar os de outros lotes).

-- 1) Repertórios
INSERT INTO repertorios (
    codigo, tipo_entidade, papel, nome, subtitulo, tipo_descricao, area, pais,
    ideia_central, lembre_na_prova, como_usar, exemplo_aplicacao, erro_comum,
    risco_uso, dificuldade, saturacao,
    nota_versatilidade, nota_autoridade, nota_compreensao, nota_aplicabilidade, nota_especificidade, nota_originalidade,
    tipos_evidencia, funcoes_argumentativas, tipos_argumento, tags,
    status_verificacao, verificado_em, ativo
) VALUES
    -- Raça e gênero
    ('REP-034', 'PERSON', 'REPERTOIRE', 'Kabengele Munanga', NULL,
     'ANTROPÓLOGO / PESQUISADOR', 'Racismo, identidade negra e relações étnico-raciais', 'Brasil / RD Congo',
     'Estuda racismo, negritude, identidade negra, multiculturalismo e educação das relações étnico-raciais, discutindo como categorias raciais foram historicamente construídas e podem operar socialmente.',
     'Munanga ajuda a explicar que raça não tem fundamento biológico como hierarquia humana, mas continua produzindo efeitos sociais por meio do racismo, da identidade e das relações históricas.',
     'Use para diferenciar a inexistência de raças biológicas humanas da existência social do racismo.',
     'A produção de Kabengele Munanga permite compreender que, embora hierarquias raciais não possuam base biológica, a ideia de raça foi historicamente construída e continua produzindo consequências sociais concretas.',
     'Não dizer que o autor defende raça como categoria biológica.',
     'HIGH', 'INTERMEDIATE', 'MEDIUM',
     4, 5, 4, 5, 5, 4,
     '[]'::jsonb, '["HISTORICAL_CONTEXT", "DEFINE_CONCEPT", "SOCIAL_CRITIQUE"]'::jsonb,
     '["construção social da raça", "racismo", "reconhecimento identitário"]'::jsonb,
     '["racismo", "identidade", "educação antirracista", "ações afirmativas"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-035', 'PERSON', 'REPERTOIRE', 'Nilma Lino Gomes', NULL,
     'EDUCADORA / ANTROPÓLOGA', 'Educação, relações étnico-raciais e identidade', 'Brasil',
     'Pesquisa educação, diversidade, relações raciais, formação docente, políticas educacionais e movimento negro, mostrando como identidade e desigualdade racial também são produzidas e contestadas em espaços educativos.',
     'Nilma Lino Gomes conecta educação, identidade negra, formação docente, movimentos sociais e políticas de igualdade racial.',
     'Use quando a escola aparece não apenas como solução, mas também como espaço que pode reproduzir ou enfrentar desigualdades raciais.',
     'As pesquisas de Nilma Lino Gomes mostram que a educação participa da construção de identidades e pode tanto reproduzir discriminações quanto criar práticas de reconhecimento e valorização da diversidade racial.',
     'Não reduzir sua obra exclusivamente a cotas universitárias.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     5, 5, 4, 5, 5, 5,
     '[]'::jsonb, '["SHOW_INEQUALITY", "SOCIAL_CRITIQUE", "SUPPORT_INTERVENTION"]'::jsonb,
     '["racismo institucional", "invisibilidade curricular", "reconhecimento"]'::jsonb,
     '["racismo escolar", "identidade negra", "formação docente", "currículo", "ações afirmativas"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-041', 'PERSON', 'REPERTOIRE', 'Axel Honneth', 'reconhecimento e invisibilidade social',
     'FILÓSOFO / CONCEITO', 'Teoria do reconhecimento', 'Alemanha',
     'A invisibilidade social pode ser compreendida como forma simbólica de desrespeito: alguém é fisicamente percebido, mas não reconhecido como sujeito moral e social válido.',
     'Para Honneth, invisibilidade social não significa ser literalmente invisível; significa não receber reconhecimento social e moral suficiente para ser tratado como sujeito plenamente válido.',
     'Use quando o problema envolve apagamento social e dignidade, combinando bem com Fernando Braga da Costa.',
     'A teoria do reconhecimento de Axel Honneth ajuda a interpretar a invisibilidade social como forma de desrespeito, pois determinados indivíduos podem estar presentes no espaço público sem serem efetivamente reconhecidos como sujeitos de igual valor.',
     'Não usar ''reconhecimento'' como sinônimo genérico de fama, atenção ou visibilidade midiática.',
     'HIGH', 'ADVANCED', 'LOW',
     4, 5, 3, 4, 5, 5,
     '[]'::jsonb, '["DEFINE_CONCEPT", "SHOW_INVISIBILITY", "SOCIAL_CRITIQUE"]'::jsonb,
     '["falta de reconhecimento", "desrespeito", "exclusão simbólica"]'::jsonb,
     '["invisibilidade", "estigma", "trabalhadores subalternizados", "racismo", "PcD"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-044', 'PERSON', 'REPERTOIRE', 'Simone de Beauvoir', 'gênero como construção social',
     'FILÓSOFA / CONCEITO', 'Filosofia e estudos de gênero', 'França',
     'O Segundo Sexo abriu caminho para pensar a condição feminina como construída social e historicamente, e não explicada apenas pela biologia.',
     'Beauvoir ajuda a argumentar que expectativas e papéis atribuídos às mulheres são socialmente construídos e podem sustentar desigualdades.',
     'Use quando o argumento precisa mostrar que expectativas sociais sobre mulheres não são inevitáveis nem puramente biológicas.',
     'A reflexão de Simone de Beauvoir sobre a construção social da condição feminina permite questionar a naturalização de papéis de cuidado e dependência atribuídos às mulheres.',
     'Não transformar a famosa frase da autora em slogan sem contexto; priorize a ideia conceitual.',
     'HIGH', 'INTERMEDIATE', 'HIGH',
     4, 5, 4, 4, 4, 2,
     '[]'::jsonb, '["DEFINE_CONCEPT", "HISTORICAL_CONTEXT", "SOCIAL_CRITIQUE"]'::jsonb,
     '["papéis de gênero", "desigualdade estrutural", "naturalização"]'::jsonb,
     '["desigualdade de gênero", "maternidade", "trabalho", "participação política", "violência simbólica"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-046', 'CULTURAL_WORK', 'REPERTOIRE', 'O Homem Invisível', 'Ralph Ellison',
     'OBRA LITERÁRIA', 'Literatura, racismo e invisibilidade social', 'Estados Unidos',
     'O romance é usado pelo próprio Axel Honneth para pensar invisibilidade social como negação simbólica de reconhecimento, especialmente em contexto racial.',
     'O personagem é fisicamente visível, mas socialmente ''atravessado'' pelo olhar dos outros; a obra ajuda a discutir racismo, desumanização e falta de reconhecimento.',
     'Use junto a Honneth ou Fernando Braga da Costa para dar forma cultural ao conceito de invisibilidade.',
     'A metáfora de O Homem Invisível ajuda a demonstrar que exclusão não exige ausência física: um indivíduo pode estar presente e, ainda assim, ser socialmente ignorado e desumanizado.',
     'Não confundir o romance de Ralph Ellison com O Homem Invisível de H. G. Wells.',
     'LOW', 'INTERMEDIATE', 'LOW',
     3, 4, 4, 4, 5, 5,
     '[]'::jsonb, '["EXAMPLE", "SHOW_INVISIBILITY", "SOCIAL_CRITIQUE"]'::jsonb,
     '["desumanização", "racismo", "falta de reconhecimento"]'::jsonb,
     '["racismo", "invisibilidade social", "estigma", "reconhecimento"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-047', 'CULTURAL_WORK', 'REPERTOIRE', 'Um Defeito de Cor', 'Ana Maria Gonçalves',
     'OBRA LITERÁRIA', 'Literatura, escravidão, memória e identidade negra', 'Brasil',
     'Romance histórico brasileiro que permite abordar escravidão, diáspora, violência, memória e protagonismo negro a partir de perspectiva centrada em uma mulher negra.',
     'Obra útil para discutir memória da escravidão, protagonismo negro e apagamentos históricos; oferece uma perspectiva que desloca o olhar tradicional sobre a formação do Brasil.',
     'Use em temas de memória, cultura afro-brasileira e permanências históricas do racismo.',
     'Um Defeito de Cor permite relacionar a memória da escravidão à construção da identidade e ao apagamento de experiências negras, mostrando como disputas de memória também são disputas por reconhecimento.',
     'Não tratar o romance como documento histórico literal; é obra literária de base histórica.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     4, 4, 4, 4, 5, 5,
     '[]'::jsonb, '["HISTORICAL_CONTEXT", "EXAMPLE", "SOCIAL_CRITIQUE"]'::jsonb,
     '["apagamento histórico", "racismo", "memória coletiva"]'::jsonb,
     '["racismo", "memória", "escravidão", "identidade", "apagamento histórico"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Violência e segurança
    ('REP-040', 'PERSON', 'REPERTOIRE', 'Jacqueline Sinhoretto', NULL,
     'SOCIÓLOGA / PESQUISADORA', 'Violência, policiamento e justiça criminal', 'Brasil',
     'Pesquisa sociologia da violência, punição, segurança pública, policiamento, justiça criminal e prisões, permitindo discutir como instituições administram conflitos e como desigualdades podem aparecer no sistema de segurança.',
     'Violência e segurança não são apenas questões de crime individual; também envolvem instituições, policiamento, justiça, punição e desigualdades na administração de conflitos.',
     'Use para analisar segurança pública além do senso comum punitivista.',
     'As pesquisas de Jacqueline Sinhoretto ajudam a compreender que políticas de segurança devem ser avaliadas também pela forma como policiamento e justiça distribuem proteção, controle e punição entre diferentes grupos sociais.',
     'Não transformar análise sociológica em defesa de uma política penal específica.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     4, 5, 4, 4, 5, 5,
     '[]'::jsonb, '["EXPLAIN_CAUSE", "SHOW_INEQUALITY", "SOCIAL_CRITIQUE"]'::jsonb,
     '["seletividade institucional", "falha de políticas", "desigualdade racial"]'::jsonb,
     '["violência urbana", "policiamento", "prisões", "seletividade institucional"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Envelhecimento
    ('REP-038', 'PERSON', 'REPERTOIRE', 'Alexandre Kalache', NULL,
     'EPIDEMIOLOGISTA / ESPECIALISTA', 'Envelhecimento e longevidade', 'Brasil',
     'Referência internacional em envelhecimento, chama atenção para a velocidade da transição demográfica brasileira e para a necessidade de políticas que garantam envelhecimento ativo, saúde e participação.',
     'O Brasil envelhece rapidamente; longevidade precisa ser acompanhada de saúde, autonomia, proteção social, cidades e serviços preparados para uma população mais velha.',
     'Use para transformar ''envelhecimento populacional'' em debate sobre cidadania e preparação social.',
     'Alexandre Kalache destaca que o rápido envelhecimento brasileiro exige adaptação de sistemas de saúde, cuidado e cidades, pois viver mais não garante, sozinho, autonomia e qualidade de vida.',
     'Não tratar projeções demográficas antigas como números atuais sem verificar o ano.',
     'MEDIUM', 'BASIC', 'LOW',
     4, 5, 5, 5, 4, 5,
     '[]'::jsonb, '["PROVE_PROBLEM", "HISTORICAL_CONTEXT", "SUPPORT_INTERVENTION"]'::jsonb,
     '["despreparo institucional", "transição demográfica", "necessidade de políticas intersetoriais"]'::jsonb,
     '["etarismo", "cuidado", "saúde da pessoa idosa", "previdência", "cidades amigáveis"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-061', 'CULTURAL_WORK', 'REPERTOIRE', 'Aquarius', 'Kleber Mendonça Filho',
     'FILME', 'Envelhecimento, autonomia, memória e cidade', 'Brasil',
     'Clara, mulher de 65 anos, resiste à pressão de uma construtora para deixar o apartamento onde construiu parte de sua história, articulando envelhecimento, memória, autonomia e especulação imobiliária.',
     'Aquarius combina envelhecimento e cidade: uma mulher idosa é sujeito autônomo de desejo, memória e decisão, e resiste à pressão econômica sobre seu espaço de vida.',
     'Use para discutir envelhecimento sem reduzir idosos a dependência e para conectar autonomia pessoal a território e memória.',
     'Em Aquarius, a resistência de Clara à pressão imobiliária evidencia que envelhecimento não elimina autonomia, desejo ou vínculo territorial, além de mostrar como o espaço urbano também guarda memória e identidade.',
     'Não transformar o filme em retrato geral da velhice brasileira; é um exemplo cultural situado.',
     'LOW', 'BASIC', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["EXAMPLE", "SOCIAL_CRITIQUE", "SHOW_INEQUALITY"]'::jsonb,
     '[]'::jsonb,
     '["etarismo", "autonomia", "memória", "especulação imobiliária", "direito à cidade"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE)
ON CONFLICT (codigo) DO UPDATE
    SET tipo_entidade = EXCLUDED.tipo_entidade,
        papel = EXCLUDED.papel,
        nome = EXCLUDED.nome,
        subtitulo = EXCLUDED.subtitulo,
        tipo_descricao = EXCLUDED.tipo_descricao,
        area = EXCLUDED.area,
        pais = EXCLUDED.pais,
        ideia_central = EXCLUDED.ideia_central,
        lembre_na_prova = EXCLUDED.lembre_na_prova,
        como_usar = EXCLUDED.como_usar,
        exemplo_aplicacao = EXCLUDED.exemplo_aplicacao,
        erro_comum = EXCLUDED.erro_comum,
        risco_uso = EXCLUDED.risco_uso,
        dificuldade = EXCLUDED.dificuldade,
        saturacao = EXCLUDED.saturacao,
        nota_versatilidade = EXCLUDED.nota_versatilidade,
        nota_autoridade = EXCLUDED.nota_autoridade,
        nota_compreensao = EXCLUDED.nota_compreensao,
        nota_aplicabilidade = EXCLUDED.nota_aplicabilidade,
        nota_especificidade = EXCLUDED.nota_especificidade,
        nota_originalidade = EXCLUDED.nota_originalidade,
        tipos_evidencia = EXCLUDED.tipos_evidencia,
        funcoes_argumentativas = EXCLUDED.funcoes_argumentativas,
        tipos_argumento = EXCLUDED.tipos_argumento,
        tags = EXCLUDED.tags,
        status_verificacao = EXCLUDED.status_verificacao,
        verificado_em = EXCLUDED.verificado_em,
        ativo = EXCLUDED.ativo,
        atualizado_em = now();

-- 2) Fontes
DELETE FROM repertorio_fontes
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo IN (
    'REP-034', 'REP-035', 'REP-041', 'REP-044', 'REP-046',
    'REP-047', 'REP-040', 'REP-038', 'REP-061'
));

WITH fontes (codigo, tipo, descricao, url) AS (
    VALUES
    ('REP-034', 'GENERAL', 'Enciclopédia de Antropologia/USP — Kabengele Munanga',
     'https://www.ea.fflch.usp.br/autor/kabengele-munanga'),
    ('REP-035', 'GENERAL', 'IEA-USP — Nilma Lino Gomes',
     'https://www.iea.usp.br/pessoas/pasta-pessoan/nilma-lino-gomes/'),
    ('REP-041', 'GENERAL', 'Estudos Avançados/SciELO — A invisibilidade social como desrespeito na teoria do reconhecimento de Axel Honneth (2026)',
     'https://www.scielo.br/j/ea/a/qf6pqFqKDBt5csH6NbZvGdQ/'),
    ('REP-044', 'GENERAL', 'Cadernos Pagu/SciELO — discussão histórica sobre O Segundo Sexo e gênero',
     'https://www.scielo.br/j/cpa/a/mwW6jT5nfRpwPHMJwsgYQDJ/'),
    ('REP-046', 'GENERAL', 'Estudos Avançados/SciELO — Honneth analisa o romance de Ralph Ellison',
     'https://www.scielo.br/j/ea/a/qf6pqFqKDBt5csH6NbZvGdQ/'),
    ('REP-047', 'GENERAL', 'Academia Brasileira de Letras — Ana Maria Gonçalves e Um Defeito de Cor',
     'https://academia.org.br/noticias/abl-na-midia-folha-de-sao-paulo-venho-falando-pretugues-afirma-escritora-ana-maria'),
    ('REP-040', 'GENERAL', 'UFSCar — perfil de Jacqueline Sinhoretto',
     'https://www.sociais.ufscar.br/pt-br/docentes/jacqueline-sinhoretto'),
    ('REP-038', 'GENERAL', 'CEE-Fiocruz — entrevista com Alexandre Kalache',
     'https://cee.fiocruz.br/Alexandre-Kalache-A-melhor-coisa-que-pode-nos-acontecer-e-envelhecer/'),
    ('REP-061', 'CONCEPT', 'Psicologia USP/SciELO — análise de Aquarius',
     'https://new.scielo.br/j/pusp/a/v6fhT4Fk6Nyb8GgQtfJkx5L/')
)
INSERT INTO repertorio_fontes (repertorio_id, tipo, descricao, url)
SELECT r.id, f.tipo, f.descricao, f.url
FROM fontes f
JOIN repertorios r ON r.codigo = f.codigo;

-- 3) Problemas sociais de cada repertório
DELETE FROM repertorio_problemas
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo IN (
    'REP-034', 'REP-035', 'REP-041', 'REP-044', 'REP-046',
    'REP-047', 'REP-040', 'REP-038', 'REP-061'
));

WITH vinculos (codigo, problema_slug) AS (
    VALUES
    ('REP-034', 'racismo'), ('REP-034', 'identidade'), ('REP-034', 'inclusao-educacional'),
    ('REP-035', 'racismo'), ('REP-035', 'violencia-escolar'), ('REP-035', 'identidade'), ('REP-035', 'inclusao-educacional'),
    ('REP-041', 'trabalhos-invisibilizados'), ('REP-041', 'discriminacao'), ('REP-041', 'racismo'), ('REP-041', 'acessibilidade'),
    ('REP-044', 'sexismo'), ('REP-044', 'trabalho-cuidado'), ('REP-044', 'participacao-juventude'), ('REP-044', 'inclusao-educacional'),
    ('REP-046', 'racismo'), ('REP-046', 'trabalhos-invisibilizados'), ('REP-046', 'discriminacao'),
    ('REP-047', 'racismo'), ('REP-047', 'identidade'),
    ('REP-040', 'violencia-urbana'), ('REP-040', 'violencia-institucional'),
    ('REP-038', 'etarismo'), ('REP-038', 'trabalho-cuidado'), ('REP-038', 'cuidado-pessoa-idosa'), ('REP-038', 'precarizacao-trabalho'), ('REP-038', 'inclusao-pessoa-idosa'),
    ('REP-061', 'etarismo'), ('REP-061', 'autonomia-pessoa-idosa'), ('REP-061', 'moradia'), ('REP-061', 'segregacao-urbana')
)
INSERT INTO repertorio_problemas (repertorio_id, problema_id)
SELECT r.id, p.id
FROM vinculos v
JOIN repertorios r ON r.codigo = v.codigo
JOIN problemas_sociais p ON p.slug = v.problema_slug;

-- Conferência (rodar à parte, depois de subir o app):
-- SELECT count(*) FROM repertorios WHERE codigo IN ('REP-034', 'REP-035', 'REP-041', 'REP-044', 'REP-046', 'REP-047', 'REP-040', 'REP-038', 'REP-061');  -- 9
-- SELECT count(*) FROM repertorio_fontes f JOIN repertorios r ON r.id = f.repertorio_id WHERE r.codigo IN ('REP-034', 'REP-035', 'REP-041', 'REP-044', 'REP-046', 'REP-047', 'REP-040', 'REP-038', 'REP-061');  -- 9
-- SELECT count(*) FROM repertorio_problemas rp JOIN repertorios r ON r.id = rp.repertorio_id WHERE r.codigo IN ('REP-034', 'REP-035', 'REP-041', 'REP-044', 'REP-046', 'REP-047', 'REP-040', 'REP-038', 'REP-061');  -- 31