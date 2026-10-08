"""Valida o curadoria-v3.json e gera a migration Flyway de seed do Repertório.

Uso:
  python gerar_seed.py curadoria-v3.json --versao 7 --saida ../src/main/resources/db/migration/
  python gerar_seed.py curadoria-v3.json --so-validar

- Idempotente: upsert por slug/código. Na próxima versão da pesquisa, basta
  atualizar o JSON e gerar uma nova migration — nada é duplicado.
- Os vínculos (fontes e problemas) de cada repertório são apagados e
  reinseridos, para refletir exatamente o JSON.
- Pacotes de argumentação ficam no JSON, mas não entram neste seed (fase 2).
"""
import argparse, json, re, sys
from datetime import date

ENUMS = {
    "entityType": {"PERSON", "CONCEPT", "CULTURAL_WORK", "LEGAL_SOURCE", "EVIDENCE", "INSTITUTION", "EVENT"},
    "role": {"REPERTOIRE", "EVIDENCE", "BOTH"},
    "misuseRisk": {"LOW", "MEDIUM", "HIGH", None},
    "saturation": {"LOW", "MEDIUM", "HIGH", None},
    "difficulty": {"BASIC", "INTERMEDIATE", "ADVANCED", None},
    "verificationStatus": {"VERIFIED", "PARTIAL", "REJECTED"},
}
EVIDENCE_TYPES = {"OFFICIAL_STATISTICS", "NATIONAL_SURVEY", "OFFICIAL_HEALTH_SURVEILLANCE", "ACADEMIC_RESEARCH",
                  "SCIENTIFIC_STUDY", "SCOPING_REVIEW", "SYSTEMATIC_REVIEW", "INTEGRATIVE_REVIEW", "INSTITUTIONAL_REPORT",
                  "INSTITUTIONAL_SUMMARY", "ADMINISTRATIVE_DATA", "TIME_SERIES", "CROSS_SECTIONAL", "CONTENT_ANALYSIS"}
FUNCOES = {"EXPLAIN_CAUSE", "EXPLAIN_CONSEQUENCE", "SHOW_INEQUALITY", "SHOW_INVISIBILITY", "DEFINE_CONCEPT",
           "SOCIAL_CRITIQUE", "HISTORICAL_CONTEXT", "PROVE_PROBLEM", "LEGAL_GAP", "COMPARE_REALITY", "EXAMPLE",
           "SUPPORT_INTERVENTION"}
CODIGO = re.compile(r"^(REP-\d{3}|EVD-\d{3}|CF-\d{3})$")
NOTAS = ("versatility", "authority", "comprehension", "applicability", "specificity", "originality")


def validar(d):
    erros, avisos = [], []
    macros = {m["slug"] for m in d["macroThemes"]}
    probs = {p["slug"] for p in d["socialProblems"]}
    if len(macros) != len(d["macroThemes"]): erros.append("slug de macroeixo duplicado")
    if len(probs) != len(d["socialProblems"]): erros.append("slug de problema duplicado")
    for p in d["socialProblems"]:
        if p["macroTheme"] not in macros: erros.append(f"problema {p['slug']}: macroeixo inexistente {p['macroTheme']}")
    usados, vistos = set(), set()
    for r in d["repertoires"]:
        c = r["code"]
        if not CODIGO.match(c): erros.append(f"{c}: código fora do padrão")
        if c in vistos: erros.append(f"{c}: código duplicado")
        vistos.add(c)
        for k, ok in ENUMS.items():
            if r.get(k) not in ok: erros.append(f"{c}: {k} inválido ({r.get(k)})")
        for f in r["argumentativeFunctions"]:
            if f not in FUNCOES: erros.append(f"{c}: função argumentativa inválida {f}")
        for t in r["evidenceTypes"]:
            if t not in EVIDENCE_TYPES: erros.append(f"{c}: tipo de evidência inválido {t}")
        if r["scores"]:
            for n in NOTAS:
                if not 1 <= r["scores"][n] <= 5: erros.append(f"{c}: nota {n} fora de 1–5")
        for s in r["socialProblems"]:
            if s not in probs: erros.append(f"{c}: problema inexistente {s}")
            usados.add(s)
        if not r["name"]: erros.append(f"{c}: sem nome")
        if r["active"]:
            if r["summaryOnly"]: erros.append(f"{c}: registro resumido não pode estar ativo")
            if r["verificationStatus"] == "REJECTED": erros.append(f"{c}: REJECTED não pode estar ativo")
            if not r["socialProblems"]: erros.append(f"{c}: ativo sem problema social")
            if not r["centralIdea"]: erros.append(f"{c}: ativo sem ideia central")
            if not r["sources"]: erros.append(f"{c}: ativo sem fonte")
            if r["entityType"] == "EVIDENCE":
                if not r["evidenceTypes"]: erros.append(f"{c}: evidência sem tipo")
                if not r.get("evidenceYear") and "contínua" not in (r.get("evidenceYearRaw") or ""):
                    erros.append(f"{c}: evidência sem ano")
                if not any(s.get("url") for s in r["sources"]): avisos.append(f"{c}: evidência sem URL de fonte")
            if r["entityType"] not in ("EVIDENCE", "LEGAL_SOURCE") and r["scores"] is None:
                avisos.append(f"{c}: sem pontuação da rubrica (a pesquisa não trouxe)")
            if r["entityType"] not in ("EVIDENCE", "LEGAL_SOURCE") and not r["applicationExample"]:
                erros.append(f"{c}: ativo sem exemplo de aplicação")
    codigos = vistos
    for p in d["packages"]:
        for slot, v in p["slots"].items():
            for c in v["repertoireCodes"]:
                if c not in codigos: erros.append(f"{p['code']}.{slot}: referencia {c} inexistente")
        for s in p["socialProblems"]:
            if s not in probs: erros.append(f"{p['code']}: problema inexistente {s}")
    sem_uso = sorted(probs - usados)
    if sem_uso: avisos.append(f"problemas sem nenhum repertório ainda: {', '.join(sem_uso)}")
    return erros, avisos


def q(v):
    if v is None: return "NULL"
    if isinstance(v, bool): return "TRUE" if v else "FALSE"
    if isinstance(v, int): return str(v)
    if isinstance(v, (list, dict)): return q(json.dumps(v, ensure_ascii=False)) + "::jsonb"
    return "'" + str(v).replace("'", "''") + "'"


def gerar_sql(d, versao):
    L = [f"-- V{versao}: seed do Repertório — pesquisa {d['meta']['researchVersion']} (verificada em {d['meta']['verifiedAt']})",
         f"-- Gerado por gerar_seed.py em {date.today().isoformat()} a partir de docs/repertorio/curadoria-v3.json. NÃO EDITAR À MÃO.",
         "", "-- 1) Macroeixos"]
    for m in d["macroThemes"]:
        L.append(f"INSERT INTO macroeixos (slug, nome, ordem) VALUES ({q(m['slug'])}, {q(m['name'])}, {m['order']})\n"
                 f"    ON CONFLICT (slug) DO UPDATE SET nome = EXCLUDED.nome, ordem = EXCLUDED.ordem;")
    L += ["", "-- 2) Problemas sociais"]
    for p in d["socialProblems"]:
        L.append(f"INSERT INTO problemas_sociais (macroeixo_id, slug, nome)\n"
                 f"    SELECT id, {q(p['slug'])}, {q(p['name'])} FROM macroeixos WHERE slug = {q(p['macroTheme'])}\n"
                 f"    ON CONFLICT (slug) DO UPDATE SET nome = EXCLUDED.nome, macroeixo_id = EXCLUDED.macroeixo_id;")
    L += ["", "-- 3) Repertórios (inclui evidências e artigos da Constituição)"]
    cols = ("codigo", "tipo_entidade", "papel", "tipos_evidencia", "nome", "subtitulo", "tipo_descricao", "area", "pais",
            "ideia_central", "lembre_na_prova", "como_usar", "exemplo_aplicacao", "erro_comum", "risco_uso", "dificuldade",
            "saturacao", "nota_versatilidade", "nota_autoridade", "nota_compreensao", "nota_aplicabilidade",
            "nota_especificidade", "nota_originalidade", "funcoes_argumentativas", "tipos_argumento", "tags", "populacao",
            "ano_evidencia", "status_verificacao", "verificado_em", "ativo")
    for r in d["repertoires"]:
        sc = r["scores"] or {}
        vals = (r["code"], r["entityType"], r["role"], r["evidenceTypes"], r["name"], r["headingSubtitle"], r["typeLabel"],
                r["field"], r["country"], r["centralIdea"], r["rememberForExam"], r["howToUse"], r["applicationExample"],
                r["commonMisuse"], r["misuseRisk"], r["difficulty"], r["saturation"], *[sc.get(n) for n in NOTAS],
                r["argumentativeFunctions"], r["argumentTypes"], r["tags"], r.get("population"), r.get("evidenceYear"),
                r["verificationStatus"], r["verificationDate"], r["active"])
        sets = ", ".join(f"{c} = EXCLUDED.{c}" for c in cols[1:])
        L.append(f"INSERT INTO repertorios ({', '.join(cols)})\nVALUES ({', '.join(q(v) for v in vals)})\n"
                 f"    ON CONFLICT (codigo) DO UPDATE SET {sets};")
    L += ["", "-- 4) Fontes e problemas de cada repertório (reflete exatamente o JSON)",
          "DELETE FROM repertorio_fontes WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo IN ("
          + ", ".join(q(r["code"]) for r in d["repertoires"]) + "));",
          "DELETE FROM repertorio_problemas WHERE repertorio_id IN (SELECT id FROM repertorios WHERE codigo IN ("
          + ", ".join(q(r["code"]) for r in d["repertoires"]) + "));"]
    for r in d["repertoires"]:
        for s in r["sources"]:
            L.append(f"INSERT INTO repertorio_fontes (repertorio_id, tipo, descricao, url) SELECT id, {q(s['kind'])}, "
                     f"{q(s['description'])}, {q(s['url'])} FROM repertorios WHERE codigo = {q(r['code'])};")
        if r["socialProblems"]:
            L.append(f"INSERT INTO repertorio_problemas (repertorio_id, problema_id) SELECT r.id, p.id FROM repertorios r, "
                     f"problemas_sociais p WHERE r.codigo = {q(r['code'])} AND p.slug IN ({', '.join(q(s) for s in r['socialProblems'])});")
    return "\n".join(L) + "\n"


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("json")
    ap.add_argument("--versao", type=int)
    ap.add_argument("--saida", default=".")
    ap.add_argument("--so-validar", action="store_true")
    a = ap.parse_args()
    d = json.load(open(a.json, encoding="utf-8"))
    erros, avisos = validar(d)
    for x in avisos: print("AVISO", x)
    for x in erros: print("ERRO ", x)
    if erros: sys.exit(f"{len(erros)} erro(s) — seed não gerado.")
    print(f"OK: {len(d['repertoires'])} registros ({sum(r['active'] for r in d['repertoires'])} ativos), "
          f"{len(d['socialProblems'])} problemas, {len(d['macroThemes'])} macroeixos.")
    if a.so_validar: return
    if not a.versao: sys.exit("informe --versao (número da próxima migration)")
    nome = f"{a.saida.rstrip('/')}/V{a.versao}__seed_repertorio_pesquisa_v3.sql"
    open(nome, "w", encoding="utf-8").write(gerar_sql(d, a.versao))
    print("gerado:", nome)


if __name__ == "__main__":
    main()
