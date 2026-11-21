#!/usr/bin/env bash
# Extrai, para cada falha da amostra, os testes gatilho do Defects4J (os que falham na versão com
# bug e passam na corrigida) com o código-fonte de cada método. Serve para comparar as entradas
# das suítes do estudo com a entrada que dispara a falha (tarefa T4 do Andamento 5).
#
# Uso (dentro do container): bash scripts/gatilhos.sh
# Saída: privado/medicao/diagnostico/gatilhos/<código>.md
set -uo pipefail
out=/work/privado/medicao/diagnostico/gatilhos
mkdir -p "$out"
tail -n +2 /work/privado/amostra.csv | while IFS=, read -r codigo p id classe _; do
  w=/tmp/gat/$codigo
  [ -d "$w" ] || defects4j checkout -p "$p" -v "${id}b" -w "$w" >/dev/null 2>&1 || { echo "erro: checkout $codigo" >&2; continue; }
  dtest=$(cd "$w" && defects4j export -p dir.src.tests 2>/dev/null)
  {
    echo "# $codigo: $p-$id, $classe"
    echo
    echo "Testes gatilho do Defects4J (falham na versão com bug, passam na corrigida)."
    echo
    (cd "$w" && defects4j export -p tests.trigger 2>/dev/null; echo) | while read -r t; do
      [ -n "$t" ] || continue
      cls=${t%%::*}; met=${t##*::}
      arq="$w/$dtest/$(tr . / <<< "$cls").java"
      echo "## \`$met\` ($cls)"
      echo
      echo '```java'
      # do cabeçalho do método até a chave que fecha o corpo
      awk -v m="$met" '
        !dentro && $0 ~ ("void[ \t]+" m "[ \t]*\(") { dentro = 1 }
        dentro { print; n += gsub(/\{/, "{"); n -= gsub(/\}/, "}"); if (n == 0 && index($0, "}")) exit }
      ' "$arq"
      echo '```'
      echo
    done
  } > "$out/$codigo.md"
  echo "$codigo ok"
done
