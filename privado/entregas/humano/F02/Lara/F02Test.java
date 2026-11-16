// inicio: 16:15  fim: 16:45
package org.apache.commons.math.fraction;

import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;

import.org.junit.Bafore

public class F02Test {

    @Before
    public void prepara(){

    }

    @Test
    public void testZeroDenominator(){
        try{
            Fraction fraction = new Fraction(2, 0);
            fail();
        }
        catch (ArithmeticException){
        }

    }

    @Test
    public void testNegativeDenominator(){
        Fraction fraction = new Fraction(2, -3);
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator(){
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
    }

    @Test
    public void testGetDenominator(){
        Fraction fraction = new Fraction(2,3);
        assertEquals(4, fraction.getDenominator());     
    }

    @Test
    public void testFractionInDouble(){
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2.5, fraction.doubleValue());       
    }

    @Test
    public void testIntValue(){
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.intValue());        
    }

}
