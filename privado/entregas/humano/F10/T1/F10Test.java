// inicio: 15:25  fim: __:__
package org.apache.commons.lang;

import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;

import org.junit.Before;

public class F10Test {

    @Before
    public void prepara(){

    }

    @Test
    public void testLocaleWithNull(){
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testValidLanguage(){
        assertEquals(new locale("pt"), LocaleUtils.toLocale("pt"));
    }

    @Test
    public void testInvalidLocale(){
        try {
            LocaleUtils.toLocale("abc");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testEmptyLocale(){
        try {
            LocaleUtils.toLocale("");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
