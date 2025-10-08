package com.codetutor.countryinfoapp.repository.service

import android.content.Context
import android.content.res.Resources
import com.codetutor.countryinfoapp.R
import com.codetutor.countryinfoapp.data.Country
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * Unit tests for the CountryListServiceProviderImpl class
 *
 * This test class demonstrates:
 * 1. How to mock Android dependencies (Context, Resources) using MockK
 * 2. How to test private methods using reflection
 * 3. How to test suspend functions with runBlocking
 * 4. How to test JSON deserialization
 * 5. How to handle and test error cases
 *
 * Note: MockK is a mocking library designed specifically for Kotlin, providing
 * idiomatic Kotlin syntax for mocking and verification.
 */
class CountryListServiceProviderImplTest {

    // Declare mock objects using MockK
    private lateinit var mockContext: Context
    private lateinit var mockResources: Resources

    // Class under test
    private lateinit var serviceProvider: CountryListServiceProviderImpl

    /**
     * Sample JSON representing a list of countries in the format
     * expected by the CountryListServiceProviderImpl.
     *
     * This test JSON contains minimal data required for testing but
     * follows the structure of the actual JSON from the API.
     */
    private val sampleCountriesJson = """
        [
            {
                "name": {
                    "common": "Togo",
                    "official": "Togolese Republic"
                },
                "capital": ["Lomé"],
                "region": "Africa",
                "subregion": "Western Africa",
                "flag": "🇹🇬",
                "continents": ["Africa"],
                "languages": {
                    "fra": "French"
                }
            },
            {
                "name": {
                    "common": "Brazil",
                    "official": "Federative Republic of Brazil"
                },
                "capital": ["Brasília"],
                "region": "Americas",
                "subregion": "South America",
                "flag": "🇧🇷",
                "continents": ["South America"],
                "languages": {
                    "por": "Portuguese"
                }
            }
        ]
    """.trimIndent()

    @Before
    fun setup() {
        // Initialize MockK mocks
        mockContext = mockk<Context>()
        mockResources = mockk<Resources>()

        // Configure the mock behavior using MockK syntax
        // When context.resources is called, return our mockResources
        every { mockContext.resources } returns mockResources

        // When resources.openRawResource() is called with R.raw.countries,
        // return an InputStream containing our sample JSON
        every { mockResources.openRawResource(R.raw.countries) } returns
            ByteArrayInputStream(sampleCountriesJson.toByteArray())

        // Create the instance of the class under test with the mocked context
        serviceProvider = CountryListServiceProviderImpl(mockContext)
    }

    /**
     * Test that the getJsonString method correctly reads the JSON from the raw resource.
     *
     * This test:
     * 1. Uses reflection to access the private method
     * 2. Verifies that the JSON string returned matches our sample data
     */
    @Test
    fun testGetJsonString_returnsCorrectJson() {
        // Use reflection to access the private method
        val getJsonStringMethod = CountryListServiceProviderImpl::class.java.getDeclaredMethod("getJsonString")
        getJsonStringMethod.isAccessible = true

        // Call the private method
        val jsonString = getJsonStringMethod.invoke(serviceProvider) as String

        // Assert that the returned JSON matches our sample
        assertEquals(sampleCountriesJson, jsonString)
    }

    /**
     * Test that getCountryList successfully parses the JSON into Country objects.
     *
     * This test:
     * 1. Uses runBlocking to handle the suspend function
     * 2. Verifies the correct number of countries is returned
     * 3. Verifies that the country properties match our sample data
     */
    @Test
    fun testGetCountryList_parsesJsonCorrectly() = runBlocking {
        // Call the method under test
        val countries = serviceProvider.getCountryList()

        // Verify we got the expected number of countries
        assertEquals(2, countries.size)

        // Verify the first country's data
        val togo = countries[0]
        assertEquals("Togo", togo.name?.common)
        assertEquals("Togolese Republic", togo.name?.official)
        assertEquals("Lomé", togo.capital?.get(0))
        assertEquals("Africa", togo.region)
        assertEquals("Western Africa", togo.subregion)
        assertEquals("🇹🇬", togo.flag)

        // Verify the second country's data
        val brazil = countries[1]
        assertEquals("Brazil", brazil.name?.common)
        assertEquals("Brasília", brazil.capital?.get(0))
        assertEquals("Americas", brazil.region)
    }

    /**
     * Test that getCountryList handles an empty JSON array correctly.
     *
     * This test verifies that the service can handle an empty list
     * without throwing exceptions.
     */
    @Test
    fun testGetCountryList_handlesEmptyList() = runBlocking {
        // Override the mock to return an empty JSON array
        val emptyJson = "[]"
        every { mockResources.openRawResource(R.raw.countries) } returns
            ByteArrayInputStream(emptyJson.toByteArray())

        // Recreate service provider with the updated mock
        val emptyServiceProvider = CountryListServiceProviderImpl(mockContext)

        // Call the method under test
        val countries = emptyServiceProvider.getCountryList()

        // Verify that we get an empty list, not null
        assertNotNull(countries)
        assertTrue(countries.isEmpty())
    }

    /**
     * Test that getCountryList handles IO exceptions correctly.
     *
     * This test:
     * 1. Creates a mock InputStream that throws an exception
     * 2. Verifies that the exception is propagated
     */
    @Test
    fun testGetCountryList_handlesIOException() = runBlocking {
        // Create a mock InputStream that throws an exception when read
        val errorStream = object : InputStream() {
            override fun read(): Int {
                throw java.io.IOException("Test exception")
            }
        }

        // Override the mock to return our error-throwing stream
        every { mockResources.openRawResource(R.raw.countries) } returns errorStream

        // Recreate service provider with the updated mock
        val errorServiceProvider = CountryListServiceProviderImpl(mockContext)

        // The test will pass if an exception is thrown
        var exceptionThrown = false

        try {
            errorServiceProvider.getCountryList()
        } catch (e: Exception) {
            exceptionThrown = true
        }

        // Verify that an exception was thrown
        assertTrue("Expected an exception to be thrown", exceptionThrown)
    }

    /**
     * Test that getCountryList handles malformed JSON correctly.
     *
     * This test:
     * 1. Provides invalid JSON that will fail to parse
     * 2. Verifies that an appropriate exception is thrown
     */
    @Test
    fun testGetCountryList_handlesMalformedJson() = runBlocking {
        // Create invalid JSON (missing closing bracket)
        val invalidJson = """
            [
                {
                    "name": {
                        "common": "Invalid Country"
                    
                }
            ]
        """.trimIndent()

        // Override the mock to return our invalid JSON
        every { mockResources.openRawResource(R.raw.countries) } returns
            ByteArrayInputStream(invalidJson.toByteArray())

        // Recreate service provider with the updated mock
        val invalidJsonProvider = CountryListServiceProviderImpl(mockContext)

        // The test will pass if an exception is thrown during parsing
        var exceptionThrown = false

        try {
            invalidJsonProvider.getCountryList()
        } catch (e: Exception) {
            exceptionThrown = true
        }

        // Verify that an exception was thrown
        assertTrue("Expected an exception to be thrown for malformed JSON", exceptionThrown)
    }

    /**
     * Test that getCountryList handles JSON with missing fields correctly.
     *
     * This test:
     * 1. Provides valid JSON but with some fields missing
     * 2. Verifies that objects are created with null values for missing fields
     */
    @Test
    fun testGetCountryList_handlesMissingFields() = runBlocking {
        // JSON with missing fields
        val incompleteJson = """
            [
                {
                    "name": {
                        "common": "Partial Country"
                    }
                }
            ]
        """.trimIndent()

        // Override the mock to return our incomplete JSON
        every { mockResources.openRawResource(R.raw.countries) } returns
            ByteArrayInputStream(incompleteJson.toByteArray())

        // Recreate service provider with the updated mock
        val incompleteJsonProvider = CountryListServiceProviderImpl(mockContext)

        // Call the method under test
        val countries = incompleteJsonProvider.getCountryList()

        // Verify we got one country
        assertEquals(1, countries.size)

        // Verify that the country has the provided field
        assertEquals("Partial Country", countries[0].name?.common)

        // Verify that missing fields are null
        assertEquals(null, countries[0].capital)
        assertEquals(null, countries[0].region)
        assertEquals(null, countries[0].subregion)
    }

    /**
     * Phase 8 Enhancement - Test that getCountryList handles missing resource file correctly.
     *
     * This test:
     * 1. Mocks Resources.NotFoundException when trying to access the resource
     * 2. Verifies that the exception is properly propagated
     */
    @Test
    fun testGetCountryList_handlesMissingResource() = runBlocking {
        // Mock the resources to throw NotFoundException when trying to access the raw resource
        every { mockResources.openRawResource(R.raw.countries) } throws 
            android.content.res.Resources.NotFoundException("Resource not found")

        // Recreate service provider with the updated mock
        val missingResourceProvider = CountryListServiceProviderImpl(mockContext)

        // The test will pass if an exception is thrown
        var exceptionThrown = false
        var correctExceptionType = false

        try {
            missingResourceProvider.getCountryList()
        } catch (e: android.content.res.Resources.NotFoundException) {
            exceptionThrown = true
            correctExceptionType = true
        } catch (e: Exception) {
            exceptionThrown = true
        }

        // Verify that the correct exception was thrown
        assertTrue("Expected a Resources.NotFoundException to be thrown", exceptionThrown)
        assertTrue("Expected Resources.NotFoundException specifically", correctExceptionType)
    }

    /**
     * Phase 8 Enhancement - Test parsing error handling with completely invalid JSON.
     *
     * This test:
     * 1. Provides completely invalid JSON (not even valid JSON format)
     * 2. Verifies that a parsing exception is thrown
     */
    @Test
    fun testGetCountryList_handlesInvalidJsonResource() = runBlocking {
        // Create completely invalid JSON (not valid JSON at all)
        val invalidJson = "This is not JSON at all! {[}]"

        // Override the mock to return our invalid JSON
        every { mockResources.openRawResource(R.raw.countries) } returns
            ByteArrayInputStream(invalidJson.toByteArray())

        // Recreate service provider with the updated mock
        val invalidJsonProvider = CountryListServiceProviderImpl(mockContext)

        // The test will pass if a serialization exception is thrown
        var exceptionThrown = false
        var isSerializationException = false

        try {
            invalidJsonProvider.getCountryList()
        } catch (e: kotlinx.serialization.SerializationException) {
            exceptionThrown = true
            isSerializationException = true
        } catch (e: Exception) {
            exceptionThrown = true
        }

        // Verify that an exception was thrown
        assertTrue("Expected a SerializationException to be thrown for invalid JSON", exceptionThrown)
        assertTrue("Expected SerializationException specifically", isSerializationException)
    }

    /**
     * Phase 8 Enhancement - Test valid JSON parsing from resources (comprehensive).
     *
     * This test:
     * 1. Uses a more comprehensive valid JSON sample
     * 2. Verifies all important fields are parsed correctly
     * 3. Tests the complete happy path scenario
     */
    @Test
    fun testGetCountryList_validJsonFromResources() = runBlocking {
        // Comprehensive valid JSON with more fields
        val comprehensiveJson = """
            [
                {
                    "name": {
                        "common": "Germany",
                        "official": "Federal Republic of Germany"
                    },
                    "capital": ["Berlin"],
                    "region": "Europe",
                    "subregion": "Western Europe",
                    "flag": "🇩🇪",
                    "continents": ["Europe"],
                    "languages": {
                        "languages": {
                            "deu": "German"
                        }
                    },
                    "currencies": {
                        "EUR": {
                            "name": "Euro",
                            "symbol": "€"
                        }
                    },
                    "car": {
                        "signs": ["D"],
                        "side": "right"
                    }
                }
            ]
        """.trimIndent()

        // Override the mock to return our comprehensive JSON
        every { mockResources.openRawResource(R.raw.countries) } returns
            ByteArrayInputStream(comprehensiveJson.toByteArray())

        // Recreate service provider with the updated mock
        val validJsonProvider = CountryListServiceProviderImpl(mockContext)

        // Call the method under test
        val countries = validJsonProvider.getCountryList()

        // Verify we got the expected result
        assertNotNull("Country list should not be null", countries)
        assertEquals("Should have exactly one country", 1, countries.size)

        val germany = countries[0]
        
        // Verify all important fields are correctly parsed
        assertEquals("Germany", germany.name?.common)
        assertEquals("Federal Republic of Germany", germany.name?.official)
        assertEquals("Berlin", germany.capital?.get(0))
        assertEquals("Europe", germany.region)
        assertEquals("Western Europe", germany.subregion)
        assertEquals("🇩🇪", germany.flag)
        assertNotNull("Continents should not be null", germany.continents)
        assertEquals("Europe", germany.continents?.get(0))
        assertNotNull("Languages should not be null", germany.languages)
        assertTrue("Should contain German language", germany.languages?.languages?.isNotEmpty() == true)
    }
}
