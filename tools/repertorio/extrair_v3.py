"""Extrai a pesquisa StudyMind Repertórios ENEM V3 (.docx) para JSON bruto.

Uso: python extrair_v3.py pesquisa.docx saida-bruta.json
Saída "bruta" = fiel ao documento. O mapeamento de problemas sociais e os
ajustes editoriais ficam em montar_curadoria.py.
"""
import json, re, sys
import docx
from docx.table import Table
from docx.text.paragraph import Paragraph

SEP = re.compile(r"\s+[—–-]\s+")


def blocos(d):
    """Percorre o corpo em ordem, devolvendo ('h1'|'h2'|'h3'|'p'|'table', conteúdo)."""
    for el in d.element.body.iterchildren():
        tag = el.tag.split("}")[1]
        if tag == "tbl":
            t = Table(el, d)
            yield "table", [[c.text.strip() for c in r.cells] for r in t.rows]
        elif tag == "p":
            p = Paragraph(el, d)
            txt = p.text.strip()
            if not txt:
                continue
            st = p.style.name
            if st == "Heading 1": yield "h1", txt
            elif st == "Heading 2": yield "h2", txt
            elif st == "Heading 3": yield "h3", txt
            else: yield "p", txt


def lista(v):
    return [x.strip() for x in re.split(r";", v or "") if x.strip()]


def secao_num(h1):
    m = re.match(r"(\d+)\.", h1)
    return int(m.group(1)) if m else None


ROTULOS = {
    "Status": "changeStatus", "Tipo": "typeLabel", "Role": "role", "entityType": "entityType",
    "País/origem": "country", "Área": "field", "Lacuna que resolve": "gapResolved",
    "Ideia central": "centralIdea", "Lembre na prova": "rememberForExam",
    "Problemas relacionados": "socialProblemsRaw", "Funções argumentativas": "argumentativeFunctions",
    "Tipos de argumento": "argumentTypes", "Como usar": "howToUse",
    "Exemplo de aplicação": "applicationExample", "Erro comum de uso": "commonMisuse",
    "Risco de uso incorreto": "misuseRisk", "Dificuldade": "difficulty", "Pontuação": "scoresRaw",
    "Saturação": "saturation", "Verificação": "verification", "profileSource": "profileSource",
    "conceptSource": "conceptSource", "conceptRelations": "conceptRelations",
}


def parse_registro(titulo, paras):
    codigo, resto = titulo.split(" ", 1)
    partes = SEP.split(resto.lstrip("—– ").strip())
    r = {"code": codigo, "name": partes[0].strip(), "headingSubtitle": " — ".join(partes[1:]) or None,
         "summaryOnly": False, "sources": []}
    for p in paras:
        m = re.match(r"^([^:]{2,30}):\s*(.*)$", p, re.S)
        if m and m.group(1).strip() in ROTULOS:
            r[ROTULOS[m.group(1).strip()]] = m.group(2).strip()
        elif p.startswith("Fonte:"):
            r["sources"].append({"kind": "GENERAL", "text": p[len("Fonte:"):].strip()})
        else:
            r.setdefault("unparsed", []).append(p)
    # normalizações
    for k in ("socialProblemsRaw",):
        r["socialProblems"] = lista(r.pop(k, ""))
    r["argumentativeFunctions"] = lista(r.get("argumentativeFunctions"))
    r["argumentTypes"] = lista(r.get("argumentTypes"))
    sc = r.pop("scoresRaw", None)
    if sc:
        d = dict(re.findall(r"(Ap|V|A|C|E|O)\s*(\d)", sc))
        r["scores"] = {"versatility": int(d["V"]), "authority": int(d["A"]), "comprehension": int(d["C"]),
                       "applicability": int(d["Ap"]), "specificity": int(d["E"]), "originality": int(d["O"])}
    else:
        r["scores"] = None
    v = r.pop("verification", "")
    m = re.match(r"([A-Z]+)\s*[—–-]+\s*(\d{4}-\d{2}-\d{2})", v)
    r["verificationStatus"], r["verificationDate"] = (m.group(1), m.group(2)) if m else (v or None, None)
    for k, kind in (("profileSource", "PROFILE"), ("conceptSource", "CONCEPT")):
        val = r.pop(k, None)
        if val and val.strip("—– "):
            r["sources"].append({"kind": kind, "text": val})
    for s in r["sources"]:
        urls = re.findall(r"https?://\S+", s["text"])
        s["url"] = urls[0].rstrip(".);,") if urls else None
        s["description"] = re.sub(r":?\s*https?://\S+", "", s.pop("text")).strip(" :—–")
    return r


def main(src, dst):
    d = docx.Document(src)
    out = {"meta": {"source": src.split("/")[-1], "version": "V3"}, "taxonomy": [], "repertoires": [],
           "summaryRecords": [], "v2Expansions": [], "evidence": [], "evidenceSourcesNote": [],
           "constitution": [], "packages": [], "packagePatches": []}
    sec, atual, paras = None, None, []
    evd_fontes = {}

    def fecha():
        nonlocal atual, paras
        if atual and atual.startswith("REP-"):
            out["repertoires"].append(parse_registro(atual, paras))
        elif atual and atual.startswith("PAC-"):
            cod, resto = atual.split(" ", 1)
            pac = {"code": cod, "title": resto.lstrip("—– ").strip()}
            for p in paras:
                m = re.match(r"^([^:]{2,20}):\s*(.*)$", p, re.S)
                if m: pac[m.group(1).strip()] = m.group(2).strip()
            out["packages"].append(pac)
        atual, paras = None, []

    for kind, val in blocos(d):
        if kind == "h1":
            fecha(); sec = secao_num(val); continue
        if kind == "h2":
            fecha(); atual = val; continue
        if kind == "h3":
            continue
        if kind == "p":
            if atual: paras.append(val)
            elif sec == 7:
                m = re.match(r"(EVD-\d{3})\s*[—–-]+\s*(https?://\S+)", val)
                if m: evd_fontes[m.group(1)] = m.group(2)
            elif sec == 18 and val.startswith("Fontes auditadas"):
                out["evidenceSourcesNote"].append(val)
            continue
        rows = val[1:]
        if sec == 3:
            for mac, sub, causas, grupos, direitos in rows:
                out["taxonomy"].append({"macroTheme": mac, "subThemes": lista(sub), "causes": lista(causas),
                                        "socialGroups": lista(grupos), "rights": lista(direitos)})
        elif sec == 5:
            for cod, nome, st, role, func, mis in rows:
                out["summaryRecords"].append({"code": cod, "name": nome, "changeStatus": st, "role": role,
                                              "centralFunction": func, "commonMisuse": mis, "summaryOnly": True})
        elif sec == 7:
            for cod, nome, tipo, fonte, ano, concl, probs, lim in rows:
                out["evidence"].append({"code": cod, "name": nome, "evidenceTypes": [t.strip() for t in re.split(r"\s+/\s+", tipo) if t.strip()],
                                        "sourceName": fonte, "evidenceYearRaw": ano, "population": None,
                                        "centralIdea": concl, "socialProblems": lista(probs), "commonMisuse": lim,
                                        "updateNeeded": None})
        elif sec == 18:
            for cod, nome, tipo, fonte, ano, pop, achado, lim, probs, upd in rows:
                out["evidence"].append({"code": cod, "name": nome, "evidenceTypes": [t.strip() for t in re.split(r"\s+/\s+", tipo) if t.strip()],
                                        "sourceName": fonte, "evidenceYearRaw": ano, "population": pop,
                                        "centralIdea": achado, "socialProblems": lista(probs), "commonMisuse": lim,
                                        "updateNeeded": upd == "YES"})
        elif sec == 8:
            for cod, art, nucleo, probs, lim in rows:
                out["constitution"].append({"code": cod, "article": art, "centralIdea": nucleo,
                                            "socialProblems": lista(probs), "commonMisuse": lim, "changeStatus": "UNCHANGED"})
        elif sec == 19:
            for cod, art, nucleo, rel, st in rows:
                out["constitution"].append({"code": cod, "article": art, "centralIdea": nucleo,
                                            "socialProblems": lista(rel), "commonMisuse": None, "changeStatus": st})
        elif sec == 16:
            for reg, st, exp, mis in rows:
                out["v2Expansions"].append({"code": reg.split()[0], "changeStatus": st, "expansion": exp, "commonMisuse": mis})
        elif sec == 20:
            for pac, campo, comp, just in rows:
                out["packagePatches"].append({"package": pac, "field": campo, "component": comp, "justification": just})
    fecha()
    for e in out["evidence"]:
        e["url"] = evd_fontes.get(e["code"])
    json.dump(out, open(dst, "w", encoding="utf-8"), ensure_ascii=False, indent=2)
    print({k: len(v) for k, v in out.items() if isinstance(v, list)})


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
