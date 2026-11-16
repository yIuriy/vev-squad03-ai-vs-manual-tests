import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class AssertErradoTest {
    @Test
    public void esperaValorErrado() {
        assertEquals("x", new LookupTranslator(new String[][] { { "a", "b" } }).translate("a"));
    }

    @Test
    public void esperaValorCerto() {
        assertEquals("b", new LookupTranslator(new String[][] { { "a", "b" } }).translate("a"));
    }
}
