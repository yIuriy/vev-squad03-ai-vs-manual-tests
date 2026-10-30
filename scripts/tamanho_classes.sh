#!/usr/bin/env bash
# Mede o tamanho da classe modificada de cada bug do Defects4J (versão com bug).
# Considera só bugs cujo patch altera exatamente 1 classe.
# Saída CSV: projeto,bug,classe,linhas_totais,ncloc
# NCLOC = linhas que não são vazias nem só comentário (contagem aproximada).
# Uso (dentro do container): bash scripts/tamanho_classes.sh Lang Math > saida.csv
set -euo pipefail

declare -A REPO=([Lang]=commons-lang.git [Math]=commons-math.git)

ncloc() {
  awk '
    { line = $0 }
    incomment { if (sub(/.*\*\//, "", line)) incomment = 0; else next }
    { gsub(/\/\*.*\*\//, "", line) }
    sub(/\/\*.*/, "", line) { incomment = 1 }
    { sub(/\/\/.*/, "", line) }
    line ~ /[^[:space:]]/ { n++ }
    END { print n + 0 }'
}

echo "projeto,bug,classe,linhas_totais,ncloc"
for p in "$@"; do
  repo="$D4J_HOME/project_repos/${REPO[$p]}"
  defects4j query -p "$p" -q "classes.modified,revision.id.buggy" | tr -d '"' |
  while IFS=, read -r id classes rev; do
    [[ "$classes" == *";"* ]] && continue
    rel="$(tr . / <<< "$classes").java"
    path=$(git -C "$repo" ls-tree -r --name-only "$rev" | grep "/$rel\$" | grep -v '/test/' | head -1)
    src=$(git -C "$repo" show "$rev:$path")
    echo "$p,$id,$classes,$(wc -l <<< "$src"),$(ncloc <<< "$src")"
  done
done
