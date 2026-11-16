// inicio: __:__  fim: __:__
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class P01Test {
    private LookupTranslator lookupTranslator = new LookupTranslator();

    @Test
    public void trocaCaractereQueEstaNaTabela() {
        LookupTranslator t = new LookupTranslator(new String[][] { { "a", "1" } });
        assertEquals("1", t.translate("a"));
    }
    
    @Test
    public void trocaCaractereQueEstaNaTabela() {
        LookupTranslator t = new LookupTranslator(new String[][] { { "aaa", "1" } });
        assertEquals("1", t.translate("aaa"));
    }    
}
