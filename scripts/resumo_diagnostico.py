"""Junta a saída de scripts/diagnosticar.sh das 24 suítes e resume a cobertura das linhas do patch.

Entrada: privado/medicao/diagnostico/<código>/<condição>/{patch.tsv,falhas.tsv}
         privado/medicao/resultados_finais.csv (para saber se a suíte detectou a falha)
Saída:   privado/medicao/diagnostico/cobertura_patch.csv
         privado/medicao/diagnostico/falhas.csv
         resumo no stdout

Uma suíte "executou o patch" quando executou todas as linhas executáveis alteradas pelo patch,
nas duas versões (com bug e corrigida). "Parcial" quando executou parte delas, "não" quando nenhuma.

Uso: python scripts/resumo_diagnostico.py
"""
import csv
from pathlib import Path

DIAG = Path("privado/medicao/diagnostico")
finais = {(r["codigo"], r["suite"]): r for r in csv.DictReader(open("privado/medicao/resultados_finais.csv", encoding="utf-8"))}

linhas = []
for cod, cond in sorted(finais):
    v = {}
    for r in csv.DictReader(open(DIAG / cod / cond / "patch.tsv", encoding="utf-8"), delimiter="\t"):
        v[r["versao"]] = (int(r["executaveis"]), int(r["executadas"]))
    exe = v["b"][0] + v["f"][0]
    cob = v["b"][1] + v["f"][1]
    situacao = "sim" if exe and cob == exe else ("parcial" if cob else "nao")
    linhas.append({"codigo": cod, "condicao": cond,
                   "executaveis_b": v["b"][0], "executadas_b": v["b"][1],
                   "executaveis_f": v["f"][0], "executadas_f": v["f"][1],
                   "executou_patch": situacao, "detecta": finais[(cod, cond)]["detecta"]})

with open(DIAG / "cobertura_patch.csv", "w", newline="", encoding="utf-8") as f:
    w = csv.DictWriter(f, fieldnames=list(linhas[0]), lineterminator="\n")
    w.writeheader()
    w.writerows(linhas)

falhas = []
for p in sorted(DIAG.glob("*/*/falhas.tsv")):
    falhas += list(csv.DictReader(open(p, encoding="utf-8"), delimiter="\t"))
with open(DIAG / "falhas.csv", "w", newline="", encoding="utf-8") as f:
    w = csv.DictWriter(f, fieldnames=list(falhas[0]), lineterminator="\n")
    w.writeheader()
    w.writerows(falhas)

print("Execução das linhas do patch (todas / parte / nenhuma) e detecção")
for cond in ("humano", "ia"):
    ls = [l for l in linhas if l["condicao"] == cond]
    conta = {s: [l["codigo"] for l in ls if l["executou_patch"] == s] for s in ("sim", "parcial", "nao")}
    det = [l["codigo"] for l in ls if l["detecta"] == "sim"]
    print(f"  {cond:7} todas {len(conta['sim']):2} {conta['sim']}")
    print(f"  {'':7} parte {len(conta['parcial']):2} {conta['parcial']}")
    print(f"  {'':7} nenhuma {len(conta['nao']):2} {conta['nao']}")
    print(f"  {'':7} detectaram: {det}")

# Testes que passam na versão com bug e falham na corrigida: o oráculo é o comportamento com bug
st = {}
for r in falhas:
    st.setdefault((r["codigo"], r["condicao"], r["teste"]), {})[r["versao"]] = r["status"]
invertidos = [k for k, s in st.items() if s.get("b") == "PASS" and s.get("f") == "FAIL"]
print("\nTestes que passam na versão com bug e falham na corrigida (oráculo invertido):")
for cod, cond, teste in invertidos:
    print(f"  {cod} {cond} {teste.split('::')[1]}")
