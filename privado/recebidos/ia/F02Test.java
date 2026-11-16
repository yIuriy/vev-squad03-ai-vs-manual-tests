package org.apache.commons.math.fraction;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class F02Test {

    @Test
    public void testConstantZero() {
        assertEquals(0, Fraction.ZERO.getNumerator());
        assertEquals(1, Fraction.ZERO.getDenominator());
    }

    @Test
    public void testConstantOne() {
        assertEquals(1, Fraction.ONE.getNumerator());
        assertEquals(1, Fraction.ONE.getDenominator());
    }

    @Test
    public void testConstantTwo() {
        assertEquals(2, Fraction.TWO.getNumerator());
        assertEquals(1, Fraction.TWO.getDenominator());
    }

    @Test
    public void testConstantMinusOne() {
        assertEquals(-1, Fraction.MINUS_ONE.getNumerator());
        assertEquals(1, Fraction.MINUS_ONE.getDenominator());
    }

    @Test
    public void testConstructorIntIntNominal() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test
    public void testConstructorIntIntReduction() {
        Fraction fraction = new Fraction(6, 8);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testConstructorIntIntNegativeDenominator() {
        Fraction fraction = new Fraction(3, -5);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test
    public void testConstructorIntIntBothNegative() {
        Fraction fraction = new Fraction(-3, -5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test
    public void testConstructorIntIntZeroNumerator() {
        Fraction fraction = new Fraction(0, 5);
        assertEquals(0, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testConstructorIntIntZeroDenominatorThrowsArithmeticException() {
        new Fraction(1, 0);
    }

    @Test(expected = ArithmeticException.class)
    public void testConstructorIntIntMinNumeratorNegativeDenThrowsArithmeticException() {
        new Fraction(Integer.MIN_VALUE, -1);
    }

    @Test(expected = ArithmeticException.class)
    public void testConstructorIntIntMinDenominatorNegativeDenThrowsArithmeticException() {
        new Fraction(1, Integer.MIN_VALUE);
    }

    @Test
    public void testConstructorDoubleNominal() throws FractionConversionException {
        Fraction fraction = new Fraction(0.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testConstructorDoubleInteger() throws FractionConversionException {
        Fraction fraction = new Fraction(4.0);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructorDoubleNegative() throws FractionConversionException {
        Fraction fraction = new Fraction(-0.75);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleOverflowThrowsConversionException() throws FractionConversionException {
        new Fraction(1.0e20);
    }

    @Test
    public void testConstructorDoubleEpsilonIterationsNominal() throws FractionConversionException {
        Fraction fraction = new Fraction(0.333333, 0.001, 10);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testConstructorDoubleEpsilonIterationsExactWithinEpsilon() throws FractionConversionException {
        Fraction fraction = new Fraction(2.00001, 0.001, 10);
        assertEquals(2, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleEpsilonIterationsMaxIterationsExceeded() throws FractionConversionException {
        new Fraction(0.3333333333, 1.0e-15, 1);
    }

    @Test
    public void testConstructorDoubleMaxDenominatorNominal() throws FractionConversionException {
        Fraction fraction = new Fraction(0.666666, 10);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 1.0e-15);
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5f, fraction.floatValue(), 1.0e-7f);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(5, 2);
        assertEquals(2, fraction.intValue());
    }

    @Test
    public void testIntValueNegative() {
        Fraction fraction = new Fraction(-5, 2);
        assertEquals(-2, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(5, 2);
        assertEquals(2L, fraction.longValue());
    }

    @Test
    public void testAbsPositive() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testAbsNegative() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction result = fraction.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testAbsZero() {
        Fraction result = Fraction.ZERO.abs();
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testAbsMinNumeratorThrowsArithmeticException() {
        new Fraction(Integer.MIN_VALUE, 1).abs();
    }

    @Test
    public void testNegatePositive() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.negate();
        assertEquals(-3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testNegateNegative() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction result = fraction.negate();
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testNegateZero() {
        Fraction result = Fraction.ZERO.negate();
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testNegateMinNumeratorThrowsArithmeticException() {
        new Fraction(Integer.MIN_VALUE, 1).negate();
    }

    @Test
    public void testReciprocalPositive() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.reciprocal();
        assertEquals(4, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testReciprocalNegative() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction result = fraction.reciprocal();
        assertEquals(-4, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testReciprocalZeroThrowsArithmeticException() {
        Fraction.ZERO.reciprocal();
    }

    @Test
    public void testCompareToSmaller() {
        Fraction smaller = new Fraction(1, 3);
        Fraction larger = new Fraction(1, 2);
        assertEquals(-1, smaller.compareTo(larger));
    }

    @Test
    public void testCompareToGreater() {
        Fraction smaller = new Fraction(1, 3);
        Fraction larger = new Fraction(1, 2);
        assertEquals(1, larger.compareTo(smaller));
    }

    @Test
    public void testCompareToEqual() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 4);
        assertEquals(0, fraction1.compareTo(fraction2));
    }

    @Test
    public void testCompareToNegativeAndPositive() {
        Fraction negative = new Fraction(-1, 2);
        Fraction positive = new Fraction(1, 2);
        assertEquals(-1, negative.compareTo(positive));
    }

    @Test
    public void testEqualsSameInstance() {
        Fraction fraction = new Fraction(1, 2);
        assertTrue(fraction.equals(fraction));
    }

    @Test
    public void testEqualsNull() {
        Fraction fraction = new Fraction(1, 2);
        assertFalse(fraction.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Fraction fraction = new Fraction(1, 2);
        assertFalse(fraction.equals("1/2"));
    }

    @Test
    public void testEqualsEquivalentFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 4);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testEqualsDifferentFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        assertFalse(fraction1.equals(fraction2));
    }

    @Test
    public void testHashCodeConsistency() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 4);
        assertEquals(fraction1.hashCode(), fraction2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullThrowsIllegalArgumentException() {
        new Fraction(1, 2).add(null);
    }

    @Test
    public void testAddZeroToFraction() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(Fraction.ZERO);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAddFractionToZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = Fraction.ZERO.add(fraction);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAddRelativelyPrimeDenominators() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAddCommonDenominator() {
        Fraction fraction1 = new Fraction(1, 6);
        Fraction fraction2 = new Fraction(1, 6);
        Fraction result = fraction1.add(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testAddOppositesYieldsZero() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(-2, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testAddOverflowThrowsArithmeticException() {
        new Fraction(Integer.MAX_VALUE - 1, 1).add(new Fraction(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractNullThrowsIllegalArgumentException() {
        new Fraction(1, 2).subtract(null);
    }

    @Test
    public void testSubtractZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.subtract(Fraction.ZERO);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtractFromZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = Fraction.ZERO.subtract(fraction);
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtractRelativelyPrimeDenominators() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testSubtractCommonDenominator() {
        Fraction fraction1 = new Fraction(5, 6);
        Fraction fraction2 = new Fraction(1, 6);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testSubtractSelfYieldsZero() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.subtract(fraction);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testSubtractOverflowThrowsArithmeticException() {
        new Fraction(Integer.MIN_VALUE + 1, 1).subtract(new Fraction(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyNullThrowsIllegalArgumentException() {
        new Fraction(1, 2).multiply(null);
    }

    @Test
    public void testMultiplyByZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(Fraction.ZERO);
        assertEquals(Fraction.ZERO, result);
    }

    @Test
    public void testMultiplyZeroByFraction() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = Fraction.ZERO.multiply(fraction);
        assertEquals(Fraction.ZERO, result);
    }

    @Test
    public void testMultiplyNominal() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testMultiplyNegative() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(-3, 4);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testMultiplyOverflowThrowsArithmeticException() {
        new Fraction(Integer.MAX_VALUE, 1).multiply(new Fraction(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideNullThrowsIllegalArgumentException() {
        new Fraction(1, 2).divide(null);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZeroThrowsArithmeticException() {
        new Fraction(1, 2).divide(Fraction.ZERO);
    }

    @Test
    public void testDivideZeroByFraction() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = Fraction.ZERO.divide(fraction);
        assertEquals(Fraction.ZERO, result);
    }

    @Test
    public void testDivideNominal() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testDivideNegative() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(-1, 4);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(-2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideOverflowThrowsArithmeticException() {
        new Fraction(Integer.MAX_VALUE, 1).divide(new Fraction(1, 2));
    }

    @Test
    public void testGetReducedFractionNominal() {
        Fraction fraction = Fraction.getReducedFraction(2, 4);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetReducedFractionZeroNumerator() {
        Fraction fraction = Fraction.getReducedFraction(0, 5);
        assertEquals(Fraction.ZERO, fraction);
    }

    @Test(expected = ArithmeticException.class)
    public void testGetReducedFractionZeroDenominatorThrowsArithmeticException() {
        Fraction.getReducedFraction(1, 0);
    }

    @Test
    public void testGetReducedFractionNegativeDenominator() {
        Fraction fraction = Fraction.getReducedFraction(1, -2);
        assertEquals(-1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetReducedFractionBothNegative() {
        Fraction fraction = Fraction.getReducedFraction(-1, -2);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetReducedFractionMinValDenominatorEvenNumerator() {
        Fraction fraction = Fraction.getReducedFraction(2, Integer.MIN_VALUE);
        assertEquals(-1, fraction.getNumerator());
        assertEquals(1073741824, fraction.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testGetReducedFractionMinValDenominatorOddNumeratorThrowsArithmeticException() {
        Fraction.getReducedFraction(1, Integer.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void testGetReducedFractionMinValNumeratorNegativeDenThrowsArithmeticException() {
        Fraction.getReducedFraction(Integer.MIN_VALUE, -1);
    }
}