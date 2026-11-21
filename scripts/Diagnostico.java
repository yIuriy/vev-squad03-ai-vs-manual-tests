import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Roda testes individuais e mostra por que falharam (exceção, mensagem e o primeiro quadro da
 * pilha fora do JUnit). Serve só para diagnóstico: a classificação dos testes vem do medir.sh.
 *
 * Uso: java Diagnostico <limite_segundos> <Classe::metodo>...
 * Saída, uma linha por teste: DIAG<TAB>teste<TAB>status<TAB>excecao<TAB>mensagem<TAB>local
 */
public class Diagnostico {
    public static void main(String[] args) throws Exception {
        int limite = Integer.parseInt(args[0]);
        for (int i = 1; i < args.length; i++) {
            String[] partes = args[i].split("::", 2);
            String status = "PASS", excecao = "", mensagem = "", local = "";
            ExecutorService ex = Executors.newSingleThreadExecutor(r -> { Thread t = new Thread(r); t.setDaemon(true); return t; });
            try {
                Class<?> classe = Class.forName(partes[0]);
                Future<Result> f = ex.submit(() -> new JUnitCore().run(Request.method(classe, partes[1])));
                Result r = f.get(limite, TimeUnit.SECONDS);
                if (!r.getFailures().isEmpty()) {
                    Failure falha = r.getFailures().get(0);
                    Throwable t = falha.getException();
                    status = "FAIL";
                    excecao = t.getClass().getName();
                    mensagem = String.valueOf(t.getMessage());
                    for (StackTraceElement e : t.getStackTrace()) {
                        String c = e.getClassName();
                        if (!c.startsWith("org.junit") && !c.startsWith("java.") && !c.startsWith("jdk.") && !c.startsWith("sun.")) {
                            local = e.toString();
                            break;
                        }
                    }
                }
            } catch (java.util.concurrent.TimeoutException e) {
                status = "TIMEOUT";
            } catch (Throwable t) {
                status = "ERRO";
                excecao = t.getClass().getName();
                mensagem = String.valueOf(t.getMessage());
            } finally {
                ex.shutdownNow();
            }
            System.out.println("DIAG\t" + args[i] + "\t" + status + "\t" + excecao + "\t" + limpa(mensagem) + "\t" + local);
        }
        System.exit(0);
    }

    private static String limpa(String s) {
        s = s.replaceAll("[\\t\\r\\n]+", " ");
        return s.length() > 300 ? s.substring(0, 300) + "..." : s;
    }
}
