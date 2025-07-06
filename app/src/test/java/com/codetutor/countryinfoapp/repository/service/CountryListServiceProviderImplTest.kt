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
}
