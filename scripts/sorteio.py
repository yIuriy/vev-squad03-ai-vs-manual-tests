"""Sorteio da ordem de validação das falhas elegíveis (ver docs/protocolo_selecao.md).

Uso:   python scripts/sorteio.py privado/elegiveis.csv > privado/ordem_sorteio.csv
Saída: projeto,posicao,bug,classe,ncloc  (posição 1 = primeira a ser validada)
"""
import csv
import random
import sys

SEMENTE = 7202620  # registrada no protocolo como "07202620"
PROJETOS = ["Lang", "Math"]


def main(caminho):
    with open(caminho, newline="") as f:
        linhas = list(csv.DictReader(f))

    rng = random.Random(SEMENTE)
    saida = csv.writer(sys.stdout, lineterminator="\n")
    saida.writerow(["projeto", "posicao", "bug", "classe", "ncloc"])
    for projeto in PROJETOS:
        bugs = sorted((l for l in linhas if l["projeto"] == projeto), key=lambda l: int(l["bug"]))
        rng.shuffle(bugs)
        for pos, l in enumerate(bugs, 1):
            saida.writerow([projeto, pos, l["bug"], l["classe"], l["ncloc"]])


if __name__ == "__main__":
    main(sys.argv[1])
