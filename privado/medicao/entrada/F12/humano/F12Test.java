// inicio: 16:15  fim: 16:45
package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.junit.Test;

public class F12Test {

    @Test
    public void addValueTest() {     
        Frequency freq = new Frequency();
        Long frequencia = freq.getSumFreq();
        freq.addValue(10);
        assertTrue(frequencia < freq.getSumFreq());
    }

    @Test
    public void clearTest() {     
        Frequency freq = new Frequency();
        Long frequencia = freq.getSumFreq();
        freq.addValue(10);
        freq.clear();
        assertEquals((long) frequencia, freq.getSumFreq());
    }

    @Test
    public void getSumFreqTest() {     
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(10);
        freq.addValue(5);
        assertEquals(freq.getSumFreq(), 25);
    }

    
}
