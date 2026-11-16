package org.apache.commons.math.stat;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class F12Test {

    private static final double TOLERANCE = 1e-9;
    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testEmptyFrequencyGetSumFreqReturnsZero() {
        assertEquals(0L, frequency.getSumFreq());
    }

    @Test
    public void testEmptyFrequencyGetCountIntReturnsZero() {
        assertEquals(0L, frequency.getCount(1));
    }

    @Test
    public void testEmptyFrequencyGetCountLongReturnsZero() {
        assertEquals(0L, frequency.getCount(1L));
    }

    @Test
    public void testEmptyFrequencyGetCountCharReturnsZero() {
        assertEquals(0L, frequency.getCount('a'));
    }

    @Test
    public void testEmptyFrequencyGetCountObjectReturnsZero() {
        assertEquals(0L, frequency.getCount("test"));
    }

    @Test
    public void testEmptyFrequencyGetPctIntReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getPct(1)));
    }

    @Test
    public void testEmptyFrequencyGetPctLongReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getPct(1L)));
    }

    @Test
    public void testEmptyFrequencyGetPctCharReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getPct('a')));
    }

    @Test
    public void testEmptyFrequencyGetPctObjectReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getPct("test")));
    }

    @Test
    public void testEmptyFrequencyGetCumFreqIntReturnsZero() {
        assertEquals(0L, frequency.getCumFreq(1));
    }

    @Test
    public void testEmptyFrequencyGetCumFreqLongReturnsZero() {
        assertEquals(0L, frequency.getCumFreq(1L));
    }

    @Test
    public void testEmptyFrequencyGetCumFreqCharReturnsZero() {
        assertEquals(0L, frequency.getCumFreq('a'));
    }

    @Test
    public void testEmptyFrequencyGetCumFreqObjectReturnsZero() {
        assertEquals(0L, frequency.getCumFreq("test"));
    }

    @Test
    public void testEmptyFrequencyGetCumPctIntReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getCumPct(1)));
    }

    @Test
    public void testEmptyFrequencyGetCumPctLongReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getCumPct(1L)));
    }

    @Test
    public void testEmptyFrequencyGetCumPctCharReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getCumPct('a')));
    }

    @Test
    public void testEmptyFrequencyGetCumPctObjectReturnsNaN() {
        assertTrue(Double.isNaN(frequency.getCumPct("test")));
    }

    @Test
    public void testEmptyFrequencyValuesIteratorIsEmpty() {
        assertFalse(frequency.valuesIterator().hasNext());
    }

    @Test
    public void testEmptyFrequencyToStringOutputsHeaderOnly() {
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n", frequency.toString());
    }

    @Test
    public void testAddValueIntIncrementsCount() {
        frequency.addValue(10);
        assertEquals(1L, frequency.getCount(10));
    }

    @Test
    public void testAddValueIntegerIncrementsCount() {
        frequency.addValue(Integer.valueOf(15));
        assertEquals(1L, frequency.getCount(15));
    }

    @Test
    public void testAddValueLongIncrementsCount() {
        frequency.addValue(20L);
        assertEquals(1L, frequency.getCount(20L));
    }

    @Test
    public void testAddValueCharIncrementsCount() {
        frequency.addValue('z');
        assertEquals(1L, frequency.getCount('z'));
    }

    @Test
    public void testAddValueObjectIncrementsCount() {
        frequency.addValue("alpha");
        assertEquals(1L, frequency.getCount("alpha"));
    }

    @Test
    public void testAddValueMultipleTimesIncrementsCount() {
        frequency.addValue(42);
        frequency.addValue(42);
        assertEquals(2L, frequency.getCount(42));
    }

    @Test
    public void testIntegralTypesAreEquivalentlyCounted() {
        frequency.addValue(5);
        frequency.addValue(Integer.valueOf(5));
        frequency.addValue(5L);
        frequency.addValue((Object) Long.valueOf(5L));

        assertEquals(4L, frequency.getCount(5));
        assertEquals(4L, frequency.getCount(5L));
        assertEquals(4L, frequency.getCount(Integer.valueOf(5)));
        assertEquals(4L, frequency.getCount((Object) Long.valueOf(5L)));
        assertEquals(4L, frequency.getSumFreq());
    }

    @Test
    public void testAddNegativeIntValue() {
        frequency.addValue(-7);
        assertEquals(1L, frequency.getCount(-7));
    }

    @Test
    public void testAddZeroIntValue() {
        frequency.addValue(0);
        assertEquals(1L, frequency.getCount(0));
    }

    @Test
    public void testAddLongMinValue() {
        frequency.addValue(Long.MIN_VALUE);
        assertEquals(1L, frequency.getCount(Long.MIN_VALUE));
    }

    @Test
    public void testAddLongMaxValue() {
        frequency.addValue(Long.MAX_VALUE);
        assertEquals(1L, frequency.getCount(Long.MAX_VALUE));
    }

    @Test
    public void testAddCharBoundaryMinAndMax() {
        frequency.addValue(Character.MIN_VALUE);
        frequency.addValue(Character.MAX_VALUE);
        assertEquals(1L, frequency.getCount(Character.MIN_VALUE));
        assertEquals(1L, frequency.getCount(Character.MAX_VALUE));
    }

    @Test
    public void testGetSumFreqWithMultipleDistinctValues() {
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(3L, frequency.getSumFreq());
    }

    @Test
    public void testGetPctForExistingValue() {
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.25, frequency.getPct(1), TOLERANCE);
        assertEquals(0.75, frequency.getPct(2), TOLERANCE);
    }

    @Test
    public void testGetPctForNonExistingValueReturnsZero() {
        frequency.addValue(10);
        assertEquals(0.0, frequency.getPct(99), TOLERANCE);
    }

    @Test
    public void testGetPctInt() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(0.5, frequency.getPct(10), TOLERANCE);
    }

    @Test
    public void testGetPctLong() {
        frequency.addValue(100L);
        frequency.addValue(200L);
        assertEquals(0.5, frequency.getPct(100L), TOLERANCE);
    }

    @Test
    public void testGetPctChar() {
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getPct('a'), TOLERANCE);
    }

    @Test
    public void testGetCumFreqValueLessThanMinimumReturnsZero() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(0L, frequency.getCumFreq(5));
    }

    @Test
    public void testGetCumFreqValueEqualToMinimumReturnsItsCount() {
        frequency.addValue(10);
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(2L, frequency.getCumFreq(10));
    }

    @Test
    public void testGetCumFreqValueBetweenEntriesReturnsAccumulatedCount() {
        frequency.addValue(10);
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(2L, frequency.getCumFreq(15));
    }

    @Test
    public void testGetCumFreqValueEqualToIntermediateEntry() {
        frequency.addValue(10);
        frequency.addValue(20);
        frequency.addValue(20);
        frequency.addValue(30);
        assertEquals(3L, frequency.getCumFreq(20));
    }

    @Test
    public void testGetCumFreqValueEqualToMaximumReturnsSumFreq() {
        frequency.addValue(10);
        frequency.addValue(20);
        frequency.addValue(30);
        assertEquals(3L, frequency.getCumFreq(30));
    }

    @Test
    public void testGetCumFreqValueGreaterThanMaximumReturnsSumFreq() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(2L, frequency.getCumFreq(50));
    }

    @Test
    public void testGetCumFreqInt() {
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1L, frequency.getCumFreq(1));
    }

    @Test
    public void testGetCumFreqLong() {
        frequency.addValue(10L);
        frequency.addValue(20L);
        assertEquals(2L, frequency.getCumFreq(20L));
    }

    @Test
    public void testGetCumFreqChar() {
        frequency.addValue('b');
        frequency.addValue('d');
        assertEquals(1L, frequency.getCumFreq('c'));
    }

    @Test
    public void testGetCumFreqWithIntegerObject() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(1L, frequency.getCumFreq((Object) Integer.valueOf(10)));
        assertEquals(2L, frequency.getCumFreq((Object) Integer.valueOf(25)));
    }

    @Test
    public void testGetCumFreqIncompatibleTypeReturnsZero() {
        frequency.addValue(10);
        assertEquals(0L, frequency.getCumFreq("incompatible"));
    }

    @Test
    public void testGetCumPctValueLessThanMinimumReturnsZero() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(0.0, frequency.getCumPct(5), TOLERANCE);
    }

    @Test
    public void testGetCumPctValueEqualToMinimum() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(0.5, frequency.getCumPct(10), TOLERANCE);
    }

    @Test
    public void testGetCumPctValueBetweenEntries() {
        frequency.addValue(10);
        frequency.addValue(20);
        frequency.addValue(20);
        frequency.addValue(20);
        assertEquals(0.25, frequency.getCumPct(15), TOLERANCE);
    }

    @Test
    public void testGetCumPctValueEqualToMaximumReturnsOne() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(1.0, frequency.getCumPct(20), TOLERANCE);
    }

    @Test
    public void testGetCumPctValueGreaterThanMaximumReturnsOne() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(1.0, frequency.getCumPct(50), TOLERANCE);
    }

    @Test
    public void testGetCumPctInt() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(0.5, frequency.getCumPct(10), TOLERANCE);
    }

    @Test
    public void testGetCumPctLong() {
        frequency.addValue(100L);
        frequency.addValue(200L);
        assertEquals(1.0, frequency.getCumPct(200L), TOLERANCE);
    }

    @Test
    public void testGetCumPctChar() {
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getCumPct('a'), TOLERANCE);
    }

    @Test
    public void testGetCumPctIncompatibleTypeReturnsZero() {
        frequency.addValue(10);
        assertEquals(0.0, frequency.getCumPct("incompatible"), TOLERANCE);
    }

    @Test
    public void testValuesIteratorReturnsOrderedValues() {
        frequency.addValue(30);
        frequency.addValue(10);
        frequency.addValue(20);

        Iterator<?> iterator = frequency.valuesIterator();
        assertEquals(10L, iterator.next());
        assertEquals(20L, iterator.next());
        assertEquals(30L, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValuesIteratorContainsUniqueKeys() {
        frequency.addValue(10);
        frequency.addValue(10);

        Iterator<?> iterator = frequency.valuesIterator();
        assertEquals(10L, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClearResetsFrequencyTable() {
        frequency.addValue(10);
        frequency.addValue(20);
        frequency.clear();

        assertEquals(0L, frequency.getSumFreq());
        assertEquals(0L, frequency.getCount(10));
        assertFalse(frequency.valuesIterator().hasNext());
    }

    @Test
    public void testCustomComparatorOrdersValuesInReverse() {
        Frequency revFreq = new Frequency(Collections.reverseOrder());
        revFreq.addValue(10);
        revFreq.addValue(20);

        Iterator<?> iterator = revFreq.valuesIterator();
        assertEquals(20L, iterator.next());
        assertEquals(10L, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCustomComparatorAffectsCumFreq() {
        Frequency revFreq = new Frequency(Collections.reverseOrder());
        revFreq.addValue(10);
        revFreq.addValue(20);
        revFreq.addValue(30);

        assertEquals(1L, revFreq.getCumFreq(30));
        assertEquals(2L, revFreq.getCumFreq(20));
        assertEquals(3L, revFreq.getCumFreq(10));
    }

    @Test
    public void testCustomComparatorCaseInsensitiveString() {
        Frequency caseInsensitiveFreq = new Frequency(String.CASE_INSENSITIVE_ORDER);
        caseInsensitiveFreq.addValue("abc");
        caseInsensitiveFreq.addValue("ABC");

        assertEquals(2L, caseInsensitiveFreq.getCount("abc"));
        assertEquals(2L, caseInsensitiveFreq.getCount("ABC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIncompatibleTypeThrowsIllegalArgumentException() {
        frequency.addValue(10);
        frequency.addValue("incompatible");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCharAfterIntegerThrowsIllegalArgumentException() {
        frequency.addValue(1);
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableObjectThrowsIllegalArgumentException() {
        frequency.addValue(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullObjectThrowsNullPointerException() {
        frequency.addValue((Object) null);
    }

    @Test
    public void testGetCountIncompatibleTypeReturnsZero() {
        frequency.addValue(10);
        assertEquals(0L, frequency.getCount("incompatible"));
    }

    @Test
    public void testToStringWithPopulatedValues() {
        frequency.addValue("A");
        NumberFormat nf = NumberFormat.getPercentInstance();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n"
                + "A\t1\t" + nf.format(1.0) + "\t" + nf.format(1.0) + "\n";
        assertEquals(expected, frequency.toString());
    }

    @Test
    public void testSerializationRoundTrip() throws Exception {
        frequency.addValue(10);
        frequency.addValue(20);
        frequency.addValue(20);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(frequency);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Frequency deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserialized = (Frequency) ois.readObject();
        }

        assertEquals(frequency.getSumFreq(), deserialized.getSumFreq());
        assertEquals(frequency.getCount(10), deserialized.getCount(10));
        assertEquals(frequency.getCount(20), deserialized.getCount(20));
    }
}