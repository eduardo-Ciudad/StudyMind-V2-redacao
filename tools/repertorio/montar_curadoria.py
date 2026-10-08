"""Monta docs/repertorio/curadoria-v3.json a partir do JSON bruto da pesquisa.

Uso: python montar_curadoria.py bruto.json curadoria-v3.json
"""
import json, re, sys, unicodedata
from taxonomia import MACROEIXOS, PROBLEMAS, MAPA, POR_REGISTRO

# Registros 001–024: a pesquisa só traz o resumo (seção 5); o tipo de entidade é inferido do nome.
TIPO_RESUMO = {
    "REP-001": "LEGAL_SOURCE", "REP-002": "LEGAL_SOURCE", "REP-003": "LEGAL_SOURCE", "REP-004": "LEGAL_SOURCE",
    "REP-005": "LEGAL_SOURCE", "REP-006": "LEGAL_SOURCE", "REP-007": "PERSON", "REP-008": "LEGAL_SOURCE",
    "REP-009": "EVIDENCE", "REP-010": "EVIDENCE", "REP-011": "LEGAL_SOURCE", "REP-012": "PERSON",
    "REP-013": "LEGAL_SOURCE", "REP-014": "LEGAL_SOURCE", "REP-015": "CONCEPT", "REP-016": "CULTURAL_WORK",
    "REP-017": "CULTURAL_WORK", "REP-018": "CULTURAL_WORK", "REP-019": "LEGAL_SOURCE", "REP-020": "CULTURAL_WORK",
    "REP-021": "LEGAL_SOURCE", "REP-022": "LEGAL_SOURCE", "REP-023": "EVIDENCE", "REP-024": "LEGAL_SOURCE",
}

# Problema principal de cada pacote (seção 9 + os dois "NOVO PAC" da seção 20)
PACOTES_PROBLEMA = {
    "PAC-01": ["infancia-digital", "inclusao-digital"], "PAC-02": ["plataformizacao-trabalho"],
    "PAC-03": ["alfabetizacao"], "PAC-04": ["inclusao-escolar-pcd"], "PAC-05": ["populacao-rua", "trabalhos-invisibilizados"],
    "PAC-06": ["trabalho-domestico", "trabalho-cuidado"], "PAC-07": ["desigualdade-saude", "racismo"],
    "PAC-08": ["violencia-racial", "violencia-urbana"], "PAC-09": ["etarismo", "cuidado-pessoa-idosa"],
    "PAC-10": ["justica-climatica", "calor-extremo"], "PAC-11": ["saude-mental"],
    "PAC-12": ["territorio-indigena", "direitos-indigenas"],
}
NOVOS_PACOTES = {  # a seção 20 cria dois pacotes sem código — códigos propostos aqui
    "Saúde mental e cuidado em liberdade": "PAC-11",
    "Povos indígenas, território e cultura": "PAC-12",
}
SLOTS = {"Especialista": "SPECIALIST", "Conceito": "CONCEPT", "Evidência": "EVIDENCE", "Jurídico": "LEGAL", "Cultural": "CULTURAL"}


def sem_acento(s):
    return "".join(c for c in unicodedata.normalize("NFD", s.lower()) if unicodedata.category(c) != "Mn")


def mapear(code, termos, avisos):
    canon = list(POR_REGISTRO.get(code, []))
    for t in termos:
        k = t.lower().strip()
        if k not in MAPA:
            avisos.append(f"{code}: termo sem mapeamento '{t}'")
            continue
        canon += MAPA[k]
    return list(dict.fromkeys(canon))


def ano(raw):
    anos = re.findall(r"(?:19|20)\d{2}", raw or "")
    return int(anos[-1]) if anos else None


def main(src, dst):
    b = json.load(open(src, encoding="utf-8"))
    avisos, reps = [], []

    # 1) registros completos REP-025..062
    for r in b["repertoires"]:
        et = r.get("entityType") or ("CULTURAL_WORK" if re.match(r"(FILME|OBRA)", r["typeLabel"]) else "PERSON")
        reps.append({
            "code": r["code"], "name": r["name"], "headingSubtitle": r["headingSubtitle"],
            "entityType": et, "entityTypeInferred": "entityType" not in r, "role": r["role"],
            "evidenceTypes": [], "typeLabel": r["typeLabel"], "country": r["country"], "field": r["field"],
            "gapResolved": r.get("gapResolved"), "centralIdea": r["centralIdea"], "rememberForExam": r["rememberForExam"],
            "howToUse": r["howToUse"], "applicationExample": r["applicationExample"], "commonMisuse": r["commonMisuse"],
            "misuseRisk": r["misuseRisk"], "difficulty": r["difficulty"], "saturation": r["saturation"], "scores": r["scores"],
            "argumentativeFunctions": r["argumentativeFunctions"], "argumentTypes": r["argumentTypes"],
            "socialProblems": mapear(r["code"], r["socialProblems"], avisos), "tags": r["socialProblems"],
            "conceptRelationsRaw": r.get("conceptRelations"), "sources": r["sources"],
            "evidenceYear": None, "verificationStatus": r["verificationStatus"], "verificationDate": r["verificationDate"],
            "changeStatus": r["changeStatus"], "summaryOnly": False, "active": True,
        })

    # 2) registros resumidos REP-001..024 (entram inativos até serem completados)
    exp = {e["code"]: e for e in b["v2Expansions"]}
    for s in b["summaryRecords"]:
        reps.append({
            "code": s["code"], "name": s["name"], "headingSubtitle": None, "entityType": TIPO_RESUMO[s["code"]],
            "entityTypeInferred": True, "role": s["role"], "evidenceTypes": [], "typeLabel": None, "country": "Brasil",
            "field": None, "gapResolved": None, "centralIdea": s["centralFunction"], "rememberForExam": None,
            "howToUse": None, "applicationExample": None,
            "commonMisuse": exp[s["code"]]["commonMisuse"] if s["code"] in exp else s["commonMisuse"],
            "misuseRisk": None, "difficulty": None, "saturation": None, "scores": None,
            "argumentativeFunctions": [], "argumentTypes": [], "socialProblems": [], "tags": [],
            "conceptRelationsRaw": exp[s["code"]]["expansion"] if s["code"] in exp else None, "sources": [],
            "evidenceYear": None, "verificationStatus": "PARTIAL", "verificationDate": None,
            "changeStatus": s["changeStatus"], "summaryOnly": True, "active": False,
        })
    # expansões da seção 16 sobre registros completos: erro comum reforçado
    por_cod = {r["code"]: r for r in reps}
    for c, e in exp.items():
        if not por_cod[c]["summaryOnly"]:
            por_cod[c]["commonMisuse"] = e["commonMisuse"]
            por_cod[c]["v3Expansion"] = e["expansion"]

    # 3) evidências
    for e in b["evidence"]:
        y = ano(e["evidenceYearRaw"])
        src = [{"kind": "GENERAL", "description": e["sourceName"], "url": e["url"]}]
        if not e["url"]:
            avisos.append(f"{e['code']}: evidência sem URL (a pesquisa só lista a fonte por nome)")
        reps.append({
            "code": e["code"], "name": e["name"], "headingSubtitle": None, "entityType": "EVIDENCE", "entityTypeInferred": False,
            "role": "EVIDENCE", "evidenceTypes": [{"SYSTEMATIC/SCOPING_REVIEW": "SCOPING_REVIEW"}.get(t, t) for t in e["evidenceTypes"]],
            "typeLabel": " / ".join(e["evidenceTypes"]),
            "country": "Brasil", "field": None, "gapResolved": None, "centralIdea": e["centralIdea"], "rememberForExam": None,
            "howToUse": None, "applicationExample": None, "commonMisuse": e["commonMisuse"], "misuseRisk": None,
            "difficulty": None, "saturation": None, "scores": None, "argumentativeFunctions": ["PROVE_PROBLEM"],
            "argumentTypes": [], "socialProblems": mapear(e["code"], e["socialProblems"], avisos), "tags": e["socialProblems"],
            "conceptRelationsRaw": None, "sources": src, "population": e["population"], "evidenceYear": y,
            "evidenceYearRaw": e["evidenceYearRaw"], "updateNeeded": e["updateNeeded"], "verificationStatus": "VERIFIED",
            "verificationDate": "2026-10-07", "changeStatus": "NEW", "summaryOnly": False, "active": True,
        })

    # 4) Constituição
    for c in b["constitution"]:
        reps.append({
            "code": c["code"], "name": f"Constituição Federal, {c['article']}", "headingSubtitle": None,
            "entityType": "LEGAL_SOURCE", "entityTypeInferred": False, "role": "REPERTOIRE", "evidenceTypes": [],
            "typeLabel": "CONSTITUIÇÃO FEDERAL", "country": "Brasil", "field": "Direito constitucional", "gapResolved": None,
            "centralIdea": c["centralIdea"], "rememberForExam": None, "howToUse": None, "applicationExample": None,
            "commonMisuse": c["commonMisuse"], "misuseRisk": None, "difficulty": None, "saturation": None, "scores": None,
            "argumentativeFunctions": ["LEGAL_GAP"], "argumentTypes": [],
            "socialProblems": mapear(c["code"], c["socialProblems"], avisos), "tags": c["socialProblems"],
            "conceptRelationsRaw": None,
            "sources": [{"kind": "GENERAL", "description": "Constituição da República Federativa do Brasil de 1988 — Portal Planalto",
                         "url": "https://www.planalto.gov.br/ccivil_03/constituicao/constituicao.htm"}],
            "evidenceYear": None, "verificationStatus": "VERIFIED", "verificationDate": "2026-10-07",
            "changeStatus": c["changeStatus"], "summaryOnly": False, "active": True,
        })

    # 5) pacotes + ajustes da seção 20
    pacotes = {}
    for p in b["packages"]:
        pac = {"code": p["code"], "title": p["title"], "socialProblems": PACOTES_PROBLEMA[p["code"]],
               "cause": p.get("Causa"), "consequence": p.get("Consequência"), "intervention": p.get("Intervenção"), "slots": {}}
        for lbl, slot in SLOTS.items():
            pac["slots"][slot] = {"text": p.get(lbl), "patchedInV3": False}
        pacotes[p["code"]] = pac
    for pt in b["packagePatches"]:
        nome = pt["package"]
        if nome.startswith("NOVO PAC"):
            titulo = re.sub(r"^NOVO PAC\s*[—–-]+\s*", "", nome).strip()
            cod = NOVOS_PACOTES[titulo]
            pacotes.setdefault(cod, {"code": cod, "codeProposed": True, "title": titulo,
                                     "socialProblems": PACOTES_PROBLEMA[cod], "cause": None, "consequence": None,
                                     "intervention": None, "slots": {s: {"text": None, "patchedInV3": False} for s in SLOTS.values()}})
        else:
            cod = nome.split()[0]
        slot = SLOTS[pt["field"]]
        pacotes[cod]["slots"][slot] = {"text": pt["component"], "patchedInV3": True, "justification": pt["justification"]}

    # resolve slots -> códigos de registros (o texto livre é sempre mantido)
    nomes = sorted(((sem_acento(r["name"].split(" — ")[0]), r["code"]) for r in reps if r["entityType"] != "LEGAL_SOURCE" or r["code"].startswith("REP")),
                   key=lambda x: -len(x[0]))
    artigos = {re.search(r"Art\. (\d+)", r["name"]).group(1): r["code"] for r in reps if r["code"].startswith("CF-")}
    apelidos = {"tic kids online": "REP-010", "pense 2024": "EVD-006", "atlas da violencia 2025": "EVD-007", "obpoprua": "EVD-008",
                "ondas de calor": "EVD-009", "desastres hidrologicos": "EVD-010", "pnad educacao": "EVD-001",
                "marco civil": "REP-008", "lei 10.216": "REP-019", "estatuto da pessoa idosa": "REP-021",
                "estatuto da igualdade racial": "REP-013", "lbi": "REP-002", "lei brasileira de inclusao": "REP-002",
                "uberizacao": "REP-031", "capital cultural": "REP-042", "reconhecimento": "REP-041", "mantoan": "REP-029",
                "ludmila": "REP-031", "rita barata": "REP-025", "kopenawa": "REP-056", "mds/oit": "REP-023",
                "revisao de escopo": "EVD-002", "letramento": "REP-027"}
    tipo_por_cod = {r["code"]: r["entityType"] for r in reps}
    aceita = {"SPECIALIST": {"PERSON"}, "CONCEPT": {"CONCEPT", "PERSON"}, "EVIDENCE": {"EVIDENCE"},
              "LEGAL": {"LEGAL_SOURCE"}, "CULTURAL": {"CULTURAL_WORK"}}
    for pac in pacotes.values():
        for slot, v in pac["slots"].items():
            t = sem_acento(v["text"] or "")
            gap = t == "lacuna" or t.startswith("mantido como lacuna") or "nao encontrado" in t
            v["status"] = "UNDEFINED" if not t else "GAP" if gap else "FILLED"
            codes = [c for n, c in nomes if len(n) > 5 and n in t]
            codes += [c for k, c in apelidos.items() if k in t]
            for a in re.findall(r"arts?\.?\s*([\dºo,e ]+)", t):
                codes += [artigos[n] for n in re.findall(r"\d+", a) if n in artigos]
            v["repertoireCodes"] = [c for c in dict.fromkeys(codes) if tipo_por_cod[c] in aceita[slot]]

    out = {
        "meta": {"source": "StudyMind_Repertorios_ENEM_V3_Correcao_Pontual_Lacunas.docx", "researchVersion": "V3",
                 "verifiedAt": "2026-10-07", "generatedBy": "montar_curadoria.py",
                 "notes": ["Bauman e Durkheim excluídos por decisão da pesquisa (saturação).",
                           "REP-001 a REP-024 só existem resumidos na V2/V3 — entram com active=false.",
                           "Itens com origin='curadoria' e códigos PAC-11/PAC-12 são propostas da conversão, não da pesquisa."]},
        "macroThemes": [{"slug": s, "name": n, "order": i + 1, "origin": o} for i, (s, n, o) in enumerate(MACROEIXOS)],
        "socialProblems": [{"slug": s, "name": n, "macroTheme": m, "origin": o} for s, n, m, o in PROBLEMAS],
        "repertoires": sorted(reps, key=lambda r: r["code"]),
        "packages": sorted(pacotes.values(), key=lambda p: p["code"]),
    }
    json.dump(out, open(dst, "w", encoding="utf-8"), ensure_ascii=False, indent=2)
    for a in avisos: print("AVISO", a)
    print({"repertorios": len(reps), "ativos": sum(r["active"] for r in reps), "pacotes": len(pacotes)})


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
