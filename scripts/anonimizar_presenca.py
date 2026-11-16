"""Gera, para cada relatório de presença do Google Meet (.xlsx), um .csv sem e-mails:
nome, entrada, saída e duração. Os .xlsx originais ficam fora do repositório (.gitignore).

Uso: python scripts/anonimizar_presenca.py privado/evidencias
"""
import csv
import re
import sys
import zipfile
from pathlib import Path

COLUNAS = {"Nome": "nome", "Sobrenome": "sobrenome", "Duração": "duracao",
           "Horário de entrada": "entrada", "Horário de saída": "saida"}


def linhas_xlsx(caminho):
    z = zipfile.ZipFile(caminho)
    textos = [re.sub("<[^>]+>", "", m) for m in
              re.findall(r"<si>(.*?)</si>", z.read("xl/sharedStrings.xml").decode("utf-8"), re.S)]
    planilha = z.read("xl/worksheets/sheet1.xml").decode("utf-8")
    for linha in re.findall(r"<row[^>]*>(.*?)</row>", planilha, re.S):
        celulas = re.findall(r"<c ([^>]*)>(?:<f>.*?</f>)?<v>(.*?)</v>", linha)
        yield [textos[int(v)] if 't="s"' in a else v for a, v in celulas]


def main(pasta):
    for xlsx in sorted(Path(pasta).glob("*.xlsx")):
        linhas = list(linhas_xlsx(xlsx))
        cabecalho = linhas[0]
        manter = [i for i, c in enumerate(cabecalho) if c in COLUNAS]
        destino = xlsx.with_suffix(".csv")
        with open(destino, "w", newline="", encoding="utf-8") as f:
            w = csv.writer(f, lineterminator="\n")
            w.writerow([COLUNAS[cabecalho[i]] for i in manter])
            for linha in linhas[1:]:
                w.writerow([linha[i] for i in manter])
        print(destino)


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    main(sys.argv[1])
