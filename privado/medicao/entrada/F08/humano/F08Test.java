// inicio: 16:15  fim: 16:45
package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import org.junit.Test;

public class F08Test {

    @Test
    public void testeSubUmArgu() {
        ExtendedMessageFormat formato = new ExtendedMessageFormat("Oie, {0}!!");

        String resultado = formato.format(new Object[] { "T2" });

        assertEquals("Oie, T2!!", resultado);
    }

    @Test
    public void testeSubDoisArgu() {
        ExtendedMessageFormat formato = new ExtendedMessageFormat("{0} tem {1} anos");

        String resultado = formato.format(new Object[] { "T2", 25 });

        assertEquals("T2 tem 25 anos", resultado);
    }

    @Test
    public void testeSemArgu() {
        ExtendedMessageFormat formato = new ExtendedMessageFormat("Não gosto de testes!");

        String resultado = formato.format(new Object[] {});

        assertEquals("Não gosto de testes!", resultado);
    }

    @Test
    public void testePadVazio() {
        ExtendedMessageFormat formato = new ExtendedMessageFormat("");

        String resultado = formato.format(new Object[] {});

        assertEquals("", resultado);
    }

    @Test
    public void testeAlterarPad() {
        ExtendedMessageFormat formato = new ExtendedMessageFormat("Oie, {0}");

        formato.applyPattern("bye bye, {0}");

        String resultado = formato.format(new Object[] { "Gian" });

        assertEquals("bye bye, Gian", resultado);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testeIndiceInvalido() {
        new ExtendedMessageFormat("Olá, {abc}");
    }

}
