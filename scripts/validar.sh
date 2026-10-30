#!/usr/bin/env bash
# Critério C5 (docs/protocolo_selecao.md) para um bug: as duas versões compilam, os testes
# que falham na versão com bug são exatamente os testes gatilho e nenhum teste falha na
# versão corrigida. Cada versão roda a suíte completa 2 vezes.
# Saída CSV: projeto,bug,gatilhos,falhas_b1,falhas_b2,falhas_f1,falhas_f2,c5,motivo
# Uso (dentro do container): bash scripts/validar.sh Lang 43
set -uo pipefail

p=$1; id=$2
base=/tmp/val/$p-$id
rm -rf "$base"; mkdir -p "$base"

falhas() {  # lista ordenada dos testes que falharam em uma execução
  (cd "$1" && defects4j test 2>/dev/null) | sed -n 's/^  - //p' | sort
}

resultado() { echo "$p,$id,$1,$2,$3"; exit 0; }

for v in b f; do
  if ! defects4j checkout -p "$p" -v "$id$v" -w "$base/$v" >/dev/null 2>&1; then
    resultado ",,,," REPROVADO "checkout $v falhou"
  fi
  if ! (cd "$base/$v" && defects4j compile >/dev/null 2>&1); then
    resultado ",,,," REPROVADO "compilacao $v falhou"
  fi
done

gatilhos=$(cd "$base/b" && defects4j export -p tests.trigger 2>/dev/null | sort)
b1=$(falhas "$base/b"); b2=$(falhas "$base/b")
f1=$(falhas "$base/f"); f2=$(falhas "$base/f")

n() { [ -z "$1" ] && echo 0 || wc -l <<< "$1"; }
cont="$(n "$gatilhos"),$(n "$b1"),$(n "$b2"),$(n "$f1"),$(n "$f2")"

if [ -z "$gatilhos" ]; then
  resultado "$cont" REPROVADO "sem testes gatilho"
elif [ "$b1" != "$gatilhos" ] || [ "$b2" != "$gatilhos" ]; then
  resultado "$cont" REPROVADO "falhas na versao com bug diferem dos gatilhos"
elif [ -n "$f1$f2" ]; then
  resultado "$cont" REPROVADO "testes falham na versao corrigida"
fi
rm -rf "$base"
resultado "$cont" APROVADO ""
