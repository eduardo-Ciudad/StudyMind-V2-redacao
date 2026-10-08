# Repertório — curadoria da pesquisa V3

Fonte: *StudyMind_Repertorios_ENEM_V3_Correcao_Pontual_Lacunas.docx* (verificada em 2026-10-07).
Este arquivo resolve a issue #4 (conversão da pesquisa + gerador) e destrava a #5 (seed).

## O que tem no `curadoria-v3.json`

| Bloco | Qtde | Observação |
|---|---|---|
| `macroThemes` | 13 | 12 da taxonomia (seção 3) + Povos indígenas (bloco criado na V3) |
| `socialProblems` | 52 | subeixos da taxonomia, desambiguados; 5 acrescentados na conversão |
| `repertoires` | 87 | 63 ativos: 38 REP completos, 13 EVD, 12 CF |
| ↳ REP-001 a 024 | 24 | só existem resumidos na V2/V3 → `active: false`, `summaryOnly: true` |
| `packages` | 12 | PAC-01 a 10 com os ajustes da seção 20 + 2 pacotes novos (PAC-11/12) |

Cada repertório guarda os termos livres da pesquisa em `tags` (para busca) e os
problemas canônicos em `socialProblems` (para filtro e para cruzar com os temas).

## Pipeline

```bash
pip install -r tools/repertorio/requirements.txt

# 1. docx -> JSON bruto (fiel ao documento)
python tools/repertorio/extrair_v3.py pesquisa-v3.docx bruto.json

# 2. bruto -> curadoria (mapeia problemas, resolve pacotes, junta CF/EVD)
python tools/repertorio/montar_curadoria.py bruto.json docs/repertorio/curadoria-v3.json

# 3. valida e gera a migration
python tools/repertorio/gerar_seed.py docs/repertorio/curadoria-v3.json --so-validar
python tools/repertorio/gerar_seed.py docs/repertorio/curadoria-v3.json --versao 7 \
       --saida src/main/resources/db/migration
```

O seed faz upsert por `slug`/`codigo`: numa V4 da pesquisa, basta regerar uma
migration nova. O mapa de termos → problemas fica em `tools/repertorio/taxonomia.py`.

`referencia/` tem o schema usado no teste (rascunho da issue #2) e o seed gerado.
Testado num PostgreSQL 16 sobre as migrations V1–V5 reais do projeto, rodando o
seed duas vezes (idempotente).

## Precisa da sua aprovação

1. **Acrescentado na conversão** (não está na tabela da seção 3):
   macroeixo *Povos indígenas e comunidades tradicionais* com os problemas
   `territorio-indigena`, `cultura-memoria-indigena`, `direitos-indigenas`, `saude-indigena`;
   e `meio-ambiente` (resíduos/saneamento) em *Cidades e clima*.
2. **Códigos PAC-11 e PAC-12**: a seção 20 cria dois pacotes sem código
   (saúde mental; povos indígenas). Os códigos são propostos aqui.
3. **Mapa de termos** em `taxonomia.py`: 174 termos livres viraram 52 problemas.
   Termos genéricos ("cultura", "território", "trabalho", "saúde") ficam só como tag.

## Lacunas da pesquisa que apareceram na conversão

- **EVD-006 a EVD-013 sem URL**: a seção 18 lista a fonte só pelo nome.
- **REP-048 a REP-062 sem pontuação da rubrica** (15 registros): o campo não veio na V3.
  As notas ficam `null`; a ordenação precisa tratar isso.
- **Seis problemas ainda sem nenhum repertório**: inclusão digital, neurodiversidade
  na escola, perspectiva de futuro, juventude e trabalho, PcD e trabalho, violência
  contra a juventude.
- Pacotes que apontam para registros inativos (REP-017 Vidas Secas, REP-020 Bicho de
  Sete Cabeças): o front deve mostrar o texto do pacote mesmo sem abrir o detalhe.

## Diferenças em relação ao texto das issues #1 e #2

- Notas da rubrica **nullable** (motivo acima).
- `tipos_evidencia` é lista: a pesquisa combina tipos (ex.: `NATIONAL_SURVEY / OFFICIAL_STATISTICS`)
  e usa 14 valores, não 7.
- `grupos_sociais` e `direitos` não vêm por registro (só na taxonomia), então saem do
  repertório; os termos livres viram `tags`.
