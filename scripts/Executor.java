import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

/**
 * Executa cada teste de uma suíte isoladamente, com limite de tempo por teste, e imprime
 * uma linha por teste: RESULTADO<TAB>status<TAB>Classe::metodo
 * Status: PASS, FAIL, TIMEOUT, IGNORADO, ERRO_CLASSE.
 * Uso: java Executor <limite_segundos> <classe_de_teste>...
 */
public class Executor {

    public static void main(String[] args) throws Exception {
        long limite = Long.parseLong(args[0]) * 1000;
        for (int i = 1; i < args.length; i++) {
            Class<?> classe;
            List<Description> testes;
            try {
                classe = Class.forName(args[i]);
                testes = folhas(Request.aClass(classe).getRunner().getDescription());
            } catch (Throwable e) {
                imprime("ERRO_CLASSE", args[i] + "::*");
                continue;
            }
            for (Description teste : testes) {
                executa(classe, teste, limite);
            }
        }
        // Encerra mesmo com testes travados em threads daemon; o JaCoCo grava a cobertura no shutdown
        System.exit(0);
    }

    private static List<Description> folhas(Description d) {
        List<Description> lista = new ArrayList<>();
        if (d.isTest()) {
            lista.add(d);
        }
        for (Description filho : d.getChildren()) {
            lista.addAll(folhas(filho));
        }
        return lista;
    }

    private static void executa(Class<?> classe, Description teste, long limite) throws InterruptedException {
        Result[] resultado = new Result[1];
        Thread t = new Thread(() -> resultado[0] = new JUnitCore().run(Request.aClass(classe).filterWith(teste)));
        t.setDaemon(true);
        t.start();
        t.join(limite);

        String status;
        Result r = resultado[0];
        if (r == null) {
            status = "TIMEOUT";
        } else if (r.getIgnoreCount() > 0) {
            status = "IGNORADO";
        } else if (r.wasSuccessful() && r.getRunCount() > 0) {
            status = "PASS";
        } else {
            status = "FAIL";
        }
        imprime(status, teste.getClassName() + "::" + teste.getMethodName());
    }

    private static void imprime(String status, String nome) {
        System.out.println("RESULTADO\t" + status + "\t" + nome);
    }
}
