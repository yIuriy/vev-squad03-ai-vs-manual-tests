package org.apache.commons.lang.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class F09Test {

    private Map<String, Object> registry;
    private MockFormatFactory factory;

    @Before
    public void setUp() {
        factory = new MockFormatFactory();
        registry = new HashMap<String, Object>();
        registry.put("upper", factory);
        registry.put("lower", factory);
        registry.put("prefix", factory);
    }

    @Test
    public void testConstructorWithPatternOnlyUsesDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertEquals(Locale.getDefault(), emf.getLocale());
        assertEquals("Hello World", emf.format(new Object[]{"World"}));
    }

    @Test
    public void testConstructorWithPatternAndLocaleSetsLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Pattern {0}", Locale.GERMAN);
        assertEquals(Locale.GERMAN, emf.getLocale());
    }

    @Test
    public void testConstructorWithPatternAndRegistryInitializesRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Value: {0,upper}", registry);
        assertEquals("Value: ABC", emf.format(new Object[]{"abc"}));
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistryInitializesAll() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Value: {0,upper}", Locale.FRANCE, registry);
        assertEquals(Locale.FRANCE, emf.getLocale());
        assertEquals(Locale.FRANCE, factory.lastSeenLocale);
        assertEquals("Value: ABC", emf.format(new Object[]{"abc"}));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullPatternThrowsNullPointerException() {
        new ExtendedMessageFormat(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.setFormat(0, new UpperCaseFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.setFormatByArgumentIndex(0, new UpperCaseFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.setFormats(new Format[]{new UpperCaseFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.setFormatsByArgumentIndex(new Format[]{new UpperCaseFormat()});
    }

    @Test
    public void testFormatWithoutRegistryUsesStandardMessageFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, score is {1}");
        String result = emf.format(new Object[]{"John", 100});
        assertEquals("Hello John, score is 100", result);
    }

    @Test
    public void testToPatternWithoutRegistryReturnsSuperToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Pattern {0}");
        assertEquals("Pattern {0}", emf.toPattern());
    }

    @Test
    public void testApplyPatternWithoutRegistryUpdatesPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Initial {0}");
        emf.applyPattern("Updated {0} {1}");
        assertEquals("Updated {0} {1}", emf.toPattern());
        assertEquals("Updated A B", emf.format(new Object[]{"A", "B"}));
    }

    @Test
    public void testPatternWithQuotesWithoutRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("'{'0'}' is {0}");
        assertEquals("{0} is test", emf.format(new Object[]{"test"}));
    }

    @Test
    public void testEmptyPatternWithoutRegistryProducesEmptyOutput() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
        assertEquals("", emf.format(new Object[]{}));
    }

    @Test
    public void testCustomFormatAppliedCorrectly() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Greeting: {0,upper}", registry);
        assertEquals("Greeting: HELLO", emf.format(new Object[]{"hello"}));
    }

    @Test
    public void testCustomFormatWithArgumentsParsedAndPassedToFactory() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Result: {0,prefix,TAG: }", registry);
        assertEquals("Result: TAG: message", emf.format(new Object[]{"message"}));
        assertEquals("prefix", factory.lastSeenName);
        assertEquals("TAG: ", factory.lastSeenArgs);
    }

    @Test
    public void testCustomFormatWithLocalePassedToFactory() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper}", Locale.ITALY, registry);
        emf.format(new Object[]{"test"});
        assertEquals(Locale.ITALY, factory.lastSeenLocale);
    }

    @Test
    public void testMultipleDifferentCustomFormatsInSamePattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} and {1,lower}", registry);
        assertEquals("FIRST and second", emf.format(new Object[]{"first", "SECOND"}));
    }

    @Test
    public void testMixedCustomAndBuiltInFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} owes {1,number,integer} dollars", Locale.US, registry);
        assertEquals("BOB owes 50 dollars", emf.format(new Object[]{"Bob", 50}));
    }

    @Test
    public void testReusedArgumentIndexWithDifferentCustomFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} is also {0,lower}", registry);
        assertEquals("MIXED is also mixed", emf.format(new Object[]{"Mixed"}));
    }

    @Test
    public void testToPatternReconstructsCustomFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Name: {0,upper}", registry);
        assertEquals("Name: {0,upper}", emf.toPattern());
    }

    @Test
    public void testToPatternReconstructsCustomFormatWithArguments() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Key: {0,prefix,ID_}", registry);
        assertEquals("Key: {0,prefix,ID_}", emf.toPattern());
    }

    @Test
    public void testToPatternWithNoCustomFormatsWhenRegistryPresent() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Simple {0}", registry);
        assertEquals("Simple {0}", emf.toPattern());
    }

    @Test
    public void testFormatElementWithWhitespaceAroundArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Value: {   0   ,upper}", registry);
        assertEquals("Value: HELLO", emf.format(new Object[]{"hello"}));
    }

    @Test
    public void testFormatElementWithWhitespaceAroundFormatNameAndStyle() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Value: {0,   prefix   ,   PRE-   }", registry);
        assertEquals("prefix", factory.lastSeenName);
        assertEquals("PRE-", factory.lastSeenArgs);
        assertEquals("Value: PRE-data", emf.format(new Object[]{"data"}));
    }

    @Test
    public void testMultiDigitArgumentIndexParsedCorrectly() {
        Object[] args = new Object[12];
        args[11] = "target";
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Item: {11,upper}", registry);
        assertEquals("Item: TARGET", emf.format(args));
    }

    @Test
    public void testNestedBracesInFormatDescriptionParsedCorrectly() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Nested: {0,prefix,{nested}}", registry);
        assertEquals("{nested}", factory.lastSeenArgs);
        assertEquals("Nested: {nested}item", emf.format(new Object[]{"item"}));
        assertEquals("Nested: {0,prefix,{nested}}", emf.toPattern());
    }

    @Test
    public void testEmptyPatternWithRegistryProducesEmptyOutput() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", registry);
        assertEquals("", emf.toPattern());
        assertEquals("", emf.format(new Object[]{}));
    }

    @Test
    public void testPatternWithoutFormatElementsWithRegistryProducesLiteralText() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Plain text only", registry);
        assertEquals("Plain text only", emf.toPattern());
        assertEquals("Plain text only", emf.format(new Object[]{}));
    }

    @Test
    public void testUnterminatedFormatElementThrowsException() {
        try {
            new ExtendedMessageFormat("Unclosed {0", registry);
            fail("Expected IllegalArgumentException for unterminated format element");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unterminated format element"));
        }
    }

    @Test
    public void testUnterminatedFormatDescriptionThrowsException() {
        try {
            new ExtendedMessageFormat("Unclosed description {0,upper", registry);
            fail("Expected IllegalArgumentException for unterminated format description");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unterminated format element"));
        }
    }

    @Test
    public void testInvalidFormatArgumentIndexNonDigitThrowsException() {
        try {
            new ExtendedMessageFormat("Invalid index {abc}", registry);
            fail("Expected IllegalArgumentException for non-digit argument index");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid format argument index"));
        }
    }

    @Test
    public void testNegativeArgumentIndexThrowsException() {
        try {
            new ExtendedMessageFormat("Negative index {-1}", registry);
            fail("Expected IllegalArgumentException for negative argument index");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid format argument index"));
        }
    }

    @Test
    public void testEmptyArgumentIndexThrowsException() {
        try {
            new ExtendedMessageFormat("Empty index {}", registry);
            fail("Expected IllegalArgumentException for empty argument index");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid format argument index"));
        }
    }

    @Test
    public void testWhitespaceSeparatedTokensInArgumentIndexThrowsException() {
        try {
            new ExtendedMessageFormat("Invalid index {0 1}", registry);
            fail("Expected IllegalArgumentException for whitespace-separated index tokens");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid format argument index"));
        }
    }

    @Test
    public void testApplyPatternCanBeCalledMultipleTimesToChangePattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("First: {0,upper}", registry);
        assertEquals("First: FOO", emf.format(new Object[]{"foo"}));

        emf.applyPattern("Second: {0,lower}");
        assertEquals("Second: {0,lower}", emf.toPattern());
        assertEquals("Second: foo", emf.format(new Object[]{"FOO"}));
    }

    @Test
    public void testRegistryFactoryReturnsNullFallsBackToStandard() {
        Map<String, Object> customRegistry = Collections.singletonMap("number", (Object) new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return null;
            }
        });
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,integer}", Locale.US, customRegistry);
        assertEquals("100", emf.format(new Object[]{100}));
    }

    @Test
    public void testRegistryMissingFormatNameFallsBackToStandard() {
        Map<String, Object> customRegistry = Collections.singletonMap("custom", (Object) factory);
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,integer}", Locale.US, customRegistry);
        assertEquals("100", emf.format(new Object[]{100}));
        assertEquals("{0,number,integer}", emf.toPattern());
    }

    @Test
    public void testEmptyRegistryProducesStandardFormatting() {
        Map<String, Object> emptyRegistry = new HashMap<String, Object>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,integer}", Locale.US, emptyRegistry);
        assertEquals("200", emf.format(new Object[]{200}));
        assertEquals("{0,number,integer}", emf.toPattern());
    }

    private static class MockFormatFactory implements FormatFactory {
        private String lastSeenName;
        private String lastSeenArgs;
        private Locale lastSeenLocale;

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            this.lastSeenName = name;
            this.lastSeenArgs = arguments;
            this.lastSeenLocale = locale;
            if ("upper".equals(name)) {
                return new UpperCaseFormat();
            } else if ("lower".equals(name)) {
                return new LowerCaseFormat();
            } else if ("prefix".equals(name)) {
                return new PrefixFormat(arguments);
            }
            return null;
        }
    }

    private static class UpperCaseFormat extends Format {
        private static final long serialVersionUID = 1L;

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            if (obj == null) {
                return toAppendTo;
            }
            return toAppendTo.append(obj.toString().toUpperCase(Locale.ROOT));
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    private static class LowerCaseFormat extends Format {
        private static final long serialVersionUID = 1L;

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            if (obj == null) {
                return toAppendTo;
            }
            return toAppendTo.append(obj.toString().toLowerCase(Locale.ROOT));
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    private static class PrefixFormat extends Format {
        private static final long serialVersionUID = 1L;
        private final String prefix;

        public PrefixFormat(String prefix) {
            this.prefix = prefix == null ? "" : prefix;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(prefix).append(obj == null ? "" : obj.toString());
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }
}