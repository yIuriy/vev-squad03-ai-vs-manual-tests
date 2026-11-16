#!/usr/bin/env bash
# Gera o kit entregue aos testadores e operadores de IA. Cada falha vira um projeto pronto para o
# VS Code (perfil em docs/perfil_vscode.md):
#   src/   classe sob teste na versão com bug, passada pela regra de neutralização
#          (scripts/Neutralizar.java); cada remoção vai para o relatório
#   test/  esqueleto <código>Test.java
#   lib/   resto do projeto compilado (sem a classe sob teste) e o JUnit, só para o compilador
#   .vscode/  configuração do projeto, nível Java 11 e avisos do compilador desligados
# Nada revela o ID do bug, e o kit não traz o patch nem os testes do projeto.
# Uso (dentro do container): bash scripts/gerar_kit.sh <dir_saida> <relatorio.tsv>
set -euo pipefail

saida=$1; relatorio=$2
JUNIT=$D4J_HOME/framework/projects/lib/junit-4.12-hamcrest-1.3.jar

rm -rf "$saida"; mkdir -p "$saida"
cp /work/docs/instrucoes_kit.md "$saida/INSTRUCOES.md"
cp /work/docs/perfil_vscode.md "$saida/PERFIL_VSCODE.md"
cp /work/docs/junit_cola.md "$saida/JUNIT.md"
echo -e "codigo\tlinha_original\ttipo\ttexto_removido" > "$relatorio"

tail -q -n +2 /work/privado/amostra.csv /work/privado/piloto.csv |
while IFS=, read -r codigo p id classe _; do
  # A versão com bug do Defects4J (checkout "b") é a corrigida com o patch revertido, que pode
  # diferir do commit original revision.id.buggy. O kit precisa ser a mesma versão medida.
  w=/tmp/kit/$codigo
  defects4j checkout -p "$p" -v "${id}b" -w "$w" >/dev/null 2>&1
  (cd "$w" && defects4j compile >/dev/null 2>&1)
  src=$(cd "$w" && defects4j export -p dir.src.classes 2>/dev/null)
  bin=$(cd "$w" && defects4j export -p dir.bin.classes 2>/dev/null)
  pacote=${classe%.*}; simples=${classe##*.}; dir_pacote=$(tr . / <<< "$pacote")
  f=$saida/$codigo

  mkdir -p "$f/src/$dir_pacote" "$f/test/$dir_pacote" "$f/lib" "$f/.vscode"
  java /work/scripts/Neutralizar.java "$w/$src/$dir_pacote/$simples.java" "$f/src/$dir_pacote/$simples.java" |
    sed "s/^/$codigo\t/" >> "$relatorio"

  # Biblioteca: classes compiladas do projeto, menos a classe sob teste (que vem do src/)
  rm -f "$w/$bin/$dir_pacote/$simples.class" "$w/$bin/$dir_pacote/$simples\$"*.class
  (cd "$w/$bin" && jar cf "$f/lib/dependencias.jar" .)
  cp "$JUNIT" "$f/lib/"

  cat > "$f/test/$dir_pacote/${codigo}Test.java" <<EOF
// inicio: __:__  fim: __:__
package $pacote;

import static org.junit.Assert.*;

import org.junit.Test;

public class ${codigo}Test {

}
EOF

  cat > "$f/.vscode/settings.json" <<'EOF'
{
    "java.project.sourcePaths": ["src", "test"],
    "java.project.referencedLibraries": ["lib/**/*.jar"],
    "java.project.outputPath": "bin",
    "java.settings.url": ".vscode/java.prefs",
    "java.compile.nullAnalysis.mode": "disabled",
    "java.import.gradle.enabled": false,
    "java.import.maven.enabled": false,
    "editor.inlineSuggest.enabled": false
}
EOF

  # Nível Java 11 e todos os avisos (warnings) desligados: só erros reais de compilação aparecem
  {
    echo "org.eclipse.jdt.core.compiler.source=11"
    echo "org.eclipse.jdt.core.compiler.compliance=11"
    echo "org.eclipse.jdt.core.compiler.codegen.targetPlatform=11"
    for aviso in assertIdentifier autoboxing comparingIdentical deadCode deprecation \
        discouragedReference emptyStatement enumIdentifier fallthroughCase fieldHiding \
        finalParameterBound finallyBlockNotCompletingNormally forbiddenReference \
        hiddenCatchBlock incompatibleNonInheritedInterfaceMethod indirectStaticAccess \
        localVariableHiding missingDefaultCase missingHashCodeMethod missingSerialVersion \
        noEffectAssignment nonExternalizedStringLiteral nullReference \
        possibleAccidentalBooleanAssignment potentialNullReference \
        potentiallyUnclosedCloseable rawTypeReference redundantNullCheck \
        redundantSuperinterface staticAccessReceiver syntheticAccessEmulation \
        typeParameterHiding unclosedCloseable uncheckedTypeOperation \
        unlikelyCollectionMethodArgumentType unlikelyEqualsArgumentType \
        unnecessaryElse unnecessaryTypeCheck unqualifiedFieldAccess \
        unusedDeclaredThrownException unusedImport unusedLabel unusedLocal \
        unusedObjectAllocation unusedParameter unusedPrivateMember unusedTypeParameter \
        unusedWarningToken varargsArgumentNeedCast; do
      echo "org.eclipse.jdt.core.compiler.problem.$aviso=ignore"
    done
  } > "$f/.vscode/java.prefs"

  cat > "$f/LEIA-ME.md" <<EOF
# $codigo

- Classe sob teste: \`$simples\`, em \`src/$dir_pacote/$simples.java\` (não altere)
- Escreva os testes em: \`test/$dir_pacote/${codigo}Test.java\` (classe \`${codigo}Test\`, pacote \`$pacote\`)
- Abra **esta pasta** no perfil \`Experimento\` do VS Code
- Regras completas: \`INSTRUCOES.md\` na raiz do kit
EOF
done

(cd "$saida" && find . -type f ! -name SHA256SUMS | LC_ALL=C sort | xargs sha256sum > SHA256SUMS)
