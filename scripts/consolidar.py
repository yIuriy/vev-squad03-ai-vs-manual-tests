"""Consolida a medição final: para cada falha e condição, usa a última rodada medida da suíte
(revisada, se houve revisão) e gera a tabela por falha e o resumo humano × IA.

Correção aplicada: quando a suíte não tem nenhum método @Test ativo (todos comentados ou
nenhum escrito), o JUnit gera um "teste" initializationError ("No runnable methods"), que o
medir.sh conta como falso alarme. Nesse caso a suíte é contada com 0 testes e 0 falsos alarmes.

Uso: python scripts/consolidar.py <rodada1.csv> <rodada2.csv> [...]   (a última rodada vence)
Saída: privado/medicao/resultados_finais.csv e o resumo no stdout.
"""
import csv
import re
import statistics
import sys
from pathlib import Path

ENTRADA = Path("privado/medicao/entrada")
CAMPOS = ["codigo", "suite", "compila_b", "compila_f", "testes", "detecta", "detectores",
          "falsos_alarmes", "instaveis", "linhas_cob_b", "linhas_tot_b", "branches_cob_b",
          "branches_tot_b", "linhas_cob_f", "linhas_tot_f", "branches_cob_f", "branches_tot_f"]


def testes_ativos(codigo, condicao):
    texto = next((ENTRADA / codigo / condicao).glob("*.java")).read_text(encoding="utf-8")
    return sum(1 for l in texto.splitlines() if "@Test" in l and not l.lstrip().startswith("//"))


def pct(cob, tot):
    return 100.0 * int(cob) / int(tot) if tot and int(tot) else 0.0


def main(rodadas):
    linhas = {}
    for arq in rodadas:
        with open(arq, newline="", encoding="utf-8") as f:
            for row in csv.reader(f):
                if row and row[0].startswith("F"):
                    linhas[(row[0], row[1])] = dict(zip(CAMPOS, row))

    final = []
    for (codigo, cond), r in sorted(linhas.items()):
        if testes_ativos(codigo, cond) == 0 and r["testes"] == "1":
            r.update(testes="0", falsos_alarmes="0", detecta="nao", detectores="0")
        r["cob_linhas_f"] = f"{pct(r['linhas_cob_f'], r['linhas_tot_f']):.1f}"
        r["cob_branches_f"] = f"{pct(r['branches_cob_f'], r['branches_tot_f']):.1f}"
        final.append(r)

    saida = Path("privado/medicao/resultados_finais.csv")
    with open(saida, "w", newline="", encoding="utf-8") as f:
        w = csv.DictWriter(f, fieldnames=CAMPOS + ["cob_linhas_f", "cob_branches_f"], lineterminator="\n")
        w.writeheader()
        w.writerows(final)

    print(f"{'falha':6}{'cond':8}{'testes':>7}{'detecta':>9}{'linhas%':>9}{'branch%':>9}{'f.alarm':>9}")
    for r in final:
        print(f"{r['codigo']:6}{r['suite']:8}{r['testes']:>7}{r['detecta']:>9}{r['cob_linhas_f']:>9}"
              f"{r['cob_branches_f']:>9}{r['falsos_alarmes']:>9}")
    print()
    for cond in ["humano", "ia"]:
        rs = [r for r in final if r["suite"] == cond]
        lin = [float(r["cob_linhas_f"]) for r in rs]
        br = [float(r["cob_branches_f"]) for r in rs]
        det = sum(r["detecta"] == "sim" for r in rs)
        print(f"{cond:7} FDR {det}/{len(rs)} = {100 * det / len(rs):.1f}% | "
              f"LCov média {statistics.mean(lin):.1f}% mediana {statistics.median(lin):.1f}% | "
              f"BCov média {statistics.mean(br):.1f}% mediana {statistics.median(br):.1f}% | "
              f"testes {sum(int(r['testes']) for r in rs)} | falsos alarmes {sum(int(r['falsos_alarmes']) for r in rs)}")


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    main(sys.argv[1:])
