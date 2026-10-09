-- Repertório (issue #5, lote 5): 10 repertórios — PcD e neurodiversidade + Trabalho e cuidado + Invisibilidade social.
-- Fonte: docs/repertorio/curadoria-v3.json.
-- As notas da rubrica só servem para curadoria/ordenação (não vão na API); REP-029, REP-036, REP-037, REP-030, REP-031, REP-032, REP-045, REP-033 têm notas, os demais ficam nulos.
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
    -- PcD e neurodiversidade
    ('REP-029', 'PERSON', 'REPERTOIRE', 'Maria Teresa Eglér Mantoan', NULL,
     'PESQUISADORA / EDUCADORA', 'Educação inclusiva', 'Brasil',
     'Defende a escola comum inclusiva e a eliminação de barreiras educacionais, com base no direito à diferença e nos direitos humanos.',
     'Inclusão escolar não é apenas matricular estudantes com deficiência; é reorganizar a escola para eliminar barreiras e garantir participação e aprendizagem no ensino comum.',
     'Use para defender que a dificuldade de inclusão não deve ser atribuída apenas ao aluno, mas também à organização escolar.',
     'Os estudos de Maria Teresa Mantoan permitem compreender a inclusão como transformação da própria escola, e não como mera inserção física do estudante em uma estrutura que permanece excludente.',
     'Não afirmar que inclusão significa ausência de qualquer suporte especializado.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     4, 5, 4, 5, 5, 5,
     '[]'::jsonb, '["DEFINE_CONCEPT", "EXPLAIN_CAUSE", "SUPPORT_INTERVENTION"]'::jsonb,
     '["barreiras institucionais", "desenho excludente", "formação docente"]'::jsonb,
     '["educação inclusiva", "capacitismo", "AEE", "formação docente", "barreiras escolares"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-036', 'PERSON', 'REPERTOIRE', 'Débora Diniz e o modelo social da deficiência', NULL,
     'PESQUISADORA / CONCEITO', 'Deficiência, bioética e direitos humanos', 'Brasil',
     'No campo dos estudos da deficiência, a abordagem social desloca o foco exclusivo do corpo individual para as barreiras sociais, políticas e ambientais que produzem exclusão.',
     'Deficiência não pode ser compreendida apenas como lesão ou diagnóstico; barreiras sociais e institucionais participam da produção da desvantagem.',
     'Use para explicar por que remover barreiras pode ser tão importante quanto oferecer tratamento individual.',
     'A abordagem social da deficiência presente nos estudos de Débora Diniz ajuda a mostrar que a exclusão surge também da maneira como instituições e ambientes são organizados.',
     'Não negar a existência de dimensões corporais ou necessidades de saúde; o modelo social critica sua transformação em explicação exclusiva da exclusão.',
     'HIGH', 'INTERMEDIATE', 'LOW',
     4, 5, 4, 5, 5, 5,
     '[]'::jsonb, '["DEFINE_CONCEPT", "EXPLAIN_CAUSE", "SOCIAL_CRITIQUE"]'::jsonb,
     '["barreiras sociais", "igualdade material", "justiça social"]'::jsonb,
     '["capacitismo", "acessibilidade", "BPC", "inclusão", "políticas sociais"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-037', 'PERSON', 'REPERTOIRE', 'Izabel Maior', NULL,
     'MÉDICA FISIATRA / ESPECIALISTA', 'Direitos da pessoa com deficiência e acessibilidade', 'Brasil',
     'Especialista e ativista histórica em políticas de acessibilidade e direitos da pessoa com deficiência, participou da implementação da abordagem biopsicossocial e da Convenção da ONU no Brasil.',
     'Izabel Maior ajuda a conectar acessibilidade, direitos e modelo biopsicossocial: a deficiência deve ser avaliada em interação com barreiras e condições de participação.',
     'Use para dar concretude brasileira a temas de políticas de inclusão e avaliação da deficiência.',
     'A trajetória de Izabel Maior evidencia como a inclusão depende de políticas que reconheçam a interação entre impedimentos pessoais e barreiras sociais, e não apenas de diagnósticos médicos isolados.',
     'Não apresentar a avaliação biopsicossocial como simples substituição do médico por outro profissional.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     3, 5, 4, 4, 5, 5,
     '[]'::jsonb, '["SUPPORT_INTERVENTION", "DEFINE_CONCEPT", "HISTORICAL_CONTEXT"]'::jsonb,
     '["barreiras institucionais", "implementação de direitos", "acessibilidade"]'::jsonb,
     '["acessibilidade", "avaliação biopsicossocial", "políticas de deficiência", "participação"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Trabalho e cuidado
    ('REP-030', 'PERSON', 'REPERTOIRE', 'Ricardo Antunes', NULL,
     'SOCIÓLOGO / PESQUISADOR', 'Sociologia do trabalho', 'Brasil',
     'Analisa transformações do trabalho contemporâneo, terceirização, precarização, jornadas e novas formas de exploração, incluindo a plataformização.',
     'Antunes ajuda a interpretar como mudanças tecnológicas e empresariais podem gerar novas formas de precarização, intensificação e insegurança do trabalho.',
     'Use para explicar que inovação tecnológica não produz automaticamente melhoria das condições de trabalho.',
     'A análise de Ricardo Antunes sobre as transformações do trabalho ajuda a compreender que plataformas digitais podem combinar inovação tecnológica com jornadas intensas e menor proteção social.',
     'Não apresentar toda tecnologia como necessariamente precarizante; o argumento deve tratar das relações de trabalho concretas.',
     'MEDIUM', 'INTERMEDIATE', 'MEDIUM',
     4, 5, 4, 5, 4, 4,
     '[]'::jsonb, '["EXPLAIN_CAUSE", "SOCIAL_CRITIQUE", "SHOW_INEQUALITY"]'::jsonb,
     '["precarização", "assimetria entre empresa e trabalhador", "desproteção"]'::jsonb,
     '["uberização", "terceirização", "jornada", "precarização", "automação"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-031', 'PERSON', 'REPERTOIRE', 'Ludmila Costhek Abílio', NULL,
     'SOCIÓLOGA / PESQUISADORA', 'Trabalho por plataformas e uberização', 'Brasil',
     'Pesquisa a uberização e processos de informalização, destacando formas de gestão e controle do trabalho mediadas por plataformas e a transferência de riscos para trabalhadores.',
     'Na uberização, o trabalhador parece autônomo, mas pode continuar submetido a formas de controle, avaliação e gestão algorítmica, assumindo custos e riscos da atividade.',
     'Use em temas de trabalho algorítmico para ir além da oposição simplista entre emprego formal e autonomia.',
     'As pesquisas de Ludmila Abílio mostram que a aparente autonomia do trabalho por plataformas pode coexistir com formas intensas de controle e com a transferência de custos e riscos para o próprio trabalhador.',
     'Não usar uberização como sinônimo de qualquer trabalho informal.',
     'HIGH', 'INTERMEDIATE', 'LOW',
     3, 5, 4, 5, 5, 5,
     '[]'::jsonb, '["DEFINE_CONCEPT", "EXPLAIN_CAUSE", "SOCIAL_CRITIQUE"]'::jsonb,
     '["autonomia subordinada", "transferência de riscos", "precarização"]'::jsonb,
     '["trabalho por aplicativos", "entregadores", "informalidade", "gestão algorítmica"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-032', 'PERSON', 'REPERTOIRE', 'Helena Hirata', NULL,
     'SOCIÓLOGA / PESQUISADORA', 'Trabalho, gênero e cuidado', 'Brasil / França',
     'Analisa divisão sexual do trabalho e relações de cuidado, mostrando como gênero, raça e classe se articulam nas trajetórias de cuidadoras.',
     'O trabalho de cuidado é indispensável, mas recai de forma desigual sobre mulheres; gênero, raça e classe estruturam quem cuida, em quais condições e com qual reconhecimento.',
     'Use para mostrar que cuidado não é apenas uma escolha familiar privada, mas uma relação social com distribuição desigual.',
     'Os estudos de Helena Hirata evidenciam que o cuidado, embora essencial à vida social, é distribuído de forma desigual e permanece fortemente associado ao trabalho feminino e às hierarquias de classe e raça.',
     'Não afirmar que todo trabalho de cuidado é necessariamente não remunerado.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     4, 5, 4, 5, 5, 5,
     '[]'::jsonb, '["EXPLAIN_CAUSE", "SHOW_INEQUALITY", "SOCIAL_CRITIQUE"]'::jsonb,
     '["divisão sexual do trabalho", "desvalorização do cuidado", "interseccionalidade"]'::jsonb,
     '["trabalho de cuidado", "envelhecimento", "trabalho doméstico", "desigualdade de gênero"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-045', 'CULTURAL_WORK', 'REPERTOIRE', 'Que Horas Ela Volta?', 'Anna Muylaert',
     'FILME', 'Cinema, trabalho doméstico e desigualdade de classe', 'Brasil',
     'O filme acompanha uma trabalhadora doméstica e a chegada de sua filha à casa dos patrões, tensionando fronteiras de classe, trabalho de cuidado, acesso à universidade e desigualdades naturalizadas.',
     'Use o filme para discutir fronteiras de classe e trabalho doméstico: quem cuida dos filhos de outras famílias muitas vezes precisa se afastar dos próprios filhos e enfrenta limites sociais considerados ''naturais''.',
     'Use como exemplo cultural para humanizar argumentos sobre trabalho doméstico e reprodução de desigualdades.',
     'Em Que Horas Ela Volta?, a relação entre Val, sua filha e a família empregadora expõe fronteiras de classe que ultrapassam o salário e organizam também espaços, expectativas e oportunidades.',
     'Não apresentar o filme como retrato estatístico de todas as trabalhadoras domésticas.',
     'LOW', 'BASIC', 'LOW',
     4, 4, 5, 5, 5, 5,
     '[]'::jsonb, '["EXAMPLE", "SOCIAL_CRITIQUE", "COMPARE_REALITY"]'::jsonb,
     '["desigualdade estrutural", "trabalho de cuidado", "hierarquias sociais"]'::jsonb,
     '["trabalho doméstico", "desigualdade de classe", "cuidado", "acesso ao ensino superior"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-059', 'CULTURAL_WORK', 'REPERTOIRE', 'O Menino e o Mundo', 'Alê Abreu',
     'FILME / ANIMAÇÃO', 'Trabalho, globalização, desigualdade e tecnologia', 'Brasil',
     'A animação acompanha um menino em uma sociedade marcada por urbanização, industrialização, trabalho e desigualdades, permitindo discutir impactos humanos de transformações econômicas e tecnológicas.',
     'A animação mostra transformações do trabalho e da vida social pelo olhar de uma criança, conectando industrialização, globalização, desigualdade e perda de vínculos.',
     'Use como referência cultural para humanizar debates sobre mudanças produtivas e desigualdades; não como prova empírica sobre IA.',
     'O Menino e o Mundo permite ilustrar como transformações produtivas e tecnológicas podem reorganizar trabalho, território e vínculos sociais, produzindo impactos que não se distribuem igualmente.',
     'Não dizer que o filme é especificamente “sobre inteligência artificial”; sua relação é com transformações tecnológicas e produtivas em sentido mais amplo.',
     'LOW', 'BASIC', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["EXAMPLE", "SOCIAL_CRITIQUE", "EXPLAIN_CONSEQUENCE"]'::jsonb,
     '[]'::jsonb,
     '["transformações do trabalho", "desigualdade", "tecnologia", "urbanização", "globalização"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Invisibilidade social
    ('REP-033', 'PERSON', 'REPERTOIRE', 'Fernando Braga da Costa', NULL,
     'PSICÓLOGO SOCIAL / PESQUISADOR', 'Invisibilidade pública e trabalho', 'Brasil',
     'Investigou a invisibilidade pública por meio de pesquisa etnográfica com garis, trabalhando como gari semanalmente durante anos. O conceito descreve uma forma de apagamento social de pessoas em posições subalternizadas.',
     'Invisibilidade pública: certos trabalhadores são fisicamente vistos, mas socialmente ignorados; a posição ocupada pode produzir apagamento e falta de reconhecimento.',
     'Use quando o problema envolver grupos presentes no cotidiano, mas pouco reconhecidos como sujeitos sociais.',
     'A pesquisa de Fernando Braga da Costa com garis mostra que a exclusão pode assumir a forma de invisibilidade pública, quando determinados trabalhadores são percebidos apenas por sua função e deixam de ser reconhecidos como sujeitos.',
     'Não afirmar que sua pesquisa original foi sobre população em situação de rua.',
     'MEDIUM', 'BASIC', 'LOW',
     4, 5, 5, 5, 5, 5,
     '[]'::jsonb, '["SHOW_INVISIBILITY", "DEFINE_CONCEPT", "SOCIAL_CRITIQUE"]'::jsonb,
     '["invisibilidade social", "desrespeito", "hierarquização do trabalho"]'::jsonb,
     '["trabalhadores invisibilizados", "população em situação de rua", "estigma", "desigualdade"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-054', 'PERSON', 'REPERTOIRE', 'André Luiz Freitas Dias', NULL,
     'PESQUISADOR / EXTENSIONISTA', 'Políticas públicas e população em situação de rua', 'Brasil',
     'Coordena o Observatório Brasileiro de Políticas Públicas com a População em Situação de Rua (OBPopRua/POLOS-UFMG), produzindo pesquisas e monitoramento sobre perfil, violência e políticas públicas.',
     'Pesquisador da UFMG especializado em população em situação de rua; ajuda a conectar invisibilidade social a dados, violência, saúde, documentação e desenho de políticas públicas.',
     'Use para sair de uma abordagem abstrata da invisibilidade e trabalhar condições concretas e políticas voltadas à população em situação de rua.',
     'O trabalho do OBPopRua, coordenado por André Dias, mostra que a população em situação de rua precisa ser tratada como público específico de políticas intersetoriais, com dados próprios sobre moradia, saúde, documentação e violência.',
     'Não tratar estimativas baseadas em CadÚnico como censo completo da população em situação de rua.',
     'MEDIUM', 'BASIC', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["PROVE_PROBLEM", "SHOW_INVISIBILITY", "SUPPORT_INTERVENTION"]'::jsonb,
     '[]'::jsonb,
     '["população em situação de rua", "violência", "documentação", "saúde", "moradia", "políticas públicas"]'::jsonb,
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
    'REP-029', 'REP-036', 'REP-037', 'REP-030', 'REP-031',
    'REP-032', 'REP-045', 'REP-059', 'REP-033', 'REP-054'
));

WITH fontes (codigo, tipo, descricao, url) AS (
    VALUES
    ('REP-029', 'GENERAL', 'LEPED/Unicamp — área temática e pesquisas',
     'https://www.leped.fe.unicamp.br/menu/o-leped'),
    ('REP-036', 'GENERAL', 'Repositório UnB — Deficiência e igualdade',
     'https://www.repositorio.unb.br/handle/10482/46499'),
    ('REP-037', 'GENERAL', 'UFRJ Acessibilidade — perfil de Izabel Maior',
     'https://acessibilidade.ufrj.br/eventos/roda-de-conversa-com-izabel-maior-avaliacao-biopsicossocial-da-deficiencia/'),
    ('REP-030', 'GENERAL', 'Jornal da Unicamp — perfil de Ricardo Antunes',
     'https://jornal.unicamp.br/autoria/ricardo-antunes/'),
    ('REP-031', 'GENERAL', 'IFCH/Unicamp — perfil e produção de Ludmila Abílio',
     'https://www.ifch.unicamp.br/pessoas/ludmila-costhek-abilio'),
    ('REP-032', 'GENERAL', 'Estudos Avançados/USP — Comparando relações de cuidado: Brasil, França, Japão',
     'https://revistas.usp.br/eav/pt_BR/article/view/170421'),
    ('REP-045', 'GENERAL', 'Instituto Moreira Salles — Que Horas Ela Volta?',
     'https://ims.com.br/filme/que-horas-ela-volta/'),
    ('REP-059', 'CONCEPT', 'Artigo 2026 sobre tecnologia, desigualdade e O Menino e o Mundo',
     'https://periodicos.grupotiradentes.com.br/educacao/article/view/13514'),
    ('REP-033', 'GENERAL', 'Repositório USP — Garis: um estudo de psicologia sobre invisibilidade pública',
     'https://repositorio.usp.br/item/001298332'),
    ('REP-054', 'PROFILE', 'PPGD/UFMG — André Luiz Freitas Dias',
     'https://pos.direito.ufmg.br/andredias/'),
    ('REP-054', 'CONCEPT', 'Polos de Cidadania/UFMG — biblioteca e relatórios do OBPopRua',
     'https://polos.direito.ufmg.br/bibliotecaobservatorio/')
)
INSERT INTO repertorio_fontes (repertorio_id, tipo, descricao, url)
SELECT r.id, f.tipo, f.descricao, f.url
FROM fontes f
JOIN repertorios r ON r.codigo = f.codigo;

-- 3) Problemas sociais de cada repertório
DELETE FROM repertorio_problemas
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo IN (
    'REP-029', 'REP-036', 'REP-037', 'REP-030', 'REP-031',
    'REP-032', 'REP-045', 'REP-059', 'REP-033', 'REP-054'
));

WITH vinculos (codigo, problema_slug) AS (
    VALUES
    ('REP-029', 'inclusao-escolar-pcd'), ('REP-029', 'inclusao-educacional'), ('REP-029', 'acessibilidade'), ('REP-029', 'discriminacao'),
    ('REP-036', 'acessibilidade'), ('REP-036', 'discriminacao'), ('REP-036', 'inclusao-educacional'),
    ('REP-037', 'acessibilidade'), ('REP-037', 'participacao-juventude'),
    ('REP-030', 'plataformizacao-trabalho'), ('REP-030', 'precarizacao-trabalho'),
    ('REP-031', 'plataformizacao-trabalho'), ('REP-031', 'precarizacao-trabalho'), ('REP-031', 'algoritmos'),
    ('REP-032', 'trabalho-cuidado'), ('REP-032', 'inclusao-pessoa-idosa'), ('REP-032', 'trabalho-domestico'), ('REP-032', 'sexismo'),
    ('REP-045', 'trabalho-domestico'), ('REP-045', 'pobreza'), ('REP-045', 'trabalho-cuidado'), ('REP-045', 'inclusao-educacional'),
    ('REP-059', 'precarizacao-trabalho'), ('REP-059', 'segregacao-urbana'),
    ('REP-033', 'trabalhos-invisibilizados'), ('REP-033', 'populacao-rua'), ('REP-033', 'discriminacao'),
    ('REP-054', 'populacao-rua'), ('REP-054', 'moradia')
)
INSERT INTO repertorio_problemas (repertorio_id, problema_id)
SELECT r.id, p.id
FROM vinculos v
JOIN repertorios r ON r.codigo = v.codigo
JOIN problemas_sociais p ON p.slug = v.problema_slug;

-- Conferência (rodar à parte, depois de subir o app):
-- SELECT count(*) FROM repertorios WHERE codigo IN ('REP-029', 'REP-036', 'REP-037', 'REP-030', 'REP-031', 'REP-032', 'REP-045', 'REP-059', 'REP-033', 'REP-054');  -- 10
-- SELECT count(*) FROM repertorio_fontes f JOIN repertorios r ON r.id = f.repertorio_id WHERE r.codigo IN ('REP-029', 'REP-036', 'REP-037', 'REP-030', 'REP-031', 'REP-032', 'REP-045', 'REP-059', 'REP-033', 'REP-054');  -- 11
-- SELECT count(*) FROM repertorio_problemas rp JOIN repertorios r ON r.id = rp.repertorio_id WHERE r.codigo IN ('REP-029', 'REP-036', 'REP-037', 'REP-030', 'REP-031', 'REP-032', 'REP-045', 'REP-059', 'REP-033', 'REP-054');  -- 29