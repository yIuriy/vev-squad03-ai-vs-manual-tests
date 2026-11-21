import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class QuebradoTest {
    @Test
    public void metodoInexistente() {
        new LookupTranslator(new String[][] { { "a", "b" } }).metodoQueNaoExiste();
    }
}
