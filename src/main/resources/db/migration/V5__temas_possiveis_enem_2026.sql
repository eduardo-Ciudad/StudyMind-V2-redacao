-- Passo 14: temas possíveis do ENEM 2026
-- Fonte: pesquisa investigativa sobre possíveis temas (out/2026). A força indica evidência relativa, não chance de cair.

-- 1) Nova origem de tema
ALTER TABLE temas DROP CONSTRAINT ck_temas_origem;
ALTER TABLE temas
    ADD CONSTRAINT ck_temas_origem CHECK (origem IN ('ENEM_OFICIAL', 'AUTORAL', 'PREVISAO'));

-- 2) Material de estudo de cada tema possível
CREATE TABLE temas_previsao (
    tema_id UUID PRIMARY KEY REFERENCES temas(id) ON DELETE CASCADE,
    ranking SMALLINT NOT NULL CHECK (ranking > 0),
    forca VARCHAR(20) NOT NULL,
    eixo VARCHAR(120) NOT NULL,
    grupo_social VARCHAR(255) NOT NULL,
    justificativa TEXT NOT NULL,
    argumentos JSONB NOT NULL DEFAULT '[]'::jsonb,
    marcos_legais JSONB NOT NULL DEFAULT '[]'::jsonb,
    agentes_intervencao JSONB NOT NULL DEFAULT '[]'::jsonb,
    CONSTRAINT ck_temas_previsao_forca CHECK (forca IN ('MUITO_FORTE', 'FORTE', 'VALE_TREINAR'))
);

-- 3) Temas possíveis que cada aluno adicionou à própria lista
CREATE TABLE usuario_temas (
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    tema_id UUID NOT NULL REFERENCES temas(id) ON DELETE CASCADE,
    adicionado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (usuario_id, tema_id)
);

-- 4) Seed dos 12 temas da pesquisa
WITH dados (ranking, titulo, forca, eixo, grupo_social, justificativa, argumentos, marcos_legais, agentes_intervencao) AS (
    VALUES
    (1, 'Desafios para a garantia dos direitos de crianças e adolescentes nos ambientes digitais brasileiros', 'MUITO_FORTE', 'Infância, direitos e tecnologia', 'Crianças e adolescentes',
     'ECA Digital sancionado em 2025 e em vigor desde março de 2026; agenda regulatória, campanhas públicas e possibilidade clara de intervenção.',
     '["Responsabilidade das plataformas", "Vulnerabilidade algorítmica", "Educação digital", "Proteção de dados", "Publicidade", "Exploração sexual", "Saúde e bem-estar"]'::jsonb,
     '["Constituição Federal, art. 227", "ECA", "Marco Civil da Internet", "LGPD", "Lei 15.211/2025 (ECA Digital)"]'::jsonb,
     '["União", "ANPD", "MEC", "Escolas", "Plataformas", "Famílias"]'::jsonb),
    (2, 'Desafios para a superação do analfabetismo entre jovens e adultos no Brasil', 'MUITO_FORTE', 'Educação e cidadania', 'Jovens, adultos e idosos historicamente excluídos da escolarização',
     '8,4 milhões de pessoas analfabetas em 2025; agenda federal específica via Pacto EJA; forte desigualdade regional e social.',
     '["Direito à educação", "Exclusão histórica", "Desigualdades territoriais e raciais", "Trabalho", "Acesso a serviços e participação cidadã"]'::jsonb,
     '["Constituição Federal, arts. 205 e 208", "LDB", "Normas e políticas da EJA"]'::jsonb,
     '["MEC", "Estados", "Municípios", "Escolas", "Assistência social", "Sociedade civil"]'::jsonb),
    (3, 'Desafios para a efetivação da inclusão educacional das pessoas com deficiência no Brasil', 'MUITO_FORTE', 'Educação, inclusão e direitos', 'Pessoas com deficiência, TEA e outros públicos da educação especial',
     'Dados recentes do Censo; nova política de educação especial inclusiva; alta densidade documental e possibilidade de intervenção.',
     '["Capacitismo", "Barreiras físicas e atitudinais", "Formação docente", "Acessibilidade", "Tecnologia assistiva", "Permanência e aprendizagem"]'::jsonb,
     '["Constituição Federal", "Lei Brasileira de Inclusão", "Política Nacional de Educação Especial Inclusiva"]'::jsonb,
     '["MEC", "Redes de ensino", "Escolas", "Municípios", "Universidades", "Famílias"]'::jsonb),
    (4, 'Desafios para a identificação e inclusão de estudantes com altas habilidades ou superdotação na educação brasileira', 'MUITO_FORTE', 'Educação e inclusão', 'Estudantes com altas habilidades ou superdotação',
     'Lei 15.436/2026 criou política e cadastro nacional; recorte pouco midiático e fortemente institucionalizado.',
     '["Subidentificação", "Falta de formação docente", "Estereótipos", "Ausência de atendimento especializado", "Desigualdade regional"]'::jsonb,
     '["Constituição Federal", "LDB", "Lei 15.436/2026", "Política Nacional de Educação Especial Inclusiva"]'::jsonb,
     '["MEC", "Secretarias de educação", "Escolas", "Centros de atendimento especializado"]'::jsonb),
    (5, 'Desafios para a garantia do direito à vida da juventude brasileira', 'FORTE', 'Juventude, violência e cidadania', 'Jovens, especialmente os socialmente vulneráveis',
     'Atlas da Violência 2026 fornece base documental nacional e evidencia persistência de mortalidade violenta entre jovens.',
     '["Desigualdade territorial", "Evasão escolar", "Exclusão social", "Prevenção da violência", "Acesso a cultura, esporte e trabalho"]'::jsonb,
     '["Constituição Federal", "Estatuto da Juventude", "Legislação de proteção à vida e segurança pública"]'::jsonb,
     '["União", "Estados", "Municípios", "Escolas", "Segurança pública", "Assistência social"]'::jsonb),
    (6, 'Desafios para a garantia de proteção social aos trabalhadores de plataformas digitais no Brasil', 'FORTE', 'Trabalho e tecnologia', 'Trabalhadores de aplicativos e plataformas',
     'Pesquisa específica do IBGE em 2026; dados sobre jornada, renda, informalidade e previdência; intervenção regulatória possível.',
     '["Plataformização", "Informalidade", "Assimetria entre trabalhador e plataforma", "Previdência", "Jornada", "Renda"]'::jsonb,
     '["Constituição Federal, art. 7º", "CLT", "Legislação previdenciária", "Futura regulação do trabalho por plataformas"]'::jsonb,
     '["Congresso Nacional", "Ministério do Trabalho", "INSS", "Plataformas", "Organizações de trabalhadores"]'::jsonb),
    (7, 'Desafios para a efetivação da cidadania da população em situação de rua no Brasil', 'FORTE', 'Cidadania e direitos sociais', 'População em situação de rua',
     'Programas federais recentes, ações interministeriais e forte conexão com invisibilidade, serviços públicos e dignidade.',
     '["Moradia", "Documentação", "Saúde", "Trabalho", "Preconceito", "Arquitetura hostil", "Acesso a políticas públicas"]'::jsonb,
     '["Constituição Federal, art. 6º", "Políticas nacionais para a população em situação de rua", "Lei Padre Júlio Lancelotti"]'::jsonb,
     '["União", "Municípios", "Assistência social", "Saúde", "Habitação", "Organizações sociais"]'::jsonb),
    (8, 'Desafios para o enfrentamento da violência sexual contra crianças e adolescentes no Brasil', 'FORTE', 'Infância, violência e direitos', 'Crianças e adolescentes',
     'Diretrizes recentes de prevenção, legislação e forte conexão com ambiente digital e proteção integral.',
     '["Subnotificação", "Aliciamento", "Exploração digital", "Proteção familiar e escolar", "Responsabilização de redes e agressores"]'::jsonb,
     '["Constituição Federal, art. 227", "ECA", "Legislação penal", "ECA Digital"]'::jsonb,
     '["MDHC", "Escolas", "Saúde", "Segurança pública", "Plataformas", "Conselhos tutelares"]'::jsonb),
    (9, 'Desafios para reduzir a vulnerabilidade social diante de eventos climáticos extremos nas cidades brasileiras', 'VALE_TREINAR', 'Meio ambiente, cidades e desigualdade', 'Populações urbanas vulneráveis',
     'Plano Clima, Lei 14.904/2024 e AdaptaCidades fornecem forte base institucional. O recorte deve ser humano e territorial, não apenas ambiental.',
     '["Ocupação urbana", "Infraestrutura", "Desigualdade territorial", "Prevenção", "Defesa civil", "Racismo ambiental"]'::jsonb,
     '["Política Nacional sobre Mudança do Clima", "Lei 14.904/2024", "Estatuto da Cidade"]'::jsonb,
     '["MMA", "Ministério das Cidades", "Municípios", "Defesa civil", "Habitação", "Planejamento urbano"]'::jsonb),
    (10, 'Desafios para o enfrentamento de novas formas de violência contra as mulheres no Brasil', 'VALE_TREINAR', 'Gênero, direitos e violência', 'Mulheres',
     '2026 marca 20 anos da Lei Maria da Penha, com campanhas e agenda institucional; perde força pela proximidade com os temas de 2015 e 2023.',
     '["Violência doméstica, digital e patrimonial", "Rede de atendimento", "Prevenção", "Autonomia econômica"]'::jsonb,
     '["Lei Maria da Penha", "Constituição Federal", "Legislação de proteção às mulheres"]'::jsonb,
     '["Ministério das Mulheres", "Estados", "Municípios", "Judiciário", "Segurança pública", "Rede de atendimento"]'::jsonb),
    (11, 'Desafios para a preservação da integridade da informação diante de conteúdos sintéticos no Brasil', 'VALE_TREINAR', 'Tecnologia, informação e cidadania', 'Sociedade e usuários de ambientes digitais',
     'Alta relevância e forte presença na agenda pública, mas tecnologia já apareceu em 2011 e 2018 e o assunto costuma ser formulado de modo genérico.',
     '["Educação midiática", "Identificação de conteúdo sintético", "Responsabilidade das plataformas", "Democracia informacional"]'::jsonb,
     '["Marco Civil da Internet", "LGPD", "Normas eleitorais e regulatórias aplicáveis"]'::jsonb,
     '["MEC", "Plataformas", "Imprensa", "Justiça Eleitoral", "Sociedade civil"]'::jsonb),
    (12, 'Desafios para a garantia do direito à cidade às populações vulneráveis no Brasil', 'VALE_TREINAR', 'Cidades e direitos sociais', 'Populações de baixa renda e em situação de rua',
     'Recorte compatível com cidadania e invisibilidade; menos sinais institucionais novos em 2026 que os temas acima.',
     '["Déficit habitacional", "Segregação urbana", "Arquitetura hostil", "Acesso a serviços", "Planejamento urbano"]'::jsonb,
     '["Constituição Federal, art. 6º", "Estatuto da Cidade", "Legislação sobre arquitetura hostil"]'::jsonb,
     '["Ministério das Cidades", "Municípios", "Habitação", "Urbanismo", "Assistência social"]'::jsonb)
),
novos AS (
    INSERT INTO temas (titulo, origem, ano)
    SELECT titulo, 'PREVISAO', 2026 FROM dados
    RETURNING id, titulo
)
INSERT INTO temas_previsao (tema_id, ranking, forca, eixo, grupo_social, justificativa, argumentos, marcos_legais, agentes_intervencao)
SELECT n.id, d.ranking, d.forca, d.eixo, d.grupo_social, d.justificativa, d.argumentos, d.marcos_legais, d.agentes_intervencao
FROM novos n
JOIN dados d ON d.titulo = n.titulo;
