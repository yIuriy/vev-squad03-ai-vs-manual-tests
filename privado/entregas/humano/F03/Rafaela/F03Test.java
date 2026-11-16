// inicio: 15:25  fim: 15:55
package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class F03Test {
    public StopWatch obj;

    @Before
    public void prepara(){
        obj = new StopWatch();
        }

    @Test
    public void tempoAtualValido(){
        obj.start();
        assertTrue(obj.getTime() > 2);
    }

    @Test
    public void duracaoString(){
        public String numero = obj.toString();
        assertTrue(string == String);
    }

    @Test
    public void testeSuspende(){
        obj.start();
        obj.wait(300);
        obj.suspend();
        assertTrue(obj.getTime() == 3);
    }

}
