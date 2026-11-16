import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regra uniforme de neutralização das classes do kit (ver docs/neutralizacao_kit.md):
 *  1. Remove todos os comentários de linha (//). Se a linha fica vazia, a linha inteira sai.
 *  2. Dentro de comentários de bloco e Javadoc, troca referências a issues (LANG-123, MATH-123
 *     e links do JIRA) por "[ref. removida]". O resto do Javadoc fica intacto.
 * Strings e caracteres literais nunca são alterados.
 * Cada remoção é impressa no stdout: linha<TAB>tipo<TAB>texto removido
 *
 * Uso: java scripts/Neutralizar.java <entrada.java> <saida.java>
 */
public class Neutralizar {

    private static final Pattern ISSUE = Pattern.compile(
            "https?://issues\\.apache\\.org/[^\\s)<>\"]*[^\\s)<>\".,;]|\\b(?:LANG|MATH)-\\d+\\b");
    private static final String MARCA = "[ref. removida]";

    public static void main(String[] args) throws Exception {
        List<String> linhas = Files.readAllLines(Path.of(args[0]), StandardCharsets.UTF_8);
        List<String> saida = new ArrayList<>();
        boolean emBloco = false;

        for (int n = 0; n < linhas.size(); n++) {
            String linha = linhas.get(n);
            StringBuilder nova = new StringBuilder();
            boolean removeuLinha = false;
            int i = 0;
            while (i < linha.length()) {
                if (emBloco) {
                    int fim = linha.indexOf("*/", i);
                    int corte = fim < 0 ? linha.length() : fim + 2;
                    nova.append(semIssues(linha.substring(i, corte), n + 1));
                    emBloco = fim < 0;
                    i = corte;
                    continue;
                }
                char c = linha.charAt(i);
                if (linha.startsWith("//", i)) {
                    System.out.println((n + 1) + "\tcomentario_linha\t" + linha.substring(i).trim());
                    removeuLinha = true;
                    break;
                } else if (linha.startsWith("/*", i)) {
                    emBloco = true;
                    nova.append("/*");
                    i += 2;
                } else if (c == '"' || c == '\'') {
                    int j = i + 1;
                    while (j < linha.length() && linha.charAt(j) != c) {
                        j += linha.charAt(j) == '\\' ? 2 : 1;
                    }
                    j = Math.min(j + 1, linha.length());
                    nova.append(linha, i, j);
                    i = j;
                } else {
                    nova.append(c);
                    i++;
                }
            }
            String resultado = removeuLinha ? rtrim(nova.toString()) : nova.toString();
            if (!(removeuLinha && resultado.isEmpty())) {
                saida.add(resultado);
            }
        }
        Files.write(Path.of(args[1]), saida, StandardCharsets.UTF_8);
    }

    private static String semIssues(String trecho, int numLinha) {
        Matcher m = ISSUE.matcher(trecho);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            System.out.println(numLinha + "\treferencia_issue\t" + m.group());
            m.appendReplacement(sb, Matcher.quoteReplacement(MARCA));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String rtrim(String s) {
        int fim = s.length();
        while (fim > 0 && Character.isWhitespace(s.charAt(fim - 1))) {
            fim--;
        }
        return s.substring(0, fim);
    }
}
