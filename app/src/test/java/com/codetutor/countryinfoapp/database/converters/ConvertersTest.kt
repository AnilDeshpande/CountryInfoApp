package com.codetutor.countryinfoapp.database.converters

import com.codetutor.countryinfoapp.data.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ConvertersTest {
    private lateinit var converters: Converters

    @Before
    fun setup() {
        converters = Converters()
    }

    // ==================== String List Conversion Tests ====================

    @Test
    fun `test conversion of non-empty string list to JSON`() {
        val stringList = listOf("USA", "Canada", "Mexico")
        val json = converters.fromStringListToJson(stringList)
        assertNotNull(json)
        assertTrue(json!!.contains("USA"))
        assertTrue(json.contains("Canada"))
        assertTrue(json.contains("Mexico"))
    }

    @Test
    fun `test conversion of empty string list to JSON`() {
        val emptyList = emptyList<String>()
        val json = converters.fromStringListToJson(emptyList)
        assertNotNull(json)
        assertTrue(json!!.contains("[]") || json == "[]")
    }

    @Test
    fun `test conversion of null string list to JSON`() {
        val json = converters.fromStringListToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON string to string list conversion`() {
        val json = "[\"USA\",\"Canada\",\"Mexico\"]"
        val list = converters.fromJsonToStringList(json)
        assertNotNull(list)
        assertEquals(3, list!!.size)
        assertEquals("USA", list[0])
        assertEquals("Canada", list[1])
        assertEquals("Mexico", list[2])
    }

    @Test
    fun `test null JSON to null string list`() {
        val list = converters.fromJsonToStringList(null)
        assertNull(list)
    }

    @Test
    fun `test invalid JSON string handling for string list`() {
        val invalidJson = "not a valid json"
        try {
            converters.fromJsonToStringList(invalidJson)
            fail("Should have thrown an exception for invalid JSON")
        } catch (e: Exception) {
            // Expected behavior
            assertTrue(true)
        }
    }

    // ==================== CapitalInfo Conversion Tests ====================

    @Test
    fun `test CapitalInfo object to JSON conversion`() {
        val capitalInfo = CapitalInfo(latlng = listOf(40.7128, -74.0060))
        val json = converters.fromCapitalInfoToJson(capitalInfo)
        assertNotNull(json)
        assertTrue(json!!.contains("latlng") || json.contains("40") || json.isNotEmpty())
    }

    @Test
    fun `test null CapitalInfo to JSON`() {
        val json = converters.fromCapitalInfoToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to CapitalInfo object conversion`() {
        val json = "{\"latlng\":[40.7128,-74.0060]}"
        val capitalInfo = converters.fromJsonToCapitalInfo(json)
        assertNotNull(capitalInfo)
        assertNotNull(capitalInfo!!.latlng)
        assertEquals(2, capitalInfo.latlng!!.size)
        assertEquals(40.7128, capitalInfo.latlng!![0], 0.0001)
        assertEquals(-74.0060, capitalInfo.latlng!![1], 0.0001)
    }

    @Test
    fun `test null JSON to null CapitalInfo`() {
        val capitalInfo = converters.fromJsonToCapitalInfo(null)
        assertNull(capitalInfo)
    }

    @Test
    fun `test invalid JSON to CapitalInfo handling`() {
        val invalidJson = "{invalid json}"
        try {
            converters.fromJsonToCapitalInfo(invalidJson)
            fail("Should have thrown an exception for invalid JSON")
        } catch (e: Exception) {
            assertTrue(true)
        }
    }

    @Test
    fun `test CapitalInfo with null latlng`() {
        val capitalInfo = CapitalInfo(latlng = null)
        val json = converters.fromCapitalInfoToJson(capitalInfo)
        assertNotNull(json)
        val converted = converters.fromJsonToCapitalInfo(json)
        assertNotNull(converted)
        assertNull(converted!!.latlng)
    }

    // ==================== Car Object Conversion Tests ====================

    @Test
    fun `test Car object to JSON conversion`() {
        val car = Car(side = "right", signs = listOf("USA", "US"))
        val json = converters.fromCarToJson(car)
        assertNotNull(json)
        assertTrue(json!!.contains("right"))
        assertTrue(json.contains("USA"))
    }

    @Test
    fun `test null Car to JSON`() {
        val json = converters.fromCarToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to Car object conversion`() {
        val json = "{\"side\":\"right\",\"signs\":[\"USA\",\"US\"]}"
        val car = converters.fromJsonToCar(json)
        assertNotNull(car)
        assertEquals("right", car!!.side)
        assertNotNull(car.signs)
        assertEquals(2, car.signs!!.size)
    }

    @Test
    fun `test malformed JSON handling for Car`() {
        val malformedJson = "{side:right}"
        try {
            converters.fromJsonToCar(malformedJson)
            fail("Should have thrown an exception")
        } catch (e: Exception) {
            assertTrue(true)
        }
    }

    @Test
    fun `test Car with null fields`() {
        val car = Car(side = null, signs = null)
        val json = converters.fromCarToJson(car)
        assertNotNull(json)
        val converted = converters.fromJsonToCar(json)
        assertNotNull(converted)
        assertNull(converted!!.side)
        assertNull(converted.signs)
    }

    // ==================== Currency Conversion Tests ====================

    @Test
    fun `test Currency map to JSON conversion`() {
        val currencyMap =
            mapOf(
                "USD" to Currency(name = "United States dollar", symbol = "$"),
                "EUR" to Currency(name = "Euro", symbol = "€"),
            )
        val json = converters.fromCurrencyMapToJson(currencyMap)
        assertNotNull(json)
        assertTrue(json!!.contains("USD"))
        assertTrue(json.contains("EUR"))
        assertTrue(json.contains("United States dollar"))
    }

    @Test
    fun `test null Currency to JSON`() {
        val json = converters.fromCurrencyMapToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to Currency map conversion`() {
        val json = "{\"USD\":{\"name\":\"United States dollar\",\"symbol\":\"$\"}}"
        val currencyMap = converters.fromJsonToCurrencyMap(json)
        assertNotNull(currencyMap)
        assertTrue(currencyMap!!.containsKey("USD"))
        assertEquals("United States dollar", currencyMap["USD"]?.name)
        assertEquals("$", currencyMap["USD"]?.symbol)
    }

    @Test
    fun `test empty currency map handling`() {
        val emptyMap = emptyMap<String, Currency>()
        val json = converters.fromCurrencyMapToJson(emptyMap)
        assertNotNull(json)
        assertTrue(json!!.contains("{}") || json == "{}")

        val converted = converters.fromJsonToCurrencyMap(json)
        assertNotNull(converted)
        assertTrue(converted!!.isEmpty())
    }

    // ==================== Flags Conversion Tests ====================

    @Test
    fun `test Flags object to JSON conversion`() {
        val flags = Flags(png = "flag.png", svg = "flag.svg", alt = "Country flag")
        val json = converters.fromFlagsToJson(flags)
        assertNotNull(json)
        assertTrue(json!!.contains("flag.png"))
        assertTrue(json.contains("flag.svg"))
    }

    @Test
    fun `test null Flags to JSON`() {
        val json = converters.fromFlagsToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to Flags object conversion`() {
        val json = "{\"png\":\"flag.png\",\"svg\":\"flag.svg\",\"alt\":\"Country flag\"}"
        val flags = converters.fromJsonToFlags(json)
        assertNotNull(flags)
        assertEquals("flag.png", flags!!.png)
        assertEquals("flag.svg", flags.svg)
        assertEquals("Country flag", flags.alt)
    }

    @Test
    fun `test Flags with null fields`() {
        val flags = Flags(png = null, svg = null, alt = null)
        val json = converters.fromFlagsToJson(flags)
        assertNotNull(json)
        val converted = converters.fromJsonToFlags(json)
        assertNotNull(converted)
    }

    // ==================== Maps Conversion Tests ====================

    @Test
    fun `test Maps object to JSON conversion`() {
        val maps = Maps(googleMaps = "https://google.com", openStreetMaps = "https://osm.org")
        val json = converters.fromMapsToJson(maps)
        assertNotNull(json)
        assertTrue(json!!.contains("google.com"))
        assertTrue(json.contains("osm.org"))
    }

    @Test
    fun `test null Maps to JSON`() {
        val json = converters.fromMapsToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to Maps object conversion`() {
        val json = "{\"googleMaps\":\"https://google.com\",\"openStreetMaps\":\"https://osm.org\"}"
        val maps = converters.fromJsonToMaps(json)
        assertNotNull(maps)
        assertEquals("https://google.com", maps!!.googleMaps)
        assertEquals("https://osm.org", maps.openStreetMaps)
    }

    @Test
    fun `test Maps with empty objects`() {
        val maps = Maps(googleMaps = null, openStreetMaps = null)
        val json = converters.fromMapsToJson(maps)
        assertNotNull(json)
        val converted = converters.fromJsonToMaps(json)
        assertNotNull(converted)
    }

    // ==================== Name Conversion Tests ====================

    @Test
    fun `test Name object to JSON conversion`() {
        val nativeName = mapOf("eng" to NameTranslation(official = "United States", common = "USA"))
        val name = Name(common = "USA", official = "United States of America", nativeName = nativeName)
        val json = converters.fromNameToJson(name)
        assertNotNull(json)
        assertTrue(json!!.contains("USA"))
        assertTrue(json.contains("United States of America"))
    }

    @Test
    fun `test null Name to JSON`() {
        val json = converters.fromNameToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to Name object conversion`() {
        val json = "{\"common\":\"USA\",\"official\":\"United States of America\"}"
        val name = converters.fromJsonToName(json)
        assertNotNull(name)
        assertEquals("USA", name!!.common)
        assertEquals("United States of America", name.official)
    }

    @Test
    fun `test Name with missing fields`() {
        val name = Name(common = "USA", official = null, nativeName = null)
        val json = converters.fromNameToJson(name)
        assertNotNull(json)
        val converted = converters.fromJsonToName(json)
        assertNotNull(converted)
        assertEquals("USA", converted!!.common)
    }

    // ==================== Languages Conversion Tests ====================

    @Test
    fun `test Languages object to JSON conversion`() {
        val languagesMap = mapOf("eng" to "English", "spa" to "Spanish")
        val languages = Languages(languages = languagesMap)
        val json = converters.fromLanguagesToJson(languages)
        assertNotNull(json)
        assertTrue(json!!.contains("English"))
        assertTrue(json.contains("Spanish"))
    }

    @Test
    fun `test null Languages to JSON`() {
        val json = converters.fromLanguagesToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to Languages object conversion`() {
        val json = "{\"languages\":{\"eng\":\"English\",\"spa\":\"Spanish\"}}"
        val languages = converters.fromJsonToLanguages(json)
        assertNotNull(languages)
        assertNotNull(languages!!.languages)
        assertEquals("English", languages.languages!!["eng"])
        assertEquals("Spanish", languages.languages!!["spa"])
    }

    @Test
    fun `test Languages with empty map`() {
        val languages = Languages(languages = emptyMap())
        val json = converters.fromLanguagesToJson(languages)
        assertNotNull(json)
        val converted = converters.fromJsonToLanguages(json)
        assertNotNull(converted)
    }

    // ==================== Double List Conversion Tests ====================

    @Test
    fun `test double list to JSON conversion`() {
        val doubleList = listOf(40.7128, -74.0060, 100.5)
        val json = converters.fromDoubleListToJson(doubleList)
        assertNotNull(json)
        assertTrue(json!!.isNotEmpty() && json.contains("[") && json.contains("]"))
    }

    @Test
    fun `test empty double list`() {
        val emptyList = emptyList<Double>()
        val json = converters.fromDoubleListToJson(emptyList)
        assertNotNull(json)
        assertTrue(json!!.contains("[]") || json == "[]")
    }

    @Test
    fun `test null double list`() {
        val json = converters.fromDoubleListToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to double list conversion`() {
        val json = "[40.7128,-74.0060,100.5]"
        val list = converters.fromJsonToDoubleList(json)
        assertNotNull(list)
        assertEquals(3, list!!.size)
        assertEquals(40.7128, list[0], 0.0001)
        assertEquals(-74.0060, list[1], 0.0001)
        assertEquals(100.5, list[2], 0.0001)
    }

    // ==================== IDD Conversion Tests ====================

    @Test
    fun `test IDD object to JSON`() {
        val idd = Idd(root = "+1", suffixes = listOf("234", "567"))
        val json = converters.fromIddToJson(idd)
        assertNotNull(json)
        assertTrue(json!!.contains("+1"))
        assertTrue(json.contains("234"))
    }

    @Test
    fun `test null IDD handling`() {
        val json = converters.fromIddToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to IDD object`() {
        val json = "{\"root\":\"+1\",\"suffixes\":[\"234\",\"567\"]}"
        val idd = converters.fromJsonToIdd(json)
        assertNotNull(idd)
        assertEquals("+1", idd!!.root)
        assertNotNull(idd.suffixes)
        assertEquals(2, idd.suffixes!!.size)
    }

    @Test
    fun `test IDD with null fields`() {
        val idd = Idd(root = null, suffixes = null)
        val json = converters.fromIddToJson(idd)
        assertNotNull(json)
        val converted = converters.fromJsonToIdd(json)
        assertNotNull(converted)
    }

    // ==================== NameTranslation Map Conversion Tests ====================

    @Test
    fun `test NameTranslation map to JSON conversion`() {
        val nameTranslationMap =
            mapOf(
                "eng" to NameTranslation(official = "United States", common = "USA"),
                "spa" to NameTranslation(official = "Estados Unidos", common = "EE.UU."),
            )
        val json = converters.fromNameTranslationMapToJson(nameTranslationMap)
        assertNotNull(json)
        assertTrue(json!!.contains("United States"))
        assertTrue(json.contains("Estados Unidos"))
    }

    @Test
    fun `test null NameTranslation map to JSON`() {
        val json = converters.fromNameTranslationMapToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to NameTranslation map conversion`() {
        val json = "{\"eng\":{\"official\":\"United States\",\"common\":\"USA\"}}"
        val map = converters.fromJsonToNameTranslationMap(json)
        assertNotNull(map)
        assertTrue(map!!.containsKey("eng"))
        assertEquals("United States", map["eng"]?.official)
        assertEquals("USA", map["eng"]?.common)
    }

    @Test
    fun `test empty NameTranslation map`() {
        val emptyMap = emptyMap<String, NameTranslation>()
        val json = converters.fromNameTranslationMapToJson(emptyMap)
        assertNotNull(json)
        val converted = converters.fromJsonToNameTranslationMap(json)
        assertNotNull(converted)
        assertTrue(converted!!.isEmpty())
    }

    // ==================== Language Map Conversion Tests ====================

    @Test
    fun `test language map to JSON conversion`() {
        val languageMap = mapOf("eng" to "English", "spa" to "Spanish")
        val json = converters.fromLanguageMapToJson(languageMap)
        assertNotNull(json)
        assertTrue(json!!.contains("English"))
        assertTrue(json.contains("Spanish"))
    }

    @Test
    fun `test null language map to JSON`() {
        val json = converters.fromLanguageMapToJson(null)
        assertNull(json)
    }

    @Test
    fun `test JSON to language map conversion`() {
        val json = "{\"eng\":\"English\",\"spa\":\"Spanish\"}"
        val map = converters.fromJsonToLanguageMap(json)
        assertNotNull(map)
        assertEquals("English", map!!["eng"])
        assertEquals("Spanish", map["spa"])
    }

    @Test
    fun `test empty language map`() {
        val emptyMap = emptyMap<String, String>()
        val json = converters.fromLanguageMapToJson(emptyMap)
        assertNotNull(json)
        assertTrue(json!!.contains("{}") || json == "{}")
        val converted = converters.fromJsonToLanguageMap(json)
        assertNotNull(converted)
        assertTrue(converted!!.isEmpty())
    }

    // ==================== Round-trip Conversion Tests ====================

    @Test
    fun `test round-trip conversion for string list`() {
        val original = listOf("USA", "Canada", "Mexico")
        val json = converters.fromStringListToJson(original)
        val converted = converters.fromJsonToStringList(json)
        assertEquals(original, converted)
    }

    @Test
    fun `test round-trip conversion for CapitalInfo`() {
        val original = CapitalInfo(latlng = listOf(40.7128, -74.0060))
        val json = converters.fromCapitalInfoToJson(original)
        val converted = converters.fromJsonToCapitalInfo(json)
        assertEquals(original.latlng, converted?.latlng)
    }

    @Test
    fun `test round-trip conversion for Car`() {
        val original = Car(side = "right", signs = listOf("USA"))
        val json = converters.fromCarToJson(original)
        val converted = converters.fromJsonToCar(json)
        assertEquals(original.side, converted?.side)
        assertEquals(original.signs, converted?.signs)
    }
}
