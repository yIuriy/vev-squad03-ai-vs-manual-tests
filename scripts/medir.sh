#!/usr/bin/env bash
# Mede uma suíte de testes contra uma falha: roda a suíte na versão com bug (b) e na
# corrigida (f), N vezes cada, com JaCoCo restrito à classe sob teste.
#
# Uso (dentro do container): bash scripts/medir.sh <codigo> <dir_suite> <dir_saida> [repeticoes]
#   codigo:     F01..F12 ou P01..P02 (resolvido em privado/amostra.csv e privado/piloto.csv)
#   dir_suite:  pasta com os .java da suíte (o nome da pasta vira o rótulo da suíte)
#
# Por teste (testes.csv), a classificação é:
#   DETECTA       falha na versão com bug e passa na corrigida, em todas as repetições
#   FALSO_ALARME  falha na versão corrigida em todas as repetições
#   INSTAVEL      resultado muda entre repetições
#   PASSA         passa nas duas versões
#   NAO_EXECUTADO ignorado (@Ignore) ou a classe de teste não carregou
# A suíte detecta a falha se tem pelo menos 1 teste DETECTA.
#
# Saída no stdout, 1 linha CSV:
#   codigo,suite,compila_b,compila_f,testes,detecta,detectores,falsos_alarmes,instaveis,
#   linhas_cob_b,linhas_tot_b,branches_cob_b,branches_tot_b,linhas_cob_f,linhas_tot_f,branches_cob_f,branches_tot_f
set -uo pipefail

codigo=$1; suite=$(realpath "$2"); saida=$3; reps=${4:-3}
LIMITE_TESTE=30  # segundos por teste
rotulo=$(basename "$suite")

linha=$(cat /work/privado/amostra.csv /work/privado/piloto.csv | grep "^$codigo,")
IFS=, read -r _ p id classe _ <<< "$linha"
pacote=${classe%.*}; simples=${classe##*.}

out=$saida/$codigo/$rotulo
rm -rf "$out"; mkdir -p "$out"
base=/tmp/med/$codigo
JUNIT=$D4J_HOME/framework/projects/lib/junit-4.12-hamcrest-1.3.jar

declare -A COMPILA
cobertura() {  # cob_linhas,tot_linhas,cob_branches,tot_branches da classe sob teste (inclui classes internas)
  local w=$1 exec=$2 dir
  dir=$w/target/classes/$(tr . / <<< "$pacote")
  java -jar "$JACOCO_HOME/lib/jacococli.jar" report "$exec" --quiet \
       --classfiles "$dir/$simples.class" $(ls "$dir/$simples\$"*.class 2>/dev/null | sed 's/^/--classfiles /') \
       --csv "$out/cobertura.csv" >/dev/null
  awk -F, 'NR>1 { bm+=$6; bc+=$7; lm+=$8; lc+=$9 } END { print lc","lc+lm","bc","bc+bm }' "$out/cobertura.csv"
}

for v in b f; do
  w=$base/$v
  if [ ! -d "$w" ]; then
    defects4j checkout -p "$p" -v "$id$v" -w "$w" >/dev/null 2>&1 && (cd "$w" && defects4j compile >/dev/null 2>&1) \
      || { echo "erro: checkout/compilacao de $p-$id$v falhou" >&2; exit 1; }
  fi
  # Classpath do projeto sem os testes originais dele
  cp=$(cd "$w" && defects4j export -p cp.test 2>/dev/null | tr : '\n' | grep -v '/target/tests$' | paste -sd:)

  bin=$out/bin-$v; mkdir -p "$bin"
  javac -nowarn -encoding UTF-8 -cp "$cp" -d "$bin" $(find "$suite" -name '*.java') > "$out/javac-$v.log" 2>&1 \
    && javac -nowarn -cp "$JUNIT" -d "$bin" /work/scripts/Executor.java >> "$out/javac-$v.log" 2>&1
  if [ $? -ne 0 ]; then COMPILA[$v]=nao; continue; fi
  COMPILA[$v]=sim

  # Classes de teste: arquivos da suíte que têm @Test ou estendem TestCase
  testes=$(grep -lE '@Test|extends +TestCase' $(find "$suite" -name '*.java') |
    while read -r f; do
      pkg=$(sed -n 's/^ *package \+\([a-zA-Z0-9_.]*\) *;.*/\1/p' "$f" | head -1)
      echo "${pkg:+$pkg.}$(basename "$f" .java)"
    done)

  for r in $(seq 1 "$reps"); do
    java -Xmx512m -javaagent:"$JACOCO_HOME/lib/jacocoagent.jar=destfile=$out/jacoco-$v-$r.exec,includes=$classe:$classe\$*" \
         -cp "$bin:$cp" Executor "$LIMITE_TESTE" $testes 2>/dev/null |
      sed -n 's/^RESULTADO\t//p' | awk -F'\t' '{ print $2 "\t" $1 }' | sort > "$out/resultado-$v-$r.tsv"
  done
  cobertura "$w" "$out/jacoco-$v-1.exec" > "$out/cobertura-$v.txt"
done

if [ "${COMPILA[b]}" != sim ] || [ "${COMPILA[f]}" != sim ]; then
  echo "$codigo,$rotulo,${COMPILA[b]},${COMPILA[f]},,,,,,,,,,,,,"
  exit 0
fi

# Junta os resultados por teste: colunas b1..bN, f1..fN e a classificação
files=$(for v in b f; do for r in $(seq 1 "$reps"); do echo "$out/resultado-$v-$r.tsv"; done; done)
awk -F'\t' -v reps="$reps" '
  { arq = FILENAME; st[$1, arq] = $2; testes[$1] = 1; if (!(arq in ordem)) { ordem[arq] = ++n; nomes[n] = arq } }
  END {
    cab = "teste"; for (i = 1; i <= n; i++) cab = cab "," (i <= reps ? "b" i : "f" (i - reps)); print cab ",classificacao"
    for (t in testes) {
      linha = t; fb = 0; ff = 0; pb = 0; pf = 0
      for (i = 1; i <= n; i++) {
        s = ((t, nomes[i]) in st) ? st[t, nomes[i]] : "AUSENTE"; linha = linha "," s
        falhou = (s == "FAIL" || s == "TIMEOUT"); passou = (s == "PASS")
        if (i <= reps) { fb += falhou; pb += passou } else { ff += falhou; pf += passou }
      }
      if (fb == reps && pf == reps) c = "DETECTA"
      else if (ff == reps) c = "FALSO_ALARME"
      else if (pb == reps && pf == reps) c = "PASSA"
      else if (fb + pb + ff + pf == 0) c = "NAO_EXECUTADO"
      else c = "INSTAVEL"
      print linha "," c
    }
  }' $files > "$out/testes.csv"

conta() { grep -c ",$1\$" "$out/testes.csv"; }
total=$(($(wc -l < "$out/testes.csv") - 1))
det=$(conta DETECTA)
echo "$codigo,$rotulo,sim,sim,$total,$([ "$det" -gt 0 ] && echo sim || echo nao),$det,$(conta FALSO_ALARME),$(conta INSTAVEL),$(cat "$out/cobertura-b.txt"),$(cat "$out/cobertura-f.txt")"
