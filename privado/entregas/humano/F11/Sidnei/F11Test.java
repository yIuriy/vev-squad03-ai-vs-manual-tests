// inicio: 16:40  fim: 17:10
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;

public class F11Test {
    @Test
    public void translatesDecimalEntity(){
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertEquals("A", unescaper.translate("&#65;"));
    }

    @Test
    public void translatesHexEntity(){
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertEquals("A", unescaper.translate("&#x41;"));
        assertEquals("A", unescaper.translate("&#X41;"));

    }

    @Test
    public void translatesEntityInsideText(){
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertEquals("aAb", unescaper.translate("a&#65;b"));

    }

    @Test
    public void translatesSuplementaryCodePoint(){
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertEquals("\uD834\uDD1E", unescaper.translate("&#x1D11E;"));

    }

    @Test
    public void leavesUnterminatedEntityUnchanged(){
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertEquals("Test &#65 end", unescaper.translate("Test &#65 end"));

    }
}
