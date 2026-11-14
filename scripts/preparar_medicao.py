"""Monta as entradas da medição a partir das suítes congeladas, sem alterar o conteúdo.

Para cada falha e condição (humano, ia), usa a versão revisada e congelada em privado/revisado/
se existir; senão, a entrega congelada em privado/entregas/. Copia para
privado/medicao/entrada/<código>/<condição>/<ClassePública>.java, porque o Java exige que o
nome do arquivo seja o da classe pública (ex.: a suíte de IA do F08 declara F09Test).
Gera privado/medicao/entrada/manifesto.csv com a origem e o SHA-256 de cada arquivo.

Uso: python scripts/preparar_medicao.py
"""
import csv
import hashlib
import re
import shutil
import sys
from pathlib import Path

ENTREGAS = Path("privado/entregas")
REVISADO = Path("privado/revisado")
SAIDA = Path("privado/medicao/entrada")
CONDICOES = ["humano", "ia"]


def unico_java(pasta):
    arquivos = sorted(pasta.glob("*.java"))
    return arquivos[0] if len(arquivos) == 1 else None


def revisao_congelada(condicao, codigo, rotulo):
    registro = REVISADO / "revisao.csv"
    if not registro.exists():
        return False
    with open(registro, newline="", encoding="utf-8") as f:
        return any(l["condicao"] == condicao and l["codigo"] == codigo and l["rotulo"] == rotulo
                   and l["categoria"] == "CONGELADO" for l in csv.DictReader(f))


def main():
    with open(ENTREGAS / "registro.csv", newline="", encoding="utf-8") as f:
        entregas = [l for l in csv.DictReader(f) if l["condicao"] in CONDICOES]

    if SAIDA.exists():
        shutil.rmtree(SAIDA)
    linhas = []
    for e in sorted(entregas, key=lambda l: (l["codigo"], l["condicao"])):
        codigo, condicao, rotulo = e["codigo"], e["condicao"], e["rotulo"]
        revisado = REVISADO / condicao / codigo / rotulo
        if revisao_congelada(condicao, codigo, rotulo):
            origem, versao = unico_java(revisado), "revisada"
        else:
            origem, versao = Path(e["arquivo"]), "entregue"
        texto = origem.read_text(encoding="utf-8")
        classe = re.search(r"public\s+(?:final\s+)?class\s+(\w+)", texto)
        if not classe:
            sys.exit(f"sem classe pública: {origem}")
        destino = SAIDA / codigo / condicao / f"{classe.group(1)}.java"
        destino.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(origem, destino)
        linhas.append([codigo, condicao, rotulo, versao, origem.as_posix(), destino.as_posix(),
                       hashlib.sha256(destino.read_bytes()).hexdigest()])

    with open(SAIDA / "manifesto.csv", "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f, lineterminator="\n")
        w.writerow(["codigo", "condicao", "rotulo", "versao", "origem", "entrada", "sha256"])
        w.writerows(linhas)
    print(f"{len(linhas)} suítes preparadas em {SAIDA}")


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    main()
