/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for {@link ExtendedMessageFormat}.
 */
public class F08Test {

    private static class UpperCaseFormat extends Format {
        private static final long serialVersionUID = 1L;

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(String.valueOf(obj).toUpperCase(Locale.ENGLISH));
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            pos.setIndex(source.length());
            return source;
        }
    }

    private static class LowerCaseFormat extends Format {
        private static final long serialVersionUID = 1L;

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(String.valueOf(obj).toLowerCase(Locale.ENGLISH));
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            pos.setIndex(source.length());
            return source;
        }
    }

    private static class PrefixFormat extends Format {
        private static final long serialVersionUID = 1L;
        private final String prefix;

        PrefixFormat(String prefix) {
            this.prefix = prefix == null ? "" : prefix;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(prefix).append(obj);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            pos.setIndex(source.length());
            return source;
        }
    }

    private static class TestFormatFactory implements FormatFactory {
        private Locale capturedLocale;

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            this.capturedLocale = locale;
            if ("upper".equals(name)) {
                return new UpperCaseFormat();
            }
            if ("lower".equals(name)) {
                return new LowerCaseFormat();
            }
            if ("prefix".equals(name)) {
                return new PrefixFormat(arguments);
            }
            return null;
        }

        Locale getCapturedLocale() {
            return capturedLocale;
        }
    }

    private Map<String, FormatFactory> createRegistry() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        TestFormatFactory factory = new TestFormatFactory();
        registry.put("upper", factory);
        registry.put("lower", factory);
        registry.put("prefix", factory);
        return registry;
    }

    @Test
    public void testConstructorPatternOnlyInitializesWithDefaultLocaleAndNullRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0} test");
        assertEquals(Locale.getDefault(), emf.getLocale());
        assertEquals("{0} test", emf.toPattern());
        assertEquals("foo test", emf.format(new Object[]{"foo"}));
    }

    @Test
    public void testConstructorPatternAndLocaleInitializesProperly() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0} test", Locale.GERMANY);
        assertEquals(Locale.GERMANY, emf.getLocale());
        assertEquals("{0} test", emf.toPattern());
    }

    @Test
    public void testConstructorPatternAndRegistryInitializesWithDefaultLocale() {
        Map<String, FormatFactory> registry = createRegistry();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper}", registry);
        assertEquals(Locale.getDefault(), emf.getLocale());
        assertEquals("FOO", emf.format(new Object[]{"foo"}));
    }

    @Test
    public void testConstructorPatternLocaleAndRegistryPropagatesLocaleToFactory() {
        TestFormatFactory factory = new TestFormatFactory();
        Map<String, FormatFactory> registry = Collections.singletonMap("upper", (FormatFactory) factory);
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper}", Locale.FRANCE, registry);

        assertEquals(Locale.FRANCE, emf.getLocale());
        assertEquals("PARIS", emf.format(new Object[]{"paris"}));
        assertEquals(Locale.FRANCE, factory.getCapturedLocale());
    }

    @Test
    public void testFormatSingleCustomElementWithoutArguments() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper}", createRegistry());
        assertEquals("HELLO", emf.format(new Object[]{"hello"}));
    }

    @Test
    public void testFormatSingleCustomElementWithArguments() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,prefix,PREFIX: }", createRegistry());
        assertEquals("PREFIX: message", emf.format(new Object[]{"message"}));
    }

    @Test
    public void testFormatMultipleCustomElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} and {1,lower}", createRegistry());
        assertEquals("CAT and dog", emf.format(new Object[]{"cat", "DOG"}));
    }

    @Test
    public void testFormatReusingSameArgumentIndexWithDifferentCustomFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} / {0,lower}", createRegistry());
        assertEquals("JAVA / java", emf.format(new Object[]{"JaVa"}));
    }

    @Test
    public void testFormatMixingCustomAndBuiltInFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} = {1,number,000}", createRegistry());
        assertEquals("COUNT = 007", emf.format(new Object[]{"count", 7}));
    }

    @Test
    public void testFormatWithSurroundingWhitespaceInFormatElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{  0  ,  upper  }", createRegistry());
        assertEquals("SPACES", emf.format(new Object[]{"spaces"}));
    }

    @Test
    public void testFormatWithSurroundingWhitespaceInFormatArguments() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{ 0 , prefix , >> }", createRegistry());
        assertEquals(">>item", emf.format(new Object[]{"item"}));
    }

    @Test
    public void testFormatUnregisteredFormatTypeDelegatesToStandardMessageFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,integer}", createRegistry());
        assertEquals("123", emf.format(new Object[]{123}));
    }

    @Test
    public void testFormatWithEmptyRegistryDelegatesToStandardMessageFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.0}", new HashMap<String, FormatFactory>());
        assertEquals("5.5", emf.format(new Object[]{5.5}));
    }

    @Test
    public void testFormatWhenFactoryReturnsNullDelegatesToStandardMessageFormat() {
        Map<String, FormatFactory> registry = Collections.singletonMap("number", (FormatFactory) new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return null;
            }
        });
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,currency}", Locale.US, registry);
        assertNotNull(emf.format(new Object[]{10}));
    }

    @Test
    public void testFormatWithoutRegistryHandlesEscapedSingleQuote() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("It''s {0}");
        assertEquals("It's fine", emf.format(new Object[]{"fine"}));
    }

    @Test
    public void testFormatWithoutRegistryHandlesQuotedFormatElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("'{0}' {0}");
        assertEquals("{0} value", emf.format(new Object[]{"value"}));
    }

    @Test
    public void testFormatLargeArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{10,upper}", createRegistry());
        Object[] args = new Object[11];
        args[10] = "eleventh";
        assertEquals("ELEVENTH", emf.format(args));
    }

    @Test
    public void testFormatNestedChoiceSubformats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,choice,0#zero|1#{1}}", createRegistry());
        assertEquals("zero", emf.format(new Object[]{0, "unused"}));
        assertEquals("one", emf.format(new Object[]{1, "one"}));
    }

    @Test
    public void testToPatternWithoutRegistryMatchesSourcePattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Message {0}");
        assertEquals("Message {0}", emf.toPattern());
    }

    @Test
    public void testToPatternWithSingleCustomFormatReconstructsPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper}", createRegistry());
        assertEquals("{0,upper}", emf.toPattern());
    }

    @Test
    public void testToPatternWithCustomFormatAndArgumentsReconstructsPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,prefix,PREFIX: }", createRegistry());
        assertEquals("{0,prefix,PREFIX: }", emf.toPattern());
    }

    @Test
    public void testToPatternWithMultipleCustomFormatsReconstructsPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} - {1,lower}", createRegistry());
        assertEquals("{0,upper} - {1,lower}", emf.toPattern());
    }

    @Test
    public void testToPatternWithMixedCustomAndBuiltInFormatsReconstructsPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper} {1,number,000}", createRegistry());
        assertEquals("{0,upper} {1,number,000}", emf.toPattern());
    }

    @Test
    public void testToPatternWithEmptyPatternAndRegistryReturnsEmptyString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", createRegistry());
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testToPatternWithRegistryButNoCustomFormatsUsed() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0} and {1}", createRegistry());
        assertEquals("{0} and {1}", emf.toPattern());
    }

    @Test
    public void testApplyPatternUpdatesPatternAndCustomFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper}", createRegistry());
        assertEquals("ABC", emf.format(new Object[]{"abc"}));

        emf.applyPattern("{0,lower}");
        assertEquals("{0,lower}", emf.toPattern());
        assertEquals("abc", emf.format(new Object[]{"ABC"}));
    }

    @Test
    public void testApplyPatternWithoutRegistryUpdatesPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.applyPattern("{0} -> {1}");
        assertEquals("{0} -> {1}", emf.toPattern());
        assertEquals("A -> B", emf.format(new Object[]{"A", "B"}));
    }

    @Test
    public void testApplyPatternWithPlainTextOnly() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,upper}", createRegistry());
        emf.applyPattern("Plain text without formats");
        assertEquals("Plain text without formats", emf.toPattern());
        assertEquals("Plain text without formats", emf.format(new Object[]{}));
    }

    @Test
    public void testEmptyPatternWithoutRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
        assertEquals("", emf.format(new Object[]{}));
    }

    @Test
    public void testEmptyPatternWithRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", createRegistry());
        assertEquals("", emf.toPattern());
        assertEquals("", emf.format(new Object[]{}));
    }

    @Test
    public void testPlainTextOnlyPatternWithRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Plain text only", createRegistry());
        assertEquals("Plain text only", emf.toPattern());
        assertEquals("Plain text only", emf.format(new Object[]{}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.setFormat(0, new UpperCaseFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.setFormatByArgumentIndex(0, new UpperCaseFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.setFormats(new Format[]{new UpperCaseFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexThrowsUnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.setFormatsByArgumentIndex(new Format[]{new UpperCaseFormat()});
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNullPointerExceptionOnNullPattern() {
        new ExtendedMessageFormat(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenArgumentIndexContainsNonDigits() {
        new ExtendedMessageFormat("{0a,upper}", createRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenArgumentIndexIsNegative() {
        new ExtendedMessageFormat("{-1,upper}", createRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenArgumentIndexIsEmpty() {
        new ExtendedMessageFormat("{}", createRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenArgumentIndexContainsInvalidWhitespaceFollowedByCharacters() {
        new ExtendedMessageFormat("{0 foo}", createRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenFormatElementUnterminatedAfterOpeningBrace() {
        new ExtendedMessageFormat("{", createRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenFormatElementUnterminatedAfterArgumentIndex() {
        new ExtendedMessageFormat("{0", createRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenFormatElementUnterminatedAfterComma() {
        new ExtendedMessageFormat("{0,", createRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenFormatElementUnterminatedInsideFormatDescription() {
        new ExtendedMessageFormat("{0,upper", createRegistry());
    }
}