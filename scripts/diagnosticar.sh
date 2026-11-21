#!/usr/bin/env bash
# Análise complementar de uma suíte já medida (não altera nenhum resultado do medir.sh):
#   1. Mensagens de falha: roda, nas duas versões, cada teste que o medir.sh não classificou
#      como PASSA e grava a exceção, a mensagem e o local da falha.
#   2. Cobertura do patch: compara a classe sob teste nas versões com bug (b) e corrigida (f)
#      e diz quantas linhas alteradas pelo patch são executáveis e quantas a suíte executou,
#      usando os arquivos jacoco-<v>-1.exec gravados pelo medir.sh.
#
# Uso (dentro do container): bash scripts/diagnosticar.sh <codigo> <condicao>
#   ex.: bash scripts/diagnosticar.sh F01 ia
# Saída: privado/medicao/diagnostico/<codigo>/<condicao>/{falhas.tsv,patch.tsv,linhas_patch.tsv}
set -uo pipefail

codigo=$1; cond=$2
LIMITE_TESTE=30
suite=/work/privado/medicao/entrada/$codigo/$cond
res=/work/privado/medicao/resultados/$codigo/$cond
out=/work/privado/medicao/diagnostico/$codigo/$cond
rm -rf "$out"; mkdir -p "$out"

linha=$(cat /work/privado/amostra.csv /work/privado/piloto.csv | grep "^$codigo,")
IFS=, read -r _ p id classe _ <<< "$linha"
pacote=${classe%.*}; simples=${classe##*.}
base=/tmp/med/$codigo
JUNIT=$D4J_HOME/framework/projects/lib/junit-4.12-hamcrest-1.3.jar

declare -A SRC
for v in b f; do
  w=$base/$v
  if [ ! -d "$w" ]; then
    defects4j checkout -p "$p" -v "$id$v" -w "$w" >/dev/null 2>&1 && (cd "$w" && defects4j compile >/dev/null 2>&1) \
      || { echo "erro: checkout/compilacao de $p-$id$v falhou" >&2; exit 1; }
  fi
  SRC[$v]=$w/$(cd "$w" && defects4j export -p dir.src.classes 2>/dev/null)/$(tr . / <<< "$pacote")/$simples.java
done

# 1. Mensagens de falha
alvos=$(awk -F, 'NR>1 && $NF != "PASSA" { print $1 }' "$res/testes.csv" | sort)
printf 'codigo\tcondicao\tteste\tversao\tstatus\texcecao\tmensagem\tlocal\n' > "$out/falhas.tsv"
if [ -n "$alvos" ]; then
  for v in b f; do
    w=$base/$v
    cp=$(cd "$w" && defects4j export -p cp.test 2>/dev/null | tr : '\n' | grep -v '/target/tests$' | paste -sd:)
    bin=$(mktemp -d)
    javac -nowarn -encoding UTF-8 -cp "$cp" -d "$bin" $(find "$suite" -name '*.java') >/dev/null 2>&1 \
      && javac -nowarn -cp "$JUNIT" -d "$bin" /work/scripts/Diagnostico.java >/dev/null 2>&1 \
      || { echo "erro: compilação da suíte em $v" >&2; exit 1; }
    java -Xmx512m -cp "$bin:$cp" Diagnostico "$LIMITE_TESTE" $alvos 2>/dev/null |
      sed -n "s/^DIAG\t/$codigo\t$cond\t/p" | awk -F'\t' -v OFS='\t' -v v="$v" '{ $3 = $3 "\t" v; print }' >> "$out/falhas.tsv"
    rm -rf "$bin"
  done
fi

# 2. Cobertura do patch
# Linhas alteradas: na versão f, as linhas novas ou modificadas; na versão b, as removidas ou modificadas.
diff --unchanged-line-format= --old-line-format='b	%dn
' --new-line-format='f	%dn
' "${SRC[b]}" "${SRC[f]}" > "$out/linhas_patch.tsv"

printf 'codigo\tcondicao\tversao\tlinhas_patch\texecutaveis\texecutadas\n' > "$out/patch.tsv"
for v in b f; do
  w=$base/$v
  dir=$w/target/classes/$(tr . / <<< "$pacote")
  xml=$(mktemp)
  java -jar "$JACOCO_HOME/lib/jacococli.jar" report "$res/jacoco-$v-1.exec" --quiet \
       --classfiles "$dir/$simples.class" $(ls "$dir/$simples\$"*.class 2>/dev/null | sed 's/^/--classfiles /') \
       --xml "$xml" >/dev/null 2>&1
  # Bloco <sourcefile name="Classe.java"> ... </sourcefile>, uma entrada <line nr= mi= ci=> por linha executável
  tr '<' '\n' < "$xml" | awk -v alvo="sourcefile name=\"$simples.java\"" '
      index($0, alvo) == 1 { dentro = 1; next }
      /^\/sourcefile/ { dentro = 0 }
      dentro && /^line / { match($0, /nr="[0-9]+"/); nr = substr($0, RSTART + 4, RLENGTH - 5)
                           match($0, /ci="[0-9]+"/); ci = substr($0, RSTART + 4, RLENGTH - 5)
                           print nr "\t" ci }' > "$xml.linhas"
  awk -F'\t' -v v="$v" -v c="$codigo" -v cond="$cond" '
      FNR == NR { ci[$1] = $2; next }
      $1 == v   { tot++; if ($2 in ci) { exe++; if (ci[$2] > 0) cob++ } }
      END { print c "\t" cond "\t" v "\t" tot + 0 "\t" exe + 0 "\t" cob + 0 }' "$xml.linhas" "$out/linhas_patch.tsv" >> "$out/patch.tsv"
  rm -f "$xml" "$xml.linhas"
done
cat "$out/patch.tsv" | tail -2
