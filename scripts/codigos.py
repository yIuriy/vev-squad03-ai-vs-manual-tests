"""Atribui os códigos neutros F01 a F12 às falhas da amostra (ver docs/protocolo_selecao.md).

As 12 falhas são ordenadas por projeto e ID do bug e embaralhadas com a mesma semente do sorteio.
Uso:   python scripts/codigos.py privado/amostra_sem_codigo.csv > privado/amostra.csv
Entrada: projeto,bug,classe,ncloc   Saída: codigo,projeto,bug,classe,ncloc
"""
import csv
import random
import sys

SEMENTE = 7202620  # registrada no protocolo como "07202620"


def main(caminho):
    with open(caminho, newline="") as f:
        falhas = sorted(csv.DictReader(f), key=lambda l: (l["projeto"], int(l["bug"])))

    random.Random(SEMENTE).shuffle(falhas)
    saida = csv.writer(sys.stdout, lineterminator="\n")
    saida.writerow(["codigo", "projeto", "bug", "classe", "ncloc"])
    for i, l in enumerate(falhas, 1):
        saida.writerow([f"F{i:02d}", l["projeto"], l["bug"], l["classe"], l["ncloc"]])


if __name__ == "__main__":
    main(sys.argv[1])
