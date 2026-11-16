// inicio: 18:10  fim: 18:25
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;
import java.io.StringWriter;

public class P01Test {

    @Test
    public void trocaChaveQueEstaNaTabela() {
        LookupTranslator t = new LookupTranslator(new String[][] { { "a", "1" } });
        assertEquals("1", t.translate("a"));
    }

    @Test
    public void textoSemChaveFicaIgual() {
        LookupTranslator t = new LookupTranslator(new String[][] { { "a", "1" } });
        assertEquals("xyz", t.translate("xyz"));
    }

    @Test
    public void textoVazioFicaVazio() {
        LookupTranslator t = new LookupTranslator(new String[][] { { "a", "1" } });
        assertEquals("", t.translate(""));
    }

    @Test
    public void prefereAChaveMaisLonga() {
        LookupTranslator t = new LookupTranslator(new String[][] { { "a", "1" }, { "ab", "2" } });
        assertEquals("2", t.translate("ab"));
    }

    @Test
    public void chaveQueNaoEhStringTambemFunciona() {
        LookupTranslator t = new LookupTranslator(new CharSequence[][] { { new StringBuilder("a"), "1" } });
        assertEquals("1", t.translate("a"));
    }

    @Test
    public void traduzIndiceComWriter() throws Exception {
        LookupTranslator t = new LookupTranslator(new String[][] { { "a", "1" } });
        StringWriter out = new StringWriter();
        assertEquals(1, t.translate("xa", 1, out));
        assertEquals("1", out.toString());
    }
}
