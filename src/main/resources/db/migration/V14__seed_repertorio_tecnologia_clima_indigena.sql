-- Repertório (issue #5, lote 7): 10 repertórios — Tecnologia + Infância + Cidades e clima + Povos indígenas.
-- Fonte: docs/repertorio/curadoria-v3.json.
-- As notas da rubrica só servem para curadoria/ordenação (não vão na API); REP-039 têm notas, os demais ficam nulos.
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
    -- Tecnologia e sociedade
    ('REP-048', 'PERSON', 'REPERTOIRE', 'Bruno Bioni', NULL,
     'JURISTA / PESQUISADOR', 'Privacidade, proteção de dados e regulação digital', 'Brasil',
     'Sua produção permite discutir proteção de dados para além do consentimento individual, incluindo responsabilização, governança e limites do tratamento de dados em ecossistemas digitais.',
     'Especialista brasileiro em proteção de dados: ajuda a explicar por que consentimento isolado nem sempre basta em ambientes marcados por assimetria de informação e tratamento massivo de dados.',
     'Use quando o problema envolver coleta, circulação ou uso de dados e a responsabilidade de organizações e plataformas.',
     'A produção de Bruno Bioni ajuda a compreender que a proteção de dados não pode depender apenas de decisões individuais de consentimento, pois sistemas digitais envolvem assimetrias de informação e responsabilidades institucionais.',
     'Não usar proteção de dados como sinônimo de sigilo absoluto ou proibição de qualquer tratamento de dados.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["DEFINE_CONCEPT", "LEGAL_GAP", "SUPPORT_INTERVENTION"]'::jsonb,
     '[]'::jsonb,
     '["privacidade", "proteção de dados", "plataformas", "IA", "assimetria informacional"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-049', 'PERSON', 'REPERTOIRE', 'Fernanda Bruno', NULL,
     'PESQUISADORA', 'Vigilância, tecnologia, subjetividade e visibilidade', 'Brasil',
     'Pesquisa como tecnologias de vigilância e visibilidade reorganizam relações de poder, comportamentos e subjetividades em ambientes digitais.',
     'Fernanda Bruno ajuda a entender vigilância digital como processo social: tecnologias coletam, observam e classificam comportamentos, afetando visibilidade, poder e autonomia.',
     'Use quando o argumento precisa explicar como monitoramento e coleta de rastros digitais produzem relações de poder, e não apenas riscos abstratos de privacidade.',
     'Os estudos de Fernanda Bruno permitem compreender que a vigilância digital não se limita a câmeras ou espionagem direta, pois também ocorre pela coleta e classificação cotidiana de rastros produzidos nas plataformas.',
     'Não equiparar toda coleta de dados a uma mesma forma de vigilância; explique o mecanismo e a finalidade.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["DEFINE_CONCEPT", "SOCIAL_CRITIQUE", "EXPLAIN_CAUSE"]'::jsonb,
     '[]'::jsonb,
     '["vigilância digital", "rastreamento", "plataformas", "visibilidade", "dados"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-050', 'PERSON', 'REPERTOIRE', 'Sérgio Amadeu da Silveira', NULL,
     'PESQUISADOR / CIENTISTA POLÍTICO', 'Sistemas algorítmicos, IA, soberania digital e tecnopolítica', 'Brasil',
     'Pesquisa implicações tecnopolíticas de sistemas algorítmicos e IA, incluindo classificação, modulação, privacidade, soberania de dados e efeitos políticos.',
     'Sistemas algorítmicos não são apenas ferramentas neutras: classificam, ordenam e modulam informações e escolhas conforme dados, objetivos e critérios definidos em seu desenho.',
     'Use para mostrar mecanismos concretos da IA e dos algoritmos, evitando frases vagas como “a tecnologia muda tudo”.',
     'As pesquisas de Sérgio Amadeu ajudam a compreender que sistemas algorítmicos não apenas exibem conteúdos, mas classificam e ordenam informações segundo critérios capazes de influenciar visibilidade e decisão.',
     'Não afirmar que todo algoritmo é necessariamente discriminatório; o risco depende de dados, objetivos, desenho e contexto.',
     'HIGH', 'INTERMEDIATE', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["DEFINE_CONCEPT", "EXPLAIN_CAUSE", "SOCIAL_CRITIQUE"]'::jsonb,
     '[]'::jsonb,
     '["algoritmos", "IA", "discriminação algorítmica", "desinformação", "soberania de dados"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Infância e adolescência
    ('REP-051', 'PERSON', 'REPERTOIRE', 'Rodolfo Rorato Londero', 'publicidade infantil e atenção no YouTube',
     'PESQUISADOR / ESTUDO', 'Comunicação, publicidade infantil e atenção', 'Brasil',
     'Estudo de conteúdo no YouTube mostrou como product placement pode integrar publicidade à narrativa infantil, dificultando que crianças reconheçam a mensagem comercial como publicidade.',
     'Na publicidade infantil digital, anúncios podem aparecer dentro da própria narrativa; isso dificulta a identificação da intenção comercial e explora a atenção infantil.',
     'Use para explicar mecanismos específicos de persuasão no ambiente digital infantil.',
     'O estudo de Rodolfo Londero e Meire Sebastião sobre publicidade infantil no YouTube mostra que o product placement pode tornar a intenção comercial menos identificável para a criança ao ser inserido na própria narrativa do vídeo.',
     'Não generalizar os resultados do recorte analisado para todo conteúdo infantil de todas as plataformas.',
     'MEDIUM', 'BASIC', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["EXPLAIN_CAUSE", "PROVE_PROBLEM", "SOCIAL_CRITIQUE"]'::jsonb,
     '[]'::jsonb,
     '["publicidade infantil", "economia da atenção", "YouTube", "consumo", "influenciadores mirins"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Cidades e clima
    ('REP-039', 'PERSON', 'REPERTOIRE', 'Carlos Nobre', NULL,
     'CLIMATOLOGISTA / PESQUISADOR', 'Mudanças climáticas e Amazônia', 'Brasil',
     'Pesquisador brasileiro de referência em mudanças climáticas. Seu trabalho é útil para fundamentar a dimensão científica da crise climática e discutir riscos sistêmicos.',
     'Mudanças climáticas têm fundamento científico robusto; seus impactos tornam-se sociais quando atingem de forma desigual territórios, infraestrutura, saúde e meios de vida.',
     'Use para estabelecer a base científica do fenômeno e depois conectá-la à desigualdade social.',
     'A produção científica de Carlos Nobre fornece base para compreender a crise climática como fenômeno mensurável, cujos impactos sociais dependem da capacidade desigual de prevenção e adaptação dos territórios.',
     'Não atribuir previsão de desastre específico sem fonte.',
     'MEDIUM', 'INTERMEDIATE', 'MEDIUM',
     4, 5, 4, 5, 5, 4,
     '[]'::jsonb, '["PROVE_PROBLEM", "EXPLAIN_CONSEQUENCE", "SUPPORT_INTERVENTION"]'::jsonb,
     '["risco climático", "necessidade de adaptação", "prevenção"]'::jsonb,
     '["eventos extremos", "Amazônia", "adaptação", "justiça climática"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-060', 'CULTURAL_WORK', 'REPERTOIRE', 'Era o Hotel Cambridge', 'Eliane Caffé',
     'FILME', 'Moradia, ocupação urbana, sem-teto e migração', 'Brasil',
     'Ficção documental sobre refugiados e pessoas sem-teto que dividem uma ocupação em prédio abandonado no centro de São Paulo sob ameaça de despejo.',
     'Mostra moradia como questão de direito, território e pertencimento; permite discutir ocupações, déficit habitacional e coexistência de grupos vulneráveis no espaço urbano.',
     'Use para complementar Estatuto da Cidade e Raquel Rolnik em argumentos sobre direito à moradia e uso do espaço urbano.',
     'Era o Hotel Cambridge evidencia que a falta de moradia não é apenas ausência de teto, pois envolve acesso à cidade, estabilidade, pertencimento e disputa pelo uso social de imóveis urbanos.',
     'Não usar “sem-teto” e “população em situação de rua” como categorias idênticas; o filme trata sobretudo de ocupação e moradia.',
     'MEDIUM', 'BASIC', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["EXAMPLE", "SOCIAL_CRITIQUE", "COMPARE_REALITY"]'::jsonb,
     '[]'::jsonb,
     '["moradia", "sem-teto", "ocupações", "migração", "desigualdade urbana"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Povos indígenas e comunidades tradicionais
    ('REP-055', 'PERSON', 'REPERTOIRE', 'Ailton Krenak', NULL,
     'INTELECTUAL / ESCRITOR INDÍGENA', 'Direitos indígenas, cultura, território e pensamento socioambiental', 'Brasil',
     'Intelectual Krenak e liderança histórica do movimento indígena, articulou direitos constitucionais e produz reflexão sobre modos de vida, natureza, desenvolvimento e diversidade cultural.',
     'Krenak ajuda a questionar a ideia de natureza como mero recurso e a relacionar território, cultura e continuidade da vida dos povos originários.',
     'Use em temas que envolvam território e ambiente quando for importante incorporar perspectiva indígena, e não apenas falar sobre indígenas a partir de fora.',
     'A reflexão de Ailton Krenak permite questionar modelos de desenvolvimento que separam sociedade e natureza, especialmente quando a exploração territorial ameaça modos de vida e continuidade cultural de povos originários.',
     'Não transformar Krenak em “ambientalista genérico”; preserve a dimensão indígena, territorial, histórica e cultural de sua obra.',
     'MEDIUM', 'INTERMEDIATE', 'MEDIUM',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["SOCIAL_CRITIQUE", "DEFINE_CONCEPT", "HISTORICAL_CONTEXT"]'::jsonb,
     '[]'::jsonb,
     '["direitos indígenas", "território", "cultura", "meio ambiente", "desenvolvimento", "memória"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-056', 'PERSON', 'REPERTOIRE', 'Davi Kopenawa Yanomami', NULL,
     'LIDERANÇA / INTELECTUAL INDÍGENA', 'Território Yanomami, floresta, cosmologia e direitos indígenas', 'Brasil',
     'Xamã e presidente da Hutukara Associação Yanomami, autor com Bruce Albert de A Queda do Céu. Sua produção articula cosmologia Yanomami, crítica à destruição ambiental e defesa territorial.',
     'Kopenawa fala a partir do pensamento Yanomami: território e floresta não são apenas recursos econômicos, mas condições de existência cultural, espiritual e coletiva.',
     'Use para incorporar uma voz indígena diretamente afetada por invasões territoriais e destruição ambiental.',
     'O pensamento de Davi Kopenawa evidencia que a destruição da floresta não é apenas perda ambiental: para povos como os Yanomami, ela ameaça simultaneamente território, saúde, memória e continuidade cultural.',
     'Não reduzir a cosmologia Yanomami a metáfora ambiental ocidental nem usar Kopenawa como representante de todos os povos indígenas.',
     'HIGH', 'INTERMEDIATE', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["SOCIAL_CRITIQUE", "HISTORICAL_CONTEXT", "EXAMPLE"]'::jsonb,
     '[]'::jsonb,
     '["garimpo", "território", "floresta", "saúde indígena", "violência", "memória"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-057', 'CULTURAL_WORK', 'REPERTOIRE', 'A Queda do Céu', 'Davi Kopenawa e Bruce Albert',
     'OBRA LITERÁRIA / AUTOETNOGRAFIA', 'Cosmologia Yanomami, ambiente, território e contato interétnico', 'Brasil',
     'Relato de vida, autoetnografia e manifesto cosmopolítico que apresenta pensamento Yanomami e critica processos de destruição territorial e ambiental associados ao contato e à exploração.',
     'A Queda do Céu permite discutir território e ambiente a partir da visão Yanomami, evitando tratar povos indígenas apenas como objeto externo de estudo.',
     'Use para dar base cultural e indígena a argumentos sobre território, destruição ambiental e modelos de desenvolvimento.',
     'Em A Queda do Céu, a crítica de Davi Kopenawa ao “povo da mercadoria” permite confrontar modelos de exploração que tratam a floresta somente como recurso, ignorando vínculos territoriais e cosmológicos indígenas.',
     'Não apresentar a obra como descrição de todos os povos indígenas; ela é situada no universo Yanomami.',
     'HIGH', 'ADVANCED', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["EXAMPLE", "SOCIAL_CRITIQUE", "HISTORICAL_CONTEXT"]'::jsonb,
     '[]'::jsonb,
     '["território", "garimpo", "meio ambiente", "cultura", "memória", "colonialismo"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-058', 'CULTURAL_WORK', 'REPERTOIRE', 'Chuva é Cantoria na Aldeia dos Mortos', 'Renée Nader Messora e João Salaviza',
     'FILME', 'Identidade indígena, cidade, cultura e pertencimento', 'Brasil',
     'O filme acompanha Ihjãc, jovem Krahô que se afasta da aldeia e enfrenta na cidade dificuldades ligadas à identidade, pertencimento e vida indígena contemporânea.',
     'Filme sobre um jovem Krahô entre aldeia, ritual, cidade e identidade; útil para evitar a representação de povos indígenas como figuras apenas do passado.',
     'Use para discutir indígenas no Brasil contemporâneo, tensões entre cidade e território e preservação cultural sem congelar identidades.',
     'Chuva é Cantoria na Aldeia dos Mortos apresenta um jovem Krahô vivendo dilemas contemporâneos entre cidade, ritual e pertencimento, o que ajuda a romper a imagem de povos indígenas como grupos presos ao passado.',
     'Não usar o filme como retrato de todos os povos indígenas nem como documentário estatístico.',
     'LOW', 'BASIC', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["EXAMPLE", "COMPARE_REALITY", "SOCIAL_CRITIQUE"]'::jsonb,
     '[]'::jsonb,
     '["identidade", "pertencimento", "cultura", "cidade", "direitos indígenas"]'::jsonb,
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
    'REP-048', 'REP-049', 'REP-050', 'REP-051', 'REP-039',
    'REP-060', 'REP-055', 'REP-056', 'REP-057', 'REP-058'
));

WITH fontes (codigo, tipo, descricao, url) AS (
    VALUES
    ('REP-048', 'PROFILE', 'IEA-USP — Bruno Bioni',
     'https://www.iea.usp.br/pessoas/pasta-pessoab/bruno-bioni'),
    ('REP-048', 'CONCEPT', 'Bruno Bioni — Proteção de Dados Pessoais: a função e os limites do consentimento; Regulação e Proteção de Dados: o princípio da accountability.',
     NULL),
    ('REP-049', 'PROFILE', 'IEA-USP — Fernanda Bruno',
     'https://www.iea.usp.br/pessoas/pasta-pessoaf/fernanda-bruno'),
    ('REP-049', 'CONCEPT', 'LAVITS / obras Máquinas de ver, modos de ser e Tecnopolíticas da Vigilância',
     'https://lavits.org/a_rede/fernanda-bruno/'),
    ('REP-050', 'PROFILE', 'UFABC — Sérgio Amadeu da Silveira',
     'https://www.ufabc.edu.br/ensino/docentes/sergio-amadeu-da-silveira'),
    ('REP-050', 'CONCEPT', 'UFABC/Cientometria — pesquisa sobre implicações tecnopolíticas de algoritmos e IA',
     'https://cientometria.pesquisa.ufabc.edu.br/UFABC-professores/membro-6800442072685268.html'),
    ('REP-051', 'PROFILE', 'Revista Anagrama/USP — biografia dos autores',
     'https://revistas.usp.br/anagrama/pt_BR/article/view/202125'),
    ('REP-051', 'CONCEPT', 'Sebastião & Londero (2022), Atenção e publicidade infantil no YouTube',
     'https://doi.org/10.11606/issn.1982-1689.anagrama.2022.202125'),
    ('REP-039', 'GENERAL', 'INPE — Fundamentos Científicos das Mudanças Climáticas',
     'https://www.inpe.br/noticias/noticia.php?Cod_Noticia=3085'),
    ('REP-060', 'CONCEPT', 'Aurora Filmes — Era o Hotel Cambridge',
     'https://www.aurorafilmes.com.br/era-o-hotel-cambridge'),
    ('REP-055', 'PROFILE', 'Academia Brasileira de Letras — Ailton Krenak',
     'https://www2.academia.org.br/academicos/ailton-krenak/biografia'),
    ('REP-055', 'CONCEPT', 'Obras Ideias para adiar o fim do mundo, A vida não é útil e Futuro ancestral.',
     NULL),
    ('REP-056', 'PROFILE', 'Hutukara Associação Yanomami — Davi Kopenawa',
     'https://hutukarayanomami.org/davi-kopenawa/'),
    ('REP-056', 'CONCEPT', 'A Queda do Céu — Companhia das Letras / Museu Nacional dos Povos Indígenas',
     'https://www.companhiadasletras.com.br/9788535926200-a-queda-do-ceu-3640/p'),
    ('REP-057', 'CONCEPT', 'Museu Nacional dos Povos Indígenas — A Queda do Céu',
     'https://pesquisa.museudoindio.gov.br/index.php/a-queda-do-ceu-palavras-de-um-xama-yanomami'),
    ('REP-058', 'CONCEPT', 'Instituto Moreira Salles — Chuva é Cantoria na Aldeia dos Mortos',
     'https://ims.com.br/filme/chuva-e-cantoria-na-aldeia-dos-mortos/')
)
INSERT INTO repertorio_fontes (repertorio_id, tipo, descricao, url)
SELECT r.id, f.tipo, f.descricao, f.url
FROM fontes f
JOIN repertorios r ON r.codigo = f.codigo;

-- 3) Problemas sociais de cada repertório
DELETE FROM repertorio_problemas
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo IN (
    'REP-048', 'REP-049', 'REP-050', 'REP-051', 'REP-039',
    'REP-060', 'REP-055', 'REP-056', 'REP-057', 'REP-058'
));

WITH vinculos (codigo, problema_slug) AS (
    VALUES
    ('REP-048', 'dados-privacidade'), ('REP-048', 'plataformas-digitais'), ('REP-048', 'algoritmos'),
    ('REP-049', 'dados-privacidade'), ('REP-049', 'plataformas-digitais'), ('REP-049', 'identidade'),
    ('REP-050', 'algoritmos'), ('REP-050', 'discriminacao'), ('REP-050', 'dados-privacidade'),
    ('REP-051', 'infancia-digital'), ('REP-051', 'plataformas-digitais'), ('REP-051', 'trabalho-infantil'),
    ('REP-039', 'calor-extremo'), ('REP-039', 'justica-climatica'),
    ('REP-060', 'moradia'), ('REP-060', 'seca'), ('REP-060', 'segregacao-urbana'),
    ('REP-055', 'territorio-indigena'), ('REP-055', 'cultura-memoria-indigena'), ('REP-055', 'direitos-indigenas'), ('REP-055', 'meio-ambiente'),
    ('REP-056', 'territorio-indigena'), ('REP-056', 'direitos-indigenas'), ('REP-056', 'justica-climatica'), ('REP-056', 'saude-indigena'),
    ('REP-057', 'territorio-indigena'), ('REP-057', 'cultura-memoria-indigena'), ('REP-057', 'meio-ambiente'),
    ('REP-058', 'cultura-memoria-indigena'), ('REP-058', 'identidade'), ('REP-058', 'segregacao-urbana'), ('REP-058', 'direitos-indigenas')
)
INSERT INTO repertorio_problemas (repertorio_id, problema_id)
SELECT r.id, p.id
FROM vinculos v
JOIN repertorios r ON r.codigo = v.codigo
JOIN problemas_sociais p ON p.slug = v.problema_slug;

-- Conferência (rodar à parte, depois de subir o app):
-- SELECT count(*) FROM repertorios WHERE codigo IN ('REP-048', 'REP-049', 'REP-050', 'REP-051', 'REP-039', 'REP-060', 'REP-055', 'REP-056', 'REP-057', 'REP-058');  -- 10
-- SELECT count(*) FROM repertorio_fontes f JOIN repertorios r ON r.id = f.repertorio_id WHERE r.codigo IN ('REP-048', 'REP-049', 'REP-050', 'REP-051', 'REP-039', 'REP-060', 'REP-055', 'REP-056', 'REP-057', 'REP-058');  -- 16
-- SELECT count(*) FROM repertorio_problemas rp JOIN repertorios r ON r.id = rp.repertorio_id WHERE r.codigo IN ('REP-048', 'REP-049', 'REP-050', 'REP-051', 'REP-039', 'REP-060', 'REP-055', 'REP-056', 'REP-057', 'REP-058');  -- 32