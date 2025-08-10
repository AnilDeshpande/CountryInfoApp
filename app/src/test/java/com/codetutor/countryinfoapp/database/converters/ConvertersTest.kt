package com.codetutor.countryinfoapp.database.converters

import com.codetutor.countryinfoapp.TestDataFactory
import com.codetutor.countryinfoapp.data.*
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Comprehensive tests for Room TypeConverters
 * Tests round-trip (obj → json → obj) and null handling for each converter
 */
class ConvertersTest {
    
    private lateinit var converters: Converters
    private val json = Json { ignoreUnknownKeys = true }
    
    @Before
    fun setUp() {
        converters = Converters()
    }
    
    // CapitalInfo Converter Tests
    @Test
    fun test_capitalInfoConverter_roundTrip() {
        val original = TestDataFactory.createCapitalInfo(latlng = listOf(52.5200, 13.4050))
        
        val jsonString = converters.fromCapitalInfoToJson(original)
        val converted = converters.fromJsonToCapitalInfo(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_capitalInfoConverter_null() {
        assertNull(converters.fromCapitalInfoToJson(null))
        assertNull(converters.fromJsonToCapitalInfo(null))
    }
    
    // Car Converter Tests
    @Test
    fun test_carConverter_roundTrip() {
        val original = TestDataFactory.createCar(side = "left")
        
        val jsonString = converters.fromCarToJson(original)
        val converted = converters.fromJsonToCar(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_carConverter_null() {
        assertNull(converters.fromCarToJson(null))
        assertNull(converters.fromJsonToCar(null))
    }
    
    // Currency Converter Tests
    @Test
    fun test_currencyConverter_roundTrip() {
        val original = mapOf(
            "USD" to TestDataFactory.createCurrency("US Dollar", "$"),
            "EUR" to TestDataFactory.createCurrency("Euro", "€")
        )
        
        val jsonString = converters.fromCurrencyMapToJson(original)
        val converted = converters.fromJsonToCurrencyMap(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_currencyConverter_null() {
        assertNull(converters.fromCurrencyMapToJson(null))
        assertNull(converters.fromJsonToCurrencyMap(null))
    }
    
    @Test
    fun test_currencyConverter_emptyMap() {
        val original = emptyMap<String, Currency>()
        
        val jsonString = converters.fromCurrencyMapToJson(original)
        val converted = converters.fromJsonToCurrencyMap(jsonString)
        
        assertEquals(original, converted)
    }
    
    // Flags Converter Tests
    @Test
    fun test_flagsConverter_roundTrip() {
        val original = TestDataFactory.createFlags(
            png = "https://flagcdn.com/w320/de.png",
            svg = "https://flagcdn.com/de.svg"
        )
        
        val jsonString = converters.fromFlagsToJson(original)
        val converted = converters.fromJsonToFlags(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_flagsConverter_null() {
        assertNull(converters.fromFlagsToJson(null))
        assertNull(converters.fromJsonToFlags(null))
    }
    
    // Idd Converter Tests
    @Test
    fun test_iddConverter_roundTrip() {
        val original = TestDataFactory.createIdd(
            root = "+49",
            suffixes = listOf("30", "40")
        )
        
        val jsonString = converters.fromIddToJson(original)
        val converted = converters.fromJsonToIdd(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_iddConverter_null() {
        assertNull(converters.fromIddToJson(null))
        assertNull(converters.fromJsonToIdd(null))
    }
    
    // Languages Converter Tests
    @Test
    fun test_languagesConverter_roundTrip() {
        val original = TestDataFactory.createLanguages(
            mapOf(
                "de" to "German",
                "en" to "English"
            )
        )
        
        val jsonString = converters.fromLanguagesToJson(original)
        val converted = converters.fromJsonToLanguages(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_languagesConverter_null() {
        assertNull(converters.fromLanguagesToJson(null))
        assertNull(converters.fromJsonToLanguages(null))
    }
    
    // Maps Converter Tests
    @Test
    fun test_mapsConverter_roundTrip() {
        val original = TestDataFactory.createMaps(
            googleMaps = "https://goo.gl/maps/TK9XV2SSqwYDcRr6A",
            openStreetMaps = "https://www.openstreetmap.org/relation/51477"
        )
        
        val jsonString = converters.fromMapsToJson(original)
        val converted = converters.fromJsonToMaps(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_mapsConverter_null() {
        assertNull(converters.fromMapsToJson(null))
        assertNull(converters.fromJsonToMaps(null))
    }
    
    // Name Converter Tests
    @Test
    fun test_nameConverter_roundTrip() {
        val original = TestDataFactory.createName(
            common = "Germany",
            official = "Federal Republic of Germany"
        )
        
        val jsonString = converters.fromNameToJson(original)
        val converted = converters.fromJsonToName(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_nameConverter_null() {
        assertNull(converters.fromNameToJson(null))
        assertNull(converters.fromJsonToName(null))
    }
    
    // NameTranslation Map Converter Tests
    @Test
    fun test_nameTranslationMapConverter_roundTrip() {
        val original = mapOf(
            "deu" to NameTranslation(
                official = "Bundesrepublik Deutschland",
                common = "Deutschland"
            ),
            "fra" to NameTranslation(
                official = "République fédérale d'Allemagne",
                common = "Allemagne"
            )
        )
        
        val jsonString = converters.fromNameTranslationMapToJson(original)
        val converted = converters.fromJsonToNameTranslationMap(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_nameTranslationMapConverter_null() {
        assertNull(converters.fromNameTranslationMapToJson(null))
        assertNull(converters.fromJsonToNameTranslationMap(null))
    }
    
    @Test
    fun test_nameTranslationMapConverter_emptyMap() {
        val original = emptyMap<String, NameTranslation>()
        
        val jsonString = converters.fromNameTranslationMapToJson(original)
        val converted = converters.fromJsonToNameTranslationMap(jsonString)
        
        assertEquals(original, converted)
    }
    
    // Additional converter tests for completeness
    
    // String List Converter Tests (already covered in existing tests, but adding for completeness)
    @Test
    fun test_stringListConverter_roundTrip() {
        val original = listOf("Berlin", "Munich", "Hamburg")
        
        val jsonString = converters.fromStringListToJson(original)
        val converted = converters.fromJsonToStringList(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_stringListConverter_emptyList() {
        val original = emptyList<String>()
        
        val jsonString = converters.fromStringListToJson(original)
        val converted = converters.fromJsonToStringList(jsonString)
        
        assertEquals(original, converted)
    }
    
    // Double List Converter Tests
    @Test
    fun test_doubleListConverter_roundTrip() {
        val original = listOf(52.5200, 13.4050)
        
        val jsonString = converters.fromDoubleListToJson(original)
        val converted = converters.fromJsonToDoubleList(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_doubleListConverter_null() {
        assertNull(converters.fromDoubleListToJson(null))
        assertNull(converters.fromJsonToDoubleList(null))
    }
    
    @Test
    fun test_doubleListConverter_emptyList() {
        val original = emptyList<Double>()
        
        val jsonString = converters.fromDoubleListToJson(original)
        val converted = converters.fromJsonToDoubleList(jsonString)
        
        assertEquals(original, converted)
    }
    
    // Language Map Converter Tests
    @Test
    fun test_languageMapConverter_roundTrip() {
        val original = mapOf(
            "de" to "German",
            "en" to "English",
            "fr" to "French"
        )
        
        val jsonString = converters.fromLanguageMapToJson(original)
        val converted = converters.fromJsonToLanguageMap(jsonString)
        
        assertEquals(original, converted)
    }
    
    @Test
    fun test_languageMapConverter_null() {
        assertNull(converters.fromLanguageMapToJson(null))
        assertNull(converters.fromJsonToLanguageMap(null))
    }
    
    @Test
    fun test_languageMapConverter_emptyMap() {
        val original = emptyMap<String, String>()
        
        val jsonString = converters.fromLanguageMapToJson(original)
        val converted = converters.fromJsonToLanguageMap(jsonString)
        
        assertEquals(original, converted)
    }
    
    // Test edge cases with malformed JSON (should throw exceptions as expected)
    @Test(expected = Exception::class)
    fun test_malformedJson_throwsException_capitalInfo() {
        converters.fromJsonToCapitalInfo("invalid json")
    }
    
    @Test(expected = Exception::class)
    fun test_malformedJson_throwsException_car() {
        converters.fromJsonToCar("invalid json")
    }
    
    @Test(expected = Exception::class)
    fun test_malformedJson_throwsException_flags() {
        converters.fromJsonToFlags("invalid json")
    }
    
    @Test(expected = Exception::class)
    fun test_emptyJsonString_throwsException_capitalInfo() {
        converters.fromJsonToCapitalInfo("")
    }
    
    @Test(expected = Exception::class)
    fun test_emptyJsonString_throwsException_car() {
        converters.fromJsonToCar("")
    }
}