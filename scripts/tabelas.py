"""Recalcula os números das Tabelas 3 e 4 do artigo a partir dos dados publicados.

Fontes:
  privado/medicao/resultados_finais.csv       (saída de scripts/consolidar.py)
  privado/medicao/resultados_rodada1_bruta.csv (suítes como foram entregues, antes da revisão mecânica)
  privado/revisado/revisao.csv                (correções da revisão mecânica)

Uso: python scripts/tabelas.py
"""
import csv
import statistics
from pathlib import Path

RAIZ = Path(__file__).resolve().parent.parent
MED = RAIZ / "privado" / "medicao"
CONDICOES = ("humano", "ia")
CORRECOES = {"import", "cast", "assinatura", "throws", "pontuacao"}


def ler(caminho):
    with open(caminho, newline="", encoding="utf-8") as f:
        return list(csv.DictReader(f))


finais = ler(MED / "resultados_finais.csv")
bruta = ler(MED / "resultados_rodada1_bruta.csv")
revisao = ler(RAIZ / "privado" / "revisado" / "revisao.csv")

print("Tabela 3 - resultado por falha (cobertura na versão corrigida)")
print(f"{'falha':6}{'suite':8}{'testes':>7}{'detecta':>9}{'LCov%':>8}{'BCov%':>8}")
for r in sorted(finais, key=lambda r: (r["codigo"], r["suite"])):
    print(f"{r['codigo']:6}{r['suite']:8}{r['testes']:>7}{r['detecta']:>9}"
          f"{float(r['cob_linhas_f']):>8.1f}{float(r['cob_branches_f']):>8.1f}")

print("\nTabela 4 - resumo por condição")
for c in CONDICOES:
    linhas = [r for r in finais if r["suite"] == c]
    n = len(linhas)
    lcov = [float(r["cob_linhas_f"]) for r in linhas]
    bcov = [float(r["cob_branches_f"]) for r in linhas]
    soma = lambda campo: sum(int(r[campo] or 0) for r in linhas)
    compiladas = sum(r["compila_b"] == "sim" and r["compila_f"] == "sim" for r in bruta if r["suite"] == c)
    rev = [r for r in revisao if r["condicao"] == c]
    print(f"\n[{c}]")
    print(f"  FDR (falhas detectadas)          {sum(r['detecta'] == 'sim' for r in linhas)}/{n}")
    print(f"  LCov média (mediana)             {statistics.mean(lcov):.1f} ({statistics.median(lcov):.1f})")
    print(f"  BCov média (mediana)             {statistics.mean(bcov):.1f} ({statistics.median(bcov):.1f})")
    print(f"  testes executados                {soma('testes')}")
    print(f"  falsos alarmes                   {soma('falsos_alarmes')}")
    print(f"  testes instáveis                 {soma('instaveis')}")
    print(f"  compilaram como entregues        {compiladas}/{n}")
    print(f"  correções da revisão mecânica    {sum(r['categoria'] in CORRECOES for r in rev)}")
    print(f"  métodos comentados               {sum(r['categoria'] == 'metodo_comentado' for r in rev)}")
    print(f"  suítes sem teste ativo           {sum(int(r['testes'] or 0) == 0 for r in linhas)}")
