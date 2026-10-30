"""Sorteia quem escreve a suíte humana e quem opera a IA em cada falha, a ordem em que cada
pessoa faz as suas falhas, e monta um zip por falha (entregue só quando a anterior é entregue).

Restrições:
  - Cada falha recebe exatamente 1 suíte humana. Quem tem cota 2 recebe 1 falha do Lang e
    1 do Math; entre os dois de cota 3, um recebe 2 Lang + 1 Math e o outro 1 Lang + 2 Math.
  - Cada operador de IA roda 4 falhas (2 Lang + 2 Math), nunca uma em que escreveu a suíte humana.
  - Sorteio com a mesma semente do protocolo; combinações que violam as restrições são descartadas.
    A ordem é sorteada depois, então não altera a distribuição.

Uso: python scripts/distribuir.py privado/amostra.csv kit privado/distribuicao
Saída: <saida>/distribuicao.csv, <saida>/ordem.csv, <saida>/aquecimento.zip,
       <saida>/humano/<Nome>_<n>_<código>.zip, <saida>/ia/<Nome>_<n>_<código>.zip
"""
import csv
import random
import sys
import zipfile
from pathlib import Path

SEMENTE = 7202620  # registrada no protocolo como "07202620"
COTA_HUMANA = {"Dyonathan": 2, "Lara": 3, "Tamires": 3, "Rafaela": 2, "Sidnei": 2}
OPERADORES_IA = ["Dyonathan", "Rafaela", "Sidnei"]
FALHAS_POR_OPERADOR = 4


def sorteia_humanos(rng, lang, math):
    lang, math = rng.sample(lang, len(lang)), rng.sample(math, len(math))
    tres = [n for n, c in COTA_HUMANA.items() if c == 3]
    mais_lang = rng.choice(tres)
    humano = {}
    for nome, cota in COTA_HUMANA.items():
        n_lang = 1 if cota == 2 else (2 if nome == mais_lang else 1)
        for _ in range(n_lang):
            humano[lang.pop()] = nome
        for _ in range(cota - n_lang):
            humano[math.pop()] = nome
    return humano


def sorteia_ia(rng, lang, math, humano):
    while True:
        lang_s, math_s = rng.sample(lang, len(lang)), rng.sample(math, len(math))
        ia = {}
        for i, nome in enumerate(OPERADORES_IA):
            for codigo in lang_s[2 * i:2 * i + 2] + math_s[2 * i:2 * i + 2]:
                ia[codigo] = nome
        if all(ia[c] != humano[c] for c in ia):
            return ia


def zipa(destino, kit, codigos):
    with zipfile.ZipFile(destino, "w", zipfile.ZIP_DEFLATED) as z:
        for nome in ["INSTRUCOES.md", "PERFIL_VSCODE.md", "JUNIT.md"]:
            z.write(kit / nome, nome)
        for codigo in sorted(codigos):
            for arq in sorted(p for p in (kit / codigo).rglob("*") if p.is_file()):
                z.write(arq, arq.relative_to(kit).as_posix())


def main(amostra, kit, saida):
    kit, saida = Path(kit), Path(saida)
    saida.mkdir(parents=True, exist_ok=True)
    with open(amostra, newline="") as f:
        falhas = sorted(csv.DictReader(f), key=lambda l: l["codigo"])
    lang = [l["codigo"] for l in falhas if l["projeto"] == "Lang"]
    math = [l["codigo"] for l in falhas if l["projeto"] == "Math"]

    rng = random.Random(SEMENTE)
    humano = sorteia_humanos(rng, lang, math)
    ia = sorteia_ia(rng, lang, math, humano)

    with open(saida / "distribuicao.csv", "w", newline="") as f:
        w = csv.writer(f, lineterminator="\n")
        w.writerow(["codigo", "projeto", "suite_humana", "operador_ia"])
        for l in falhas:
            w.writerow([l["codigo"], l["projeto"], humano[l["codigo"]], ia[l["codigo"]]])

    with open(saida / "ordem.csv", "w", newline="") as f:
        w = csv.writer(f, lineterminator="\n")
        w.writerow(["papel", "pessoa", "ordem", "codigo", "zip"])
        for papel, atribuicao, pessoas in [("humano", humano, COTA_HUMANA), ("ia", ia, OPERADORES_IA)]:
            (saida / papel).mkdir(exist_ok=True)
            for nome in pessoas:
                codigos = rng.sample(sorted(c for c, n in atribuicao.items() if n == nome),
                                     sum(1 for n in atribuicao.values() if n == nome))
                for ordem, codigo in enumerate(codigos, 1):
                    arquivo = f"{papel}/{nome}_{ordem}_{codigo}.zip"
                    zipa(saida / arquivo, kit, [codigo])
                    w.writerow([papel, nome, ordem, codigo, arquivo])
    zipa(saida / "aquecimento.zip", kit, ["P01"])


if __name__ == "__main__":
    main(*sys.argv[1:4])
