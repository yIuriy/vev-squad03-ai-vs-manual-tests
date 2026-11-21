package org.apache.commons.lang3.text.translate;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class F11Test {

    private NumericEntityUnescaper unescaper;
    private StringWriter out;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
        out = new StringWriter();
    }

    @Test
    public void translate_whenCharAtNotAmpersand_shouldReturnZeroAndNotWrite() throws IOException {
        int consumed = unescaper.translate("abc", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void translate_whenAmpersandNotFollowedByHash_shouldReturnZeroAndNotWrite() throws IOException {
        int consumed = unescaper.translate("&abc;", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void translate_whenValidDecimalEntity_shouldWriteCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("&#65;", 0, out);

        assertEquals(5, consumed);
        assertEquals("A", out.toString());
    }

    @Test
    public void translate_whenValidHexEntityWithLowercaseX_shouldWriteCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("&#x41;", 0, out);

        assertEquals(6, consumed);
        assertEquals("A", out.toString());
    }

    @Test
    public void translate_whenValidHexEntityWithUppercaseX_shouldWriteCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("&#X41;", 0, out);

        assertEquals(6, consumed);
        assertEquals("A", out.toString());
    }

    @Test
    public void translate_whenValidEntityAtNonZeroIndex_shouldWriteCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("prefix&#65;suffix", 6, out);

        assertEquals(5, consumed);
        assertEquals("A", out.toString());
    }

    @Test
    public void translate_whenDecimalEntityWithLeadingZeros_shouldWriteCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("&#0065;", 0, out);

        assertEquals(7, consumed);
        assertEquals("A", out.toString());
    }

    @Test
    public void translate_whenHexEntityWithMixedCaseDigits_shouldWriteCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("&#x002B;", 0, out);

        assertEquals(8, consumed);
        assertEquals("+", out.toString());
    }

    @Test
    public void translate_whenEntityValueIsZero_shouldWriteNullCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("&#0;", 0, out);

        assertEquals(4, consumed);
        assertEquals("\u0000", out.toString());
    }

    @Test
    public void translate_whenEntityValueIsMaxBmpChar_shouldWriteCharacterAndReturnConsumedLength() throws IOException {
        int consumed = unescaper.translate("&#65535;", 0, out);

        assertEquals(8, consumed);
        assertEquals("\uFFFF", out.toString());
    }

    @Test
    public void translate_whenDecimalEntityContainsNonDigits_shouldReturnZeroAndNotWrite() throws IOException {
        int consumed = unescaper.translate("&#xyz;", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void translate_whenHexEntityContainsNonHexDigits_shouldReturnZeroAndNotWrite() throws IOException {
        int consumed = unescaper.translate("&#xzzz;", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void translate_whenDecimalEntityIsEmpty_shouldReturnZeroAndNotWrite() throws IOException {
        int consumed = unescaper.translate("&#;", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void translate_whenHexEntityIsEmpty_shouldReturnZeroAndNotWrite() throws IOException {
        int consumed = unescaper.translate("&#x;", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void translate_whenEntityValueOverflowsInteger_shouldReturnZeroAndNotWrite() throws IOException {
        int consumed = unescaper.translate("&#99999999999999999999;", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void translate_whenStringEndsAtAmpersand_shouldThrowIndexOutOfBoundsException() throws IOException {
        unescaper.translate("&", 0, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void translate_whenStringEndsAtHash_shouldThrowIndexOutOfBoundsException() throws IOException {
        unescaper.translate("&#", 0, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void translate_whenEntityLacksClosingSemicolon_shouldThrowIndexOutOfBoundsException() throws IOException {
        unescaper.translate("&#65", 0, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void translate_whenIndexExceedsInputLength_shouldThrowIndexOutOfBoundsException() throws IOException {
        unescaper.translate("test", 10, out);
    }

    @Test(expected = NullPointerException.class)
    public void translate_whenInputIsNull_shouldThrowNullPointerException() throws IOException {
        unescaper.translate(null, 0, out);
    }

    @Test(expected = NullPointerException.class)
    public void translate_whenWriterIsNullAndEntityValid_shouldThrowNullPointerException() throws IOException {
        unescaper.translate("&#65;", 0, null);
    }
}