#!/usr/bin/env bash
# Gera o kit entregue aos testadores e operadores de IA: para cada falha, só o código-fonte
# da classe sob teste na versão com bug (sem patch e sem os testes do projeto) e um LEIA-ME
# que não revela o projeto nem o ID do bug. A classe passa pela regra uniforme de
# neutralização (scripts/Neutralizar.java); cada remoção vai para o relatório.
# Uso (dentro do container): bash scripts/gerar_kit.sh <dir_saida> <relatorio.tsv>
set -euo pipefail

saida=$1; relatorio=$2

rm -rf "$saida"; mkdir -p "$saida"
cp /work/docs/instrucoes_kit.md "$saida/INSTRUCOES.md"
echo -e "codigo\tlinha_original\ttipo\ttexto_removido" > "$relatorio"

tail -q -n +2 /work/privado/amostra.csv /work/privado/piloto.csv |
while IFS=, read -r codigo p id classe _; do
  # A versão com bug do Defects4J (checkout "b") é a corrigida com o patch revertido, que pode
  # diferir do commit original revision.id.buggy. O kit precisa ser a mesma versão medida.
  w=/tmp/kit/$codigo
  defects4j checkout -p "$p" -v "${id}b" -w "$w" >/dev/null 2>&1
  src=$(cd "$w" && defects4j export -p dir.src.classes 2>/dev/null)
  simples=${classe##*.}

  mkdir -p "$saida/$codigo"
  java /work/scripts/Neutralizar.java "$w/$src/$(tr . / <<< "$classe").java" "$saida/$codigo/$simples.java" |
    sed "s/^/$codigo\t/" >> "$relatorio"
  cat > "$saida/$codigo/LEIA-ME.md" <<EOF
# $codigo

- Classe sob teste: \`$simples\` (arquivo \`$simples.java\` nesta pasta)
- Pacote: \`${classe%.*}\`
- Entrega: um arquivo \`${codigo}Test.java\` com a classe pública \`${codigo}Test\`, no pacote \`${classe%.*}\`
- Regras completas: \`INSTRUCOES.md\` na raiz do kit
EOF
done

(cd "$saida" && find . -type f ! -name SHA256SUMS | sort | xargs sha256sum > SHA256SUMS)
