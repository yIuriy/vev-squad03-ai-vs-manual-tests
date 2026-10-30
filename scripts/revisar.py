"""Revisão mecânica de uma suíte congelada (docs/protocolo_medicao.md). Três passos:

  preparar  <codigo> <condicao> <rotulo> <revisor>
      Copia a entrega congelada para privado/revisado/<condicao>/<codigo>/<rotulo>/, onde o
      revisor edita. A entrega original nunca é tocada. Recusa revisor que seja o autor
      (o próprio testador, ou o operador de IA da falha).
  registrar <codigo> <condicao> <rotulo> <revisor> <categoria> <descricao>
      Registra uma correção em privado/revisado/revisao.csv. Uma linha por correção.
  congelar  <codigo> <condicao> <rotulo> <revisor>
      Deixa a versão revisada somente leitura e registra o hash final. Depois disso, nada muda.

Categorias (lista fechada): import, cast, assinatura, throws, pontuacao, metodo_comentado
"""
import csv
import hashlib
import shutil
import sys
from datetime import datetime
from pathlib import Path

ENTREGAS = Path("privado/entregas")
REVISADO = Path("privado/revisado")
REGISTRO = REVISADO / "revisao.csv"
DISTRIBUICAO = Path("privado/distribuicao/distribuicao.csv")
CATEGORIAS = {"import", "cast", "assinatura", "throws", "pontuacao", "metodo_comentado"}


def sha(arquivo):
    return hashlib.sha256(arquivo.read_bytes()).hexdigest()


def arquivo_unico(pasta):
    arquivos = list(pasta.glob("*.java"))
    if len(arquivos) != 1:
        sys.exit(f"esperava 1 arquivo .java em {pasta}, achei {len(arquivos)}")
    return arquivos[0]


def autor(codigo, condicao, rotulo):
    if condicao != "ia":
        return rotulo
    with open(DISTRIBUICAO, newline="", encoding="utf-8") as f:
        return next(l["operador_ia"] for l in csv.DictReader(f) if l["codigo"] == codigo)


def registra(codigo, condicao, rotulo, revisor, categoria, descricao):
    original = arquivo_unico(ENTREGAS / condicao / codigo / rotulo)
    revisado = arquivo_unico(REVISADO / condicao / codigo / rotulo)
    novo = not REGISTRO.exists()
    with open(REGISTRO, "a", newline="", encoding="utf-8") as f:
        w = csv.writer(f, lineterminator="\n")
        if novo:
            w.writerow(["data", "codigo", "condicao", "rotulo", "revisor", "categoria", "descricao",
                        "sha256_original", "sha256_revisado"])
        w.writerow([datetime.now().isoformat(timespec="seconds"), codigo, condicao, rotulo, revisor,
                    categoria, descricao, sha(original), sha(revisado)])


def main(acao, codigo, condicao, rotulo, revisor, *resto):
    destino = REVISADO / condicao / codigo / rotulo
    if acao == "preparar":
        if revisor.lower() == autor(codigo, condicao, rotulo).lower():
            sys.exit(f"{revisor} é o autor desta suíte e não pode revisá-la")
        if destino.exists():
            sys.exit(f"já existe uma revisão em {destino}")
        original = arquivo_unico(ENTREGAS / condicao / codigo / rotulo)
        destino.mkdir(parents=True)
        shutil.copyfile(original, destino / original.name)
        print(f"edite: {destino / original.name}")
    elif acao == "registrar":
        categoria, descricao = resto
        if categoria not in CATEGORIAS:
            sys.exit(f"categoria fora da lista fechada: {categoria} (use {', '.join(sorted(CATEGORIAS))})")
        registra(codigo, condicao, rotulo, revisor, categoria, descricao)
        print("registrado")
    elif acao == "congelar":
        revisado = arquivo_unico(destino)
        revisado.chmod(0o444)
        registra(codigo, condicao, rotulo, revisor, "CONGELADO", "versão revisada final")
        print(f"congelado: {revisado}\nsha256: {sha(revisado)}")
    else:
        sys.exit(f"ação desconhecida: {acao}")


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    sys.stderr.reconfigure(encoding="utf-8")
    main(*sys.argv[1:])
