package com.codetutor.countryinfoapp.repository.service

import com.codetutor.countryinfoapp.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.codetutor.countryinfoapp.TestDataFactory
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.service.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * TestableCountryListProviderViaNetwork allows injecting a custom ApiService for testing
 */
class TestableCountryListProviderViaNetwork(
    private val apiService: ApiService
) : CountryListServiceProvider {
    
    override suspend fun getCountryList(): MutableList<Country> {
        val response = apiService.getAllCountries()
        if (response.isSuccessful) {
            return response.body() ?: mutableListOf()
        } else {
            throw RuntimeException("HTTP ${response.code()}: ${response.message()}")
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CountryListProviderViaNetworkTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var countryListProvider: TestableCountryListProviderViaNetwork
    private lateinit var gson: Gson

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        gson = Gson()
        
        // Create Retrofit instance pointing to mock server
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            
        apiService = retrofit.create(ApiService::class.java)
        countryListProvider = TestableCountryListProviderViaNetwork(apiService)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getCountryList returns non-empty list on successful response`() = runTest {
        // Arrange
        val testCountries = TestDataFactory.createCountryList(3)
        val jsonResponse = gson.toJson(testCountries)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val result = countryListProvider.getCountryList()

        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        assertEquals("Country 1", result[0].name?.common)
        assertEquals("Country 2", result[1].name?.common)
        assertEquals("Country 3", result[2].name?.common)

        // Verify request was made correctly
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path?.contains("/v3.1/all") == true)
        assertTrue(request.path?.contains("fields=") == true)
    }

    @Test(expected = RuntimeException::class)
    fun `getCountryList throws exception on server error`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        // Act & Assert
        countryListProvider.getCountryList()
    }

    @Test(expected = RuntimeException::class)
    fun `getCountryList throws exception on client error`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("Not Found")
        )

        // Act & Assert
        countryListProvider.getCountryList()
    }

    @Test
    fun `getCountryList handles empty array response`() = runTest {
        // Arrange
        val emptyList = emptyList<Country>()
        val jsonResponse = gson.toJson(emptyList)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val result = countryListProvider.getCountryList()

        // Assert
        assertNotNull(result)
        assertEquals(0, result.size)
    }

    @Test(expected = Exception::class)
    fun `getCountryList throws exception on malformed JSON`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{ invalid json }")
                .setHeader("Content-Type", "application/json")
        )

        // Act & Assert
        countryListProvider.getCountryList()
    }

    @Test
    fun `getCountryList throws IOException on network timeout`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        // Act & Assert
        try {
            countryListProvider.getCountryList()
            fail("Expected IOException to be thrown")
        } catch (e: Exception) {
            assertTrue("Expected IOException or timeout exception", 
                e is IOException || e.cause is IOException)
        }
    }

    @Test(expected = IOException::class)
    fun `getCountryList throws IOException on connection failure`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        )

        // Act & Assert
        countryListProvider.getCountryList()
    }

    @Test
    fun `getCountryList handles large response successfully`() = runTest {
        // Arrange
        val largeCountryList = TestDataFactory.createCountryList(100)
        val jsonResponse = gson.toJson(largeCountryList)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val result = countryListProvider.getCountryList()

        // Assert
        assertNotNull(result)
        assertEquals(100, result.size)
        assertEquals("Country 1", result[0].name?.common)
        assertEquals("Country 100", result[99].name?.common)
    }

    @Test
    fun `getCountryList handles response with null values gracefully`() = runTest {
        // Arrange
        val countryWithNulls = TestDataFactory.createCountry(
            name = "Test Country",
            region = null,
            capital = null,
            population = null
        )
        val jsonResponse = gson.toJson(listOf(countryWithNulls))
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val result = countryListProvider.getCountryList()

        // Assert
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Test Country", result[0].name?.common)
        assertNull(result[0].region)
        assertNull(result[0].capital)
        assertNull(result[0].population)
    }

    @Test
    fun `getCountryList handles special characters in response`() = runTest {
        // Arrange
        val countryWithSpecialChars = TestDataFactory.createCountry(
            name = "São Tomé and Príncipe",
            region = "África"
        )
        val jsonResponse = gson.toJson(listOf(countryWithSpecialChars))
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json; charset=utf-8")
        )

        // Act
        val result = countryListProvider.getCountryList()

        // Assert
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("São Tomé and Príncipe", result[0].name?.common)
        assertEquals("África", result[0].region)
    }

    @Test
    fun `getCountryList handles slow response within reasonable time`() = runTest {
        // Arrange
        val testCountries = TestDataFactory.createCountryList(1)
        val jsonResponse = gson.toJson(testCountries)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setBodyDelay(2, TimeUnit.SECONDS) // Simulate slow network
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val result = countryListProvider.getCountryList()

        // Assert
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Country 1", result[0].name?.common)
    }
}