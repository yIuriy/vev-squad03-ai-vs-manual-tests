// inicio: 15:25  fim: 15:55
package org.apache.commons.lang;

import static org.junit.Assert.*;

import org.junit.Test;

public class F07Test {
    @Test
    public void verificarFalsidade() {
        assertTrue(BooleanUtils.negate(Boolean.FALSE));
    }

    @Test
    public void NegativoNãoEFalso() {
        assertFalse(BooleanUtils.negate(Boolean.TRUE));
    }

    @Test
    public void verdadeiroEVerdadeiro() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
    }

    @Test
    public void toBooleanObjectTest() {
        assertFalse(BooleanUtils.toBooleanObject(false));
        assertTrue(BooleanUtils.toBooleanObject(true));
    }

    @Test
    public void xorTest() {
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.FALSE }));
        assertTrue(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE }));
    }
    

}
