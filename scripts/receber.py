"""Recebe e congela uma suíte entregue: copia o arquivo para privado/entregas/ e registra
data, hora e SHA-256 em privado/entregas/registro.csv. Uma entrega congelada nunca é sobrescrita.

Uso: python scripts/receber.py <codigo> <condicao> <rotulo> <arquivo>
  condicao: humano, ia ou ensaio
  rotulo:   nome do autor (humano/ensaio) ou execucao1..3 (ia)
Destino: privado/entregas/<condicao>/<codigo>/<rotulo>/<arquivo>
"""
import csv
import hashlib
import shutil
import sys
from datetime import datetime
from pathlib import Path

RAIZ = Path("privado/entregas")
CONDICOES = {"humano", "ia", "ensaio"}


def main(codigo, condicao, rotulo, arquivo):
    if condicao not in CONDICOES:
        sys.exit(f"condicao invalida: {condicao} (use {', '.join(sorted(CONDICOES))})")
    origem = Path(arquivo)
    destino = RAIZ / condicao / codigo / rotulo / origem.name
    if destino.exists():
        sys.exit(f"ja congelado, nao sobrescrevo: {destino}")

    destino.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(origem, destino)
    destino.chmod(0o444)
    sha = hashlib.sha256(destino.read_bytes()).hexdigest()
    recebido = datetime.now().isoformat(timespec="seconds")

    registro = RAIZ / "registro.csv"
    novo = not registro.exists()
    with open(registro, "a", newline="", encoding="utf-8") as f:
        w = csv.writer(f, lineterminator="\n")
        if novo:
            w.writerow(["recebido_em", "codigo", "condicao", "rotulo", "arquivo", "sha256"])
        w.writerow([recebido, codigo, condicao, rotulo, destino.as_posix(), sha])
    print(f"congelado: {destino}\nsha256: {sha}\nrecebido_em: {recebido}")


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    sys.stderr.reconfigure(encoding="utf-8")
    main(*sys.argv[1:5])
