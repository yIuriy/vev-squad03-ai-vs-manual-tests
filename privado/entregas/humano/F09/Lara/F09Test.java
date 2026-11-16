// inicio: 16:00  fim: 16:30
package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;


public class F09Test {

    @Test
    public void testCloneNull(){
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testCloneString(){
        String texto = SerializationUtils.clone("test");
        assertEquals("test", texto);
    }

    @Test
    public void testSerializeString(){
        byte bytes = SerializationUtils.serialize("teste");
        assertNotNull(bytes);
    }

    @Test
    public void testCloneInteger(){
        Integer numero = SerializationUtils.clone(10);
        assertEquals(Integer.valueOf(10), numero);
    }
}
