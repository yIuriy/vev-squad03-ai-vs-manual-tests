package org.apache.commons.lang;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class F10Test {

    @BeforeClass
    public static void setUpClass() {
        LocaleUtils.availableLocaleSet();
    }

    // -----------------------------------------------------------------------
    // Constructor tests
    // -----------------------------------------------------------------------

    @Test
    public void testConstructorNotNull() {
        LocaleUtils utils = new LocaleUtils();
        assertNotNull(utils);
    }

    // -----------------------------------------------------------------------
    // toLocale tests
    // -----------------------------------------------------------------------

    @Test
    public void testToLocaleWithNullReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleValidLanguageOnly() {
        Locale locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocaleValidLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocaleValidLanguageCountryAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_US_win");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("win", locale.getVariant());
    }

    @Test
    public void testToLocaleValidLanguageCountryAndLongVariant() {
        Locale locale = LocaleUtils.toLocale("en_US_POSIX_variant");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("POSIX_variant", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionForEmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionForLengthOne() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionForLengthThree() {
        LocaleUtils.toLocale("eng");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionForLengthFour() {
        LocaleUtils.toLocale("en_U");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionForLengthSix() {
        LocaleUtils.toLocale("en_US_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenFirstCharOfLanguageIsNotLowercase() {
        LocaleUtils.toLocale("En");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenSecondCharOfLanguageIsNotLowercase() {
        LocaleUtils.toLocale("eN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenLanguageContainsNumericChar() {
        LocaleUtils.toLocale("1n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenCountrySeparatorIsNotUnderscore() {
        LocaleUtils.toLocale("en-US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenFirstCharOfCountryIsNotUppercase() {
        LocaleUtils.toLocale("en_uS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenSecondCharOfCountryIsNotUppercase() {
        LocaleUtils.toLocale("en_Us");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenCountryContainsNumericChar() {
        LocaleUtils.toLocale("en_U1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsExceptionWhenVariantSeparatorIsNotUnderscore() {
        LocaleUtils.toLocale("en_US#win");
    }

    // -----------------------------------------------------------------------
    // localeLookupList tests
    // -----------------------------------------------------------------------

    @Test
    public void testLocaleLookupListWithSingleParamLanguageOnly() {
        Locale locale = new Locale("en");
        List list = LocaleUtils.localeLookupList(locale);
        assertEquals(1, list.size());
        assertEquals(locale, list.get(0));
    }

    @Test
    public void testLocaleLookupListWithSingleParamLanguageAndCountry() {
        Locale locale = new Locale("en", "US");
        List list = LocaleUtils.localeLookupList(locale);
        assertEquals(2, list.size());
        assertEquals(new Locale("en", "US"), list.get(0));
        assertEquals(new Locale("en", ""), list.get(1));
    }

    @Test
    public void testLocaleLookupListWithSingleParamLanguageCountryAndVariant() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr", ""), list.get(2));
    }

    @Test
    public void testLocaleLookupListWithNullLocaleReturnsEmptyList() {
        List list = LocaleUtils.localeLookupList(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testLocaleLookupListWithVariantAndEmptyCountry() {
        Locale locale = new Locale("fr", "", "xxx");
        List list = LocaleUtils.localeLookupList(locale);
        assertEquals(2, list.size());
        assertEquals(new Locale("fr", "", "xxx"), list.get(0));
        assertEquals(new Locale("fr", ""), list.get(1));
    }

    @Test
    public void testLocaleLookupListAppendsDefaultLocaleWhenNotPresent() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr", ""), list.get(2));
        assertEquals(new Locale("en"), list.get(3));
    }

    @Test
    public void testLocaleLookupListDoesNotDuplicateDefaultLocaleWhenAlreadyPresent() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("fr");
        List list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(2, list.size());
        assertEquals(new Locale("fr", "CA"), list.get(0));
        assertEquals(new Locale("fr"), list.get(1));
    }

    @Test
    public void testLocaleLookupListWithNullLocaleAndNonNullDefaultReturnsEmptyList() {
        List list = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocaleLookupListReturnsUnmodifiableList() {
        List list = LocaleUtils.localeLookupList(Locale.ENGLISH);
        list.add(Locale.FRENCH);
    }

    // -----------------------------------------------------------------------
    // availableLocaleList and availableLocaleSet tests
    // -----------------------------------------------------------------------

    @Test
    public void testAvailableLocaleListContainsInstalledLocales() {
        List list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        List expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected.size(), list.size());
        assertEquals(expected, list);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableLocaleListIsUnmodifiable() {
        List list = LocaleUtils.availableLocaleList();
        list.add(Locale.ENGLISH);
    }

    @Test
    public void testAvailableLocaleSetContainsAllAvailableLocales() {
        Set set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        Set expected = new HashSet(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected.size(), set.size());
        assertEquals(expected, set);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableLocaleSetIsUnmodifiable() {
        Set set = LocaleUtils.availableLocaleSet();
        set.add(Locale.ENGLISH);
    }

    // -----------------------------------------------------------------------
    // isAvailableLocale tests
    // -----------------------------------------------------------------------

    @Test
    public void testIsAvailableLocaleReturnsTrueForExistingLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocaleReturnsFalseForNonExistentLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zzz", "ZZ")));
    }

    // -----------------------------------------------------------------------
    // languagesByCountry tests
    // -----------------------------------------------------------------------

    @Test
    public void testLanguagesByCountryReturnsEmptyListForNull() {
        List list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testLanguagesByCountryReturnsMatchingLocalesForKnownCountry() {
        List list = LocaleUtils.languagesByCountry("US");
        assertNotNull(list);
        assertFalse(list.isEmpty());
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Locale locale = (Locale) it.next();
            assertEquals("US", locale.getCountry());
            assertEquals("", locale.getVariant());
        }
    }

    @Test
    public void testLanguagesByCountryReturnsEmptyListForUnknownCountry() {
        List list = LocaleUtils.languagesByCountry("ZZ");
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLanguagesByCountryReturnsUnmodifiableList() {
        List list = LocaleUtils.languagesByCountry("US");
        list.add(Locale.US);
    }

    @Test
    public void testLanguagesByCountryCachesResult() {
        List firstCall = LocaleUtils.languagesByCountry("US");
        List secondCall = LocaleUtils.languagesByCountry("US");
        assertSame(firstCall, secondCall);
    }

    // -----------------------------------------------------------------------
    // countriesByLanguage tests
    // -----------------------------------------------------------------------

    @Test
    public void testCountriesByLanguageReturnsEmptyListForNull() {
        List list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCountriesByLanguageReturnsMatchingLocalesForKnownLanguage() {
        List list = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list);
        assertFalse(list.isEmpty());
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Locale locale = (Locale) it.next();
            assertEquals("en", locale.getLanguage());
            assertFalse(locale.getCountry().isEmpty());
            assertEquals("", locale.getVariant());
        }
    }

    @Test
    public void testCountriesByLanguageReturnsEmptyListForUnknownLanguage() {
        List list = LocaleUtils.countriesByLanguage("zz");
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCountriesByLanguageReturnsUnmodifiableList() {
        List list = LocaleUtils.countriesByLanguage("en");
        list.add(Locale.US);
    }

    @Test
    public void testCountriesByLanguageCachesResult() {
        List firstCall = LocaleUtils.countriesByLanguage("en");
        List secondCall = LocaleUtils.countriesByLanguage("en");
        assertSame(firstCall, secondCall);
    }
}