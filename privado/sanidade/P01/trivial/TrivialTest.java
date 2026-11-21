import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class TrivialTest {
    @Test
    public void constroi() {
        assertNotNull(new LookupTranslator(new String[][] { { "a", "b" } }));
    }
}
