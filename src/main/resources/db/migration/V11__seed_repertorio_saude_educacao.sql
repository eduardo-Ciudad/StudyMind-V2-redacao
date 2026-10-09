-- Repertório (issue #5, lote 4): 9 repertórios — Saúde + Educação.
-- Fonte: docs/repertorio/curadoria-v3.json.
-- As notas da rubrica só servem para curadoria/ordenação (não vão na API); REP-025, REP-026, REP-027, REP-028, REP-042, REP-043 têm notas, os demais ficam nulos.
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
    -- Saúde
    ('REP-025', 'PERSON', 'REPERTOIRE', 'Rita Barradas Barata', NULL,
     'PESQUISADORA / EPIDEMIOLOGISTA', 'Epidemiologia social e desigualdades em saúde', 'Brasil',
     'Estuda como posição social, renda, gênero e raça se relacionam com diferenças de saúde e acesso a serviços. Sua obra é especialmente útil para explicar que desigualdades em saúde são socialmente produzidas, e não apenas resultado de escolhas individuais.',
     'Epidemiologista brasileira: desigualdades sociais moldam adoecimento, acesso e condições de saúde; renda, classe, gênero e raça influenciam oportunidades de viver com saúde.',
     'Use para deslocar a explicação da saúde do comportamento individual para condições sociais e estruturais.',
     'Os estudos de Rita Barradas Barata permitem compreender que diferenças de saúde entre grupos sociais não se explicam apenas por escolhas pessoais, pois renda, território e relações raciais também condicionam exposição a riscos e acesso ao cuidado.',
     'Não afirmar que desigualdade social determina automaticamente a doença de cada indivíduo.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     4, 5, 4, 5, 5, 5,
     '[]'::jsonb, '["EXPLAIN_CAUSE", "SHOW_INEQUALITY", "DEFINE_CONCEPT"]'::jsonb,
     '["desigualdade estrutural", "barreiras de acesso", "determinantes sociais"]'::jsonb,
     '["desigualdade no acesso à saúde", "saúde da população negra", "desigualdade territorial", "prevenção"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-026', 'PERSON', 'REPERTOIRE', 'Jairnilson Paim e Naomar de Almeida Filho', NULL,
     'PESQUISADORES / SANITARISTAS', 'Saúde coletiva', 'Brasil',
     'Autores centrais na formulação conceitual da saúde coletiva brasileira. A abordagem considera o processo saúde-doença-cuidado em sua historicidade e integralidade, articulando dimensões sociais, políticas e institucionais.',
     'Saúde coletiva não é apenas assistência médica: analisa saúde-doença-cuidado como fenômeno histórico e social, ligado a políticas, condições de vida e organização do sistema.',
     'Use quando o tema exigir mostrar que saúde pública depende de condições sociais e organização coletiva do cuidado.',
     'A perspectiva da saúde coletiva desenvolvida por Paim e Almeida Filho ajuda a superar uma visão exclusivamente biomédica, pois entende o cuidado como fenômeno também social, histórico e político.',
     'Não atribuir aos autores a ideia de que fatores biológicos são irrelevantes.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     4, 5, 3, 4, 5, 5,
     '[]'::jsonb, '["DEFINE_CONCEPT", "EXPLAIN_CAUSE", "HISTORICAL_CONTEXT"]'::jsonb,
     '["necessidade de políticas públicas integradas", "limites do modelo puramente biomédico"]'::jsonb,
     '["SUS", "acesso à saúde", "prevenção", "determinantes sociais", "políticas de saúde"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-052', 'PERSON', 'REPERTOIRE', 'Nise da Silveira', NULL,
     'PSIQUIATRA / PESQUISADORA', 'Saúde mental, terapêutica ocupacional e cuidado humanizado', 'Brasil',
     'Nise se opôs a práticas psiquiátricas agressivas e desenvolveu formas de cuidado baseadas em atividades expressivas, convivência e portas abertas, criando a Seção de Terapêutica Ocupacional, o Museu de Imagens do Inconsciente e a Casa das Palmeiras.',
     'Nise da Silveira é referência brasileira de cuidado psiquiátrico humanizado: combateu práticas agressivas e valorizou expressão, vínculo e reabilitação em liberdade.',
     'Use para contextualizar historicamente alternativas ao modelo asilar e mostrar que cuidado em saúde mental pode preservar expressão e autonomia.',
     'A trajetória de Nise da Silveira mostra que o tratamento psiquiátrico pode ser reorganizado a partir de vínculo, expressão e respeito à pessoa, em oposição a práticas institucionalizantes e violentas.',
     'Não atribuir a Nise toda a Reforma Psiquiátrica brasileira nem dizer que arte, isoladamente, substitui qualquer tratamento clínico.',
     'MEDIUM', 'BASIC', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["HISTORICAL_CONTEXT", "EXAMPLE", "SUPPORT_INTERVENTION"]'::jsonb,
     '[]'::jsonb,
     '["institucionalização", "estigma", "cuidado em liberdade", "tratamento desumanizado"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-053', 'PERSON', 'REPERTOIRE', 'Paulo Amarante', NULL,
     'PSIQUIATRA / PESQUISADOR', 'Reforma psiquiátrica, saúde mental e atenção psicossocial', 'Brasil',
     'Pioneiro da Reforma Psiquiátrica brasileira, diferencia desinstitucionalização de mera desospitalização e enfatiza direitos, território, participação social e cuidado em liberdade.',
     'Paulo Amarante: reforma psiquiátrica não é só fechar hospitais; envolve criar outro modelo de cuidado, com direitos, território, serviços comunitários e participação social.',
     'Use para explicar a transformação do modelo de cuidado e seus desafios atuais.',
     'A noção de desinstitucionalização trabalhada por Paulo Amarante mostra que reformar a assistência psiquiátrica exige mais do que reduzir internações: é preciso construir redes territoriais de cuidado e reconhecimento de direitos.',
     'Não confundir desinstitucionalização com abandono de pessoas ou ausência de cuidado intensivo quando necessário.',
     'HIGH', 'INTERMEDIATE', 'LOW',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["DEFINE_CONCEPT", "HISTORICAL_CONTEXT", "SUPPORT_INTERVENTION"]'::jsonb,
     '[]'::jsonb,
     '["RAPS", "institucionalização", "estigma", "cuidado em liberdade", "políticas de saúde mental"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    -- Educação
    ('REP-027', 'PERSON', 'REPERTOIRE', 'Magda Soares', NULL,
     'EDUCADORA / PESQUISADORA', 'Alfabetização, leitura e letramento', 'Brasil',
     'Distinguiu e articulou alfabetização e letramento: dominar o sistema de escrita não equivale, por si só, a participar competentemente das práticas sociais de leitura e escrita.',
     'Alfabetizar é ensinar o sistema de escrita; letrar envolve usar leitura e escrita em práticas sociais. Uma política educacional de qualidade precisa articular as duas dimensões.',
     'Use para discutir por que acesso formal à escola ou decodificação de palavras não garantem plena participação na cultura escrita.',
     'A distinção proposta por Magda Soares entre alfabetização e letramento mostra que aprender o código escrito não basta quando o estudante não desenvolve condições de utilizar leitura e escrita em situações sociais concretas.',
     'Não tratar alfabetização e letramento como opostos; na autora, são processos distintos, porém articulados.',
     'MEDIUM', 'BASIC', 'MEDIUM',
     4, 5, 5, 5, 5, 4,
     '[]'::jsonb, '["DEFINE_CONCEPT", "EXPLAIN_CAUSE", "SUPPORT_INTERVENTION"]'::jsonb,
     '["falha educacional", "ensino descontextualizado", "formação docente"]'::jsonb,
     '["alfabetização", "analfabetismo funcional", "desigualdade educacional", "formação docente"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-028', 'PERSON', 'REPERTOIRE', 'Miguel Arroyo', NULL,
     'EDUCADOR / PESQUISADOR', 'Direito à educação, sujeitos populares e movimentos sociais', 'Brasil',
     'Sua produção enfatiza que grupos historicamente excluídos são sujeitos de direitos e de conhecimentos; a escola deve reconhecer trajetórias, identidades e saberes dos setores populares.',
     'Arroyo ajuda a pensar educação como direito de sujeitos historicamente excluídos, e não como favor ou simples adaptação desses grupos a um padrão escolar dominante.',
     'Use em temas de acesso/permanência quando o problema envolve grupos cujas experiências são pouco reconhecidas pela escola.',
     'A perspectiva de Miguel Arroyo evidencia que assegurar o direito à educação também exige reconhecer como legítimos os saberes e trajetórias de grupos historicamente afastados da escola.',
     'Não reduzir sua obra a uma defesa genérica de inclusão sem mencionar sujeitos, direitos e reconhecimento.',
     'MEDIUM', 'INTERMEDIATE', 'LOW',
     4, 5, 4, 4, 4, 5,
     '[]'::jsonb, '["SOCIAL_CRITIQUE", "SHOW_INVISIBILITY", "SUPPORT_INTERVENTION"]'::jsonb,
     '["invisibilidade de sujeitos", "barreiras institucionais", "reconhecimento de saberes"]'::jsonb,
     '["EJA", "educação do campo", "desigualdade educacional", "exclusão", "povos tradicionais"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-042', 'PERSON', 'REPERTOIRE', 'Pierre Bourdieu', 'capital cultural e reprodução das desigualdades',
     'SOCIÓLOGO / CONCEITO', 'Educação e desigualdade social', 'França',
     'O conceito de capital cultural ajuda a explicar por que recursos culturais valorizados pela escola são distribuídos de maneira desigual entre famílias e classes sociais, influenciando trajetórias escolares.',
     'A escola não recebe alunos com os mesmos recursos culturais prévios; quando trata como natural a cultura dos grupos privilegiados, pode transformar desigualdades sociais em desigualdades escolares.',
     'Use para contestar explicações que atribuem sucesso escolar somente ao esforço individual.',
     'O conceito de capital cultural de Bourdieu ajuda a compreender por que estudantes chegam à escola com recursos desigualmente valorizados, o que torna insuficiente explicar resultados educacionais apenas pelo mérito individual.',
     'Não afirmar que a escola sempre reproduz mecanicamente a estrutura social ou que mobilidade é impossível.',
     'HIGH', 'ADVANCED', 'HIGH',
     5, 5, 3, 5, 5, 2,
     '[]'::jsonb, '["EXPLAIN_CAUSE", "SHOW_INEQUALITY", "DEFINE_CONCEPT"]'::jsonb,
     '["reprodução de desigualdades", "capital cultural", "meritocracia"]'::jsonb,
     '["desigualdade educacional", "acesso ao ensino superior", "sucesso/fracasso escolar"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-043', 'PERSON', 'REPERTOIRE', 'Paulo Freire', 'educação dialógica e autonomia',
     'EDUCADOR / PENSADOR', 'Educação', 'Brasil',
     'A educação deve formar sujeitos capazes de compreender criticamente a realidade e participar do processo educativo, em oposição a práticas meramente transmissivas.',
     'Freire é mais útil quando ligado a autonomia, diálogo, leitura crítica do mundo e participação do estudante — não como citação genérica sobre educação.',
     'Use quando a proposta argumentativa envolve formação crítica e participação ativa do estudante.',
     'A pedagogia de Paulo Freire contribui para compreender que a educação cidadã não se limita à transmissão de conteúdos, pois exige diálogo e participação do estudante na construção do conhecimento.',
     'Não atribuir frases populares sem fonte; evitar ''educação muda o mundo'' como citação automática.',
     'HIGH', 'INTERMEDIATE', 'HIGH',
     5, 5, 4, 5, 4, 1,
     '[]'::jsonb, '["DEFINE_CONCEPT", "SUPPORT_INTERVENTION", "SOCIAL_CRITIQUE"]'::jsonb,
     '["falha educacional", "ausência de participação", "educação emancipatória"]'::jsonb,
     '["formação cidadã", "alfabetização", "educação de adultos", "ensino autoritário"]'::jsonb,
     'VERIFIED', DATE '2026-10-07', TRUE),
    ('REP-062', 'CULTURAL_WORK', 'REPERTOIRE', 'Central do Brasil', 'Walter Salles',
     'FILME', 'Analfabetismo, letramento e cidadania', 'Brasil',
     'Dora escreve cartas para pessoas analfabetas na estação Central do Brasil, tornando visível como a ausência de domínio da escrita pode criar dependência de intermediários para comunicação e exercício cotidiano da cidadania.',
     'A cena das cartas permite relacionar analfabetismo a autonomia e cidadania: quem não domina a escrita pode depender de terceiros para registrar voz, vínculos e direitos.',
     'Use junto a Magda Soares para transformar alfabetização em questão de participação social, não apenas decodificação.',
     'Em Central do Brasil, a dependência de pessoas analfabetas de uma intermediária para escrever cartas evidencia como a exclusão da cultura escrita pode restringir autonomia e participação social.',
     'Não usar o filme como dado sobre a taxa atual de analfabetismo; ele é repertório cultural.',
     'LOW', 'BASIC', 'MEDIUM',
     NULL, NULL, NULL, NULL, NULL, NULL,
     '[]'::jsonb, '["EXAMPLE", "SHOW_INEQUALITY", "SOCIAL_CRITIQUE"]'::jsonb,
     '[]'::jsonb,
     '["analfabetismo", "letramento", "cidadania", "desigualdade regional"]'::jsonb,
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
    'REP-025', 'REP-026', 'REP-052', 'REP-053', 'REP-027',
    'REP-028', 'REP-042', 'REP-043', 'REP-062'
));

WITH fontes (codigo, tipo, descricao, url) AS (
    VALUES
    ('REP-025', 'GENERAL', 'Fiocruz/Editora Fiocruz — Como e por que as desigualdades sociais fazem mal à saúde (2009)',
     'https://arca.fiocruz.br/items/7be8d6e6-0126-46c4-b09b-5e5e1383ef47'),
    ('REP-026', 'GENERAL', 'Revista de Saúde Pública — Saúde coletiva: uma nova saúde pública ou campo aberto a novos paradigmas?',
     'https://revistas.usp.br/rsp/pt_BR/article/view/24383'),
    ('REP-052', 'PROFILE', 'Museu de Imagens do Inconsciente — Nise da Silveira',
     'https://museuimagensdoinconsciente.org.br/nise-da-silveira/'),
    ('REP-052', 'CONCEPT', 'Estudos Avançados/SciELO — Nise da Silveira: imagens do inconsciente entre psicologia, arte e política',
     'https://www.scielo.br/j/ea/a/DXNtq8VnSpjxsh5YvgYX8qM/'),
    ('REP-053', 'PROFILE', 'CEE-Fiocruz — perfil de Paulo Amarante',
     'https://cee.fiocruz.br/pesquisador-do-cee-fiocruz-e-convidado-a-integrar-grupo-de-trabalho-da-18a-conferencia-nacional-de-saude/'),
    ('REP-053', 'CONCEPT', 'Cadernos de Saúde Pública — Novos sujeitos, novos direitos',
     'https://cadernos.ensp.fiocruz.br/ojs/index.php/csp/article/view/677'),
    ('REP-027', 'GENERAL', 'CEALE/UFMG — Magda Soares',
     'https://ceale.fae.ufmg.br/magda-soares/'),
    ('REP-028', 'GENERAL', 'UFMG — Miguel Arroyo fala sobre o direito à educação',
     'https://www3.ufmg.br/comunicacao/noticias/90-anos-de-historias-direito-a-educacao'),
    ('REP-042', 'GENERAL', 'FFLCH-USP — A Reprodução das diferenças sociais na perspectiva de Pierre Bourdieu',
     'https://ensinosociologia.fflch.usp.br/reproducao-das-diferencas-sociais-na-perspectiva-de-pierre-bourdieu'),
    ('REP-043', 'GENERAL', 'Revista de Estudos Culturais/USP — leitura de Pedagogia da Autonomia',
     'https://revistas.usp.br/revistaec/pt_BR/article/view/170632'),
    ('REP-062', 'CONCEPT', 'Instituto Moreira Salles — Central do Brasil',
     'https://ims.com.br/filme/central-do-brasil/')
)
INSERT INTO repertorio_fontes (repertorio_id, tipo, descricao, url)
SELECT r.id, f.tipo, f.descricao, f.url
FROM fontes f
JOIN repertorios r ON r.codigo = f.codigo;

-- 3) Problemas sociais de cada repertório
DELETE FROM repertorio_problemas
WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo IN (
    'REP-025', 'REP-026', 'REP-052', 'REP-053', 'REP-027',
    'REP-028', 'REP-042', 'REP-043', 'REP-062'
));

WITH vinculos (codigo, problema_slug) AS (
    VALUES
    ('REP-025', 'desigualdade-saude'), ('REP-025', 'sus-acesso-saude'), ('REP-025', 'racismo'), ('REP-025', 'segregacao-urbana'), ('REP-025', 'prevencao-saude'),
    ('REP-026', 'sus-acesso-saude'), ('REP-026', 'prevencao-saude'), ('REP-026', 'desigualdade-saude'),
    ('REP-052', 'saude-mental'), ('REP-052', 'violencia-institucional'), ('REP-052', 'discriminacao'),
    ('REP-053', 'saude-mental'), ('REP-053', 'violencia-institucional'), ('REP-053', 'discriminacao'),
    ('REP-027', 'alfabetizacao'), ('REP-027', 'inclusao-educacional'),
    ('REP-028', 'eja'), ('REP-028', 'inclusao-educacional'), ('REP-028', 'direitos-indigenas'),
    ('REP-042', 'inclusao-educacional'), ('REP-042', 'evasao-escolar'),
    ('REP-043', 'participacao-juventude'), ('REP-043', 'alfabetizacao'), ('REP-043', 'eja'), ('REP-043', 'inclusao-educacional'),
    ('REP-062', 'alfabetizacao'), ('REP-062', 'eja')
)
INSERT INTO repertorio_problemas (repertorio_id, problema_id)
SELECT r.id, p.id
FROM vinculos v
JOIN repertorios r ON r.codigo = v.codigo
JOIN problemas_sociais p ON p.slug = v.problema_slug;

-- Conferência (rodar à parte, depois de subir o app):
-- SELECT count(*) FROM repertorios WHERE codigo IN ('REP-025', 'REP-026', 'REP-052', 'REP-053', 'REP-027', 'REP-028', 'REP-042', 'REP-043', 'REP-062');  -- 9
-- SELECT count(*) FROM repertorio_fontes f JOIN repertorios r ON r.id = f.repertorio_id WHERE r.codigo IN ('REP-025', 'REP-026', 'REP-052', 'REP-053', 'REP-027', 'REP-028', 'REP-042', 'REP-043', 'REP-062');  -- 11
-- SELECT count(*) FROM repertorio_problemas rp JOIN repertorios r ON r.id = rp.repertorio_id WHERE r.codigo IN ('REP-025', 'REP-026', 'REP-052', 'REP-053', 'REP-027', 'REP-028', 'REP-042', 'REP-043', 'REP-062');  -- 27
