package org.apache.commons.lang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit test suite for {@link BooleanUtils}.
 */
public class F07Test {

    @Test
    public void testConstructor() {
        BooleanUtils utils = new BooleanUtils();
        assertNotNull(utils);
    }

    // -----------------------------------------------------------------------
    // negate(Boolean)
    // -----------------------------------------------------------------------

    @Test
    public void testNegateWithNullInputReturnsNull() {
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testNegateWithTrueInputReturnsFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
    }

    @Test
    public void testNegateWithFalseInputReturnsTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
    }

    // -----------------------------------------------------------------------
    // isTrue / isNotTrue / isFalse / isNotFalse
    // -----------------------------------------------------------------------

    @Test
    public void testIsTrueWithNullReturnsFalse() {
        assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsTrueWithTrueReturnsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
    }

    @Test
    public void testIsTrueWithFalseReturnsFalse() {
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrueWithNullReturnsTrue() {
        assertTrue(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsNotTrueWithTrueReturnsFalse() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
    }

    @Test
    public void testIsNotTrueWithFalseReturnsTrue() {
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalseWithNullReturnsFalse() {
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsFalseWithTrueReturnsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
    }

    @Test
    public void testIsFalseWithFalseReturnsTrue() {
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
    }

    @Test
    public void testIsNotFalseWithNullReturnsTrue() {
        assertTrue(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testIsNotFalseWithTrueReturnsTrue() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
    }

    @Test
    public void testIsNotFalseWithFalseReturnsFalse() {
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    // -----------------------------------------------------------------------
    // toBooleanObject(boolean) / toBoolean(Boolean) / toBooleanDefaultIfNull
    // -----------------------------------------------------------------------

    @Test
    public void testToBooleanObjectFromPrimitiveTrueReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(true));
    }

    @Test
    public void testToBooleanObjectFromPrimitiveFalseReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(false));
    }

    @Test
    public void testToBooleanFromBooleanTrueReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
    }

    @Test
    public void testToBooleanFromBooleanFalseReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
    }

    @Test
    public void testToBooleanFromNullBooleanReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean((Boolean) null));
    }

    @Test
    public void testToBooleanDefaultIfNullWithNullInputReturnsDefaultTrue() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanDefaultIfNullWithNullInputReturnsDefaultFalse() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanDefaultIfNullWithTrueInputIgnoresDefault() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
    }

    @Test
    public void testToBooleanDefaultIfNullWithFalseInputIgnoresDefault() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
    }

    // -----------------------------------------------------------------------
    // int / Integer conversions
    // -----------------------------------------------------------------------

    @Test
    public void testToBooleanFromPrimitiveIntZeroReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanFromPrimitiveIntPositiveReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(1));
    }

    @Test
    public void testToBooleanFromPrimitiveIntNegativeReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectFromPrimitiveIntZeroReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
    }

    @Test
    public void testToBooleanObjectFromPrimitiveIntPositiveReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
    }

    @Test
    public void testToBooleanObjectFromPrimitiveIntNegativeReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(-1));
    }

    @Test
    public void testToBooleanObjectFromNullIntegerReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject((Integer) null));
    }

    @Test
    public void testToBooleanObjectFromIntegerZeroReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
    }

    @Test
    public void testToBooleanObjectFromIntegerPositiveReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
    }

    @Test
    public void testToBooleanObjectFromIntegerNegativeReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(-1)));
    }

    @Test
    public void testToBooleanMatchingPrimitiveIntTrueValueReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
    }

    @Test
    public void testToBooleanMatchingPrimitiveIntFalseValueReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanPrimitiveIntNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBoolean(2, 1, 0);
    }

    @Test
    public void testToBooleanIntegerMatchingTrueValueReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
    }

    @Test
    public void testToBooleanIntegerMatchingFalseValueReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
    }

    @Test
    public void testToBooleanNullIntegerMatchingNullTrueValueReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(null, null, Integer.valueOf(0)));
    }

    @Test
    public void testToBooleanNullIntegerMatchingNullFalseValueReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean(null, Integer.valueOf(1), null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanNullIntegerNoNullValuesThrowsIllegalArgumentException() {
        BooleanUtils.toBoolean(null, Integer.valueOf(1), Integer.valueOf(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanIntegerNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0));
    }

    @Test
    public void testToBooleanObjectMatchingPrimitiveIntTrueValueReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 2, 3));
    }

    @Test
    public void testToBooleanObjectMatchingPrimitiveIntFalseValueReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(2, 1, 2, 3));
    }

    @Test
    public void testToBooleanObjectMatchingPrimitiveIntNullValueReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject(3, 1, 2, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectPrimitiveIntNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBooleanObject(4, 1, 2, 3);
    }

    @Test
    public void testToBooleanObjectIntegerMatchingTrueValueReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)));
    }

    @Test
    public void testToBooleanObjectIntegerMatchingFalseValueReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)));
    }

    @Test
    public void testToBooleanObjectIntegerMatchingNullValueReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)));
    }

    @Test
    public void testToBooleanObjectNullIntegerMatchingNullTrueValueReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(null, null, Integer.valueOf(2), Integer.valueOf(3)));
    }

    @Test
    public void testToBooleanObjectNullIntegerMatchingNullFalseValueReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(null, Integer.valueOf(1), null, Integer.valueOf(3)));
    }

    @Test
    public void testToBooleanObjectNullIntegerMatchingNullValueReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(2), null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectNullIntegerNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectIntegerNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBooleanObject(Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    }

    // -----------------------------------------------------------------------
    // toInteger conversions
    // -----------------------------------------------------------------------

    @Test
    public void testToIntegerFromPrimitiveTrueReturnsOne() {
        assertEquals(1, BooleanUtils.toInteger(true));
    }

    @Test
    public void testToIntegerFromPrimitiveFalseReturnsZero() {
        assertEquals(0, BooleanUtils.toInteger(false));
    }

    @Test
    public void testToIntegerObjectFromPrimitiveTrueReturnsIntegerOne() {
        assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(true));
    }

    @Test
    public void testToIntegerObjectFromPrimitiveFalseReturnsIntegerZero() {
        assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(false));
    }

    @Test
    public void testToIntegerObjectFromBooleanTrueReturnsIntegerOne() {
        assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
    }

    @Test
    public void testToIntegerObjectFromBooleanFalseReturnsIntegerZero() {
        assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectFromNullBooleanReturnsNull() {
        assertNull(BooleanUtils.toIntegerObject((Boolean) null));
    }

    @Test
    public void testToIntegerPrimitiveBooleanWithCustomValuesReturnsTrueValue() {
        assertEquals(10, BooleanUtils.toInteger(true, 10, 20));
    }

    @Test
    public void testToIntegerPrimitiveBooleanWithCustomValuesReturnsFalseValue() {
        assertEquals(20, BooleanUtils.toInteger(false, 10, 20));
    }

    @Test
    public void testToIntegerBooleanWithCustomValuesReturnsTrueValue() {
        assertEquals(10, BooleanUtils.toInteger(Boolean.TRUE, 10, 20, 30));
    }

    @Test
    public void testToIntegerBooleanWithCustomValuesReturnsFalseValue() {
        assertEquals(20, BooleanUtils.toInteger(Boolean.FALSE, 10, 20, 30));
    }

    @Test
    public void testToIntegerBooleanWithCustomValuesReturnsNullValue() {
        assertEquals(30, BooleanUtils.toInteger(null, 10, 20, 30));
    }

    @Test
    public void testToIntegerObjectPrimitiveBooleanWithCustomObjectsReturnsTrueValue() {
        Integer trueVal = Integer.valueOf(10);
        Integer falseVal = Integer.valueOf(20);
        assertSame(trueVal, BooleanUtils.toIntegerObject(true, trueVal, falseVal));
    }

    @Test
    public void testToIntegerObjectPrimitiveBooleanWithCustomObjectsReturnsFalseValue() {
        Integer trueVal = Integer.valueOf(10);
        Integer falseVal = Integer.valueOf(20);
        assertSame(falseVal, BooleanUtils.toIntegerObject(false, trueVal, falseVal));
    }

    @Test
    public void testToIntegerObjectBooleanWithCustomObjectsReturnsTrueValue() {
        Integer trueVal = Integer.valueOf(10);
        Integer falseVal = Integer.valueOf(20);
        Integer nullVal = Integer.valueOf(30);
        assertSame(trueVal, BooleanUtils.toIntegerObject(Boolean.TRUE, trueVal, falseVal, nullVal));
    }

    @Test
    public void testToIntegerObjectBooleanWithCustomObjectsReturnsFalseValue() {
        Integer trueVal = Integer.valueOf(10);
        Integer falseVal = Integer.valueOf(20);
        Integer nullVal = Integer.valueOf(30);
        assertSame(falseVal, BooleanUtils.toIntegerObject(Boolean.FALSE, trueVal, falseVal, nullVal));
    }

    @Test
    public void testToIntegerObjectBooleanWithCustomObjectsReturnsNullValue() {
        Integer trueVal = Integer.valueOf(10);
        Integer falseVal = Integer.valueOf(20);
        Integer nullVal = Integer.valueOf(30);
        assertSame(nullVal, BooleanUtils.toIntegerObject(null, trueVal, falseVal, nullVal));
    }

    // -----------------------------------------------------------------------
    // toBooleanObject(String)
    // -----------------------------------------------------------------------

    @Test
    public void testToBooleanObjectStringWithNullReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject((String) null));
    }

    @Test
    public void testToBooleanObjectStringWithTrueCaseInsensitiveReturnsTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject("TRUE"));
    }

    @Test
    public void testToBooleanObjectStringWithFalseCaseInsensitiveReturnsFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject("FALSE"));
    }

    @Test
    public void testToBooleanObjectStringWithOnCaseInsensitiveReturnsTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject("on"));
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject("ON"));
    }

    @Test
    public void testToBooleanObjectStringWithOffCaseInsensitiveReturnsFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject("off"));
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject("OFF"));
    }

    @Test
    public void testToBooleanObjectStringWithYesCaseInsensitiveReturnsTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject("yes"));
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject("YES"));
    }

    @Test
    public void testToBooleanObjectStringWithNoCaseInsensitiveReturnsFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject("no"));
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject("NO"));
    }

    @Test
    public void testToBooleanObjectStringWithArbitraryStringReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject("blue"));
    }

    @Test
    public void testToBooleanObjectStringWithEmptyStringReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject(""));
    }

    // -----------------------------------------------------------------------
    // toBooleanObject(String, String, String, String)
    // -----------------------------------------------------------------------

    @Test
    public void testToBooleanObjectFourStringsMatchingTrueStringReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject("T", "T", "F", "N"));
    }

    @Test
    public void testToBooleanObjectFourStringsMatchingFalseStringReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject("F", "T", "F", "N"));
    }

    @Test
    public void testToBooleanObjectFourStringsMatchingNullStringReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject("N", "T", "F", "N"));
    }

    @Test
    public void testToBooleanObjectFourStringsNullStringMatchingNullTrueReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(null, null, "F", "N"));
    }

    @Test
    public void testToBooleanObjectFourStringsNullStringMatchingNullFalseReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(null, "T", null, "N"));
    }

    @Test
    public void testToBooleanObjectFourStringsNullStringMatchingNullNullReturnsNull() {
        assertNull(BooleanUtils.toBooleanObject(null, "T", "F", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectFourStringsNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBooleanObject("X", "T", "F", "N");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectFourStringsNullInputNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBooleanObject(null, "T", "F", "N");
    }

    // -----------------------------------------------------------------------
    // toBoolean(String)
    // -----------------------------------------------------------------------

    @Test
    public void testToBooleanStringWithInternedTrueReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean("true"));
    }

    @Test
    public void testToBooleanStringWithConstructedTrueReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(new String(new char[]{'t', 'r', 'u', 'e'})));
    }

    @Test
    public void testToBooleanStringWithNullReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean((String) null));
    }

    @Test
    public void testToBooleanStringWithCaseVariationsOfTrueReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean("TRUE"));
        assertTrue(BooleanUtils.toBoolean("True"));
        assertTrue(BooleanUtils.toBoolean("tRUe"));
    }

    @Test
    public void testToBooleanStringWithCaseVariationsOfOnReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean("on"));
        assertTrue(BooleanUtils.toBoolean("ON"));
        assertTrue(BooleanUtils.toBoolean("oN"));
        assertTrue(BooleanUtils.toBoolean("On"));
    }

    @Test
    public void testToBooleanStringWithCaseVariationsOfYesReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean("yes"));
        assertTrue(BooleanUtils.toBoolean("YES"));
        assertTrue(BooleanUtils.toBoolean("Yes"));
        assertTrue(BooleanUtils.toBoolean("yEs"));
    }

    @Test
    public void testToBooleanStringWithTwoCharNonMatchingReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean("no"));
        assertFalse(BooleanUtils.toBoolean("ox"));
    }

    @Test
    public void testToBooleanStringWithThreeCharNonMatchingReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean("foo"));
        assertFalse(BooleanUtils.toBoolean("yep"));
    }

    @Test
    public void testToBooleanStringWithFourCharNonMatchingReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean("blue"));
        assertFalse(BooleanUtils.toBoolean("tree"));
    }

    @Test
    public void testToBooleanStringWithOtherLengthsReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean(""));
        assertFalse(BooleanUtils.toBoolean("a"));
        assertFalse(BooleanUtils.toBoolean("false"));
    }

    // -----------------------------------------------------------------------
    // toBoolean(String, String, String)
    // -----------------------------------------------------------------------

    @Test
    public void testToBooleanThreeStringsMatchingTrueStringReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean("T", "T", "F"));
    }

    @Test
    public void testToBooleanThreeStringsMatchingFalseStringReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean("F", "T", "F"));
    }

    @Test
    public void testToBooleanThreeStringsNullMatchingNullTrueStringReturnsTrue() {
        assertTrue(BooleanUtils.toBoolean(null, null, "F"));
    }

    @Test
    public void testToBooleanThreeStringsNullMatchingNullFalseStringReturnsFalse() {
        assertFalse(BooleanUtils.toBoolean(null, "T", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanThreeStringsNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBoolean("X", "T", "F");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanThreeStringsNullInputNoMatchThrowsIllegalArgumentException() {
        BooleanUtils.toBoolean(null, "T", "F");
    }

    // -----------------------------------------------------------------------
    // toString conversions
    // -----------------------------------------------------------------------

    @Test
    public void testToStringTrueFalseBooleanObject() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        assertNull(BooleanUtils.toStringTrueFalse((Boolean) null));
    }

    @Test
    public void testToStringOnOffBooleanObject() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        assertNull(BooleanUtils.toStringOnOff((Boolean) null));
    }

    @Test
    public void testToStringYesNoBooleanObject() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        assertNull(BooleanUtils.toStringYesNo((Boolean) null));
    }

    @Test
    public void testToStringBooleanObjectCustomStrings() {
        assertEquals("Y", BooleanUtils.toString(Boolean.TRUE, "Y", "N", "U"));
        assertEquals("N", BooleanUtils.toString(Boolean.FALSE, "Y", "N", "U"));
        assertEquals("U", BooleanUtils.toString(null, "Y", "N", "U"));
    }

    @Test
    public void testToStringTrueFalsePrimitiveBoolean() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(true));
        assertEquals("false", BooleanUtils.toStringTrueFalse(false));
    }

    @Test
    public void testToStringOnOffPrimitiveBoolean() {
        assertEquals("on", BooleanUtils.toStringOnOff(true));
        assertEquals("off", BooleanUtils.toStringOnOff(false));
    }

    @Test
    public void testToStringYesNoPrimitiveBoolean() {
        assertEquals("yes", BooleanUtils.toStringYesNo(true));
        assertEquals("no", BooleanUtils.toStringYesNo(false));
    }

    @Test
    public void testToStringPrimitiveBooleanCustomStrings() {
        assertEquals("Y", BooleanUtils.toString(true, "Y", "N"));
        assertEquals("N", BooleanUtils.toString(false, "Y", "N"));
    }

    // -----------------------------------------------------------------------
    // xor(boolean[])
    // -----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testXorPrimitiveArrayWithNullInputThrowsIllegalArgumentException() {
        BooleanUtils.xor((boolean[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXorPrimitiveArrayWithEmptyArrayThrowsIllegalArgumentException() {
        BooleanUtils.xor(new boolean[0]);
    }

    @Test
    public void testXorPrimitiveArrayWithSingleTrueReturnsTrue() {
        assertTrue(BooleanUtils.xor(new boolean[]{true}));
    }

    @Test
    public void testXorPrimitiveArrayWithSingleFalseReturnsFalse() {
        assertFalse(BooleanUtils.xor(new boolean[]{false}));
    }

    @Test
    public void testXorPrimitiveArrayWithExactlyOneTrueReturnsTrue() {
        assertTrue(BooleanUtils.xor(new boolean[]{false, true, false}));
    }

    @Test
    public void testXorPrimitiveArrayWithMultipleTrueReturnsFalse() {
        assertFalse(BooleanUtils.xor(new boolean[]{true, true}));
        assertFalse(BooleanUtils.xor(new boolean[]{true, false, true}));
    }

    @Test
    public void testXorPrimitiveArrayWithAllFalseReturnsFalse() {
        assertFalse(BooleanUtils.xor(new boolean[]{false, false, false}));
    }

    // -----------------------------------------------------------------------
    // xor(Boolean[])
    // -----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testXorObjectArrayWithNullInputThrowsIllegalArgumentException() {
        BooleanUtils.xor((Boolean[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXorObjectArrayWithEmptyArrayThrowsIllegalArgumentException() {
        BooleanUtils.xor(new Boolean[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXorObjectArrayWithNullElementThrowsIllegalArgumentException() {
        BooleanUtils.xor(new Boolean[]{Boolean.TRUE, null});
    }

    @Test
    public void testXorObjectArrayWithSingleTrueReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.xor(new Boolean[]{Boolean.TRUE}));
    }

    @Test
    public void testXorObjectArrayWithSingleFalseReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.xor(new Boolean[]{Boolean.FALSE}));
    }

    @Test
    public void testXorObjectArrayWithExactlyOneTrueReturnsBooleanTrue() {
        assertSame(Boolean.TRUE, BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
    }

    @Test
    public void testXorObjectArrayWithMultipleTrueReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
    }

    @Test
    public void testXorObjectArrayWithAllFalseReturnsBooleanFalse() {
        assertSame(Boolean.FALSE, BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
    }
}