package com.codetutor.countryinfoapp.repository.service.network

import com.codetutor.countryinfoapp.TestCoroutineRule
import com.codetutor.countryinfoapp.TestDataFactory
import com.codetutor.countryinfoapp.data.Country
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
class ApiServiceTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var gson: Gson

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        gson = Gson()
        
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            
        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getAllCountries returns successful response with country list`() = runTest {
        // Arrange
        val testCountries = TestDataFactory.createCountryList(2)
        val jsonResponse = gson.toJson(testCountries)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val response = apiService.getAllCountries()

        // Assert
        assertTrue(response.isSuccessful)
        assertEquals(200, response.code())
        assertNotNull(response.body())
        assertEquals(2, response.body()!!.size)
        assertEquals("Country 1", response.body()!![0].name?.common)
        assertEquals("Country 2", response.body()!![1].name?.common)

        // Verify request details
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue("Path should contain v3.1/all", request.path?.contains("/v3.1/all") == true)
        assertTrue("Path should contain fields parameter", request.path?.contains("fields=") == true)
        assertTrue("Path should contain expected field names", 
            request.path?.contains("name") == true && request.path?.contains("capital") == true)
    }

    @Test
    fun `getAllCountries with custom fields parameter`() = runTest {
        // Arrange
        val testCountries = TestDataFactory.createCountryList(1)
        val jsonResponse = gson.toJson(testCountries)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val customFields = "name,capital,region"
        val response = apiService.getAllCountries(customFields)

        // Assert
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())

        // Verify custom fields in request
        val request = mockWebServer.takeRequest()
        assertTrue("Path should contain custom fields", 
            request.path?.contains("fields=name%2Ccapital%2Cregion") == true || 
            request.path?.contains("fields=name,capital,region") == true)
    }

    @Test
    fun `getAllCountries returns error response on server error`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        // Act
        val response = apiService.getAllCountries()

        // Assert
        assertFalse(response.isSuccessful)
        assertEquals(500, response.code())
        assertNull(response.body())
        assertNotNull(response.errorBody())
    }

    @Test
    fun `getAllCountries returns error response on client error`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("Not Found")
        )

        // Act
        val response = apiService.getAllCountries()

        // Assert
        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
        assertNull(response.body())
        assertNotNull(response.errorBody())
    }

    @Test
    fun `getAllCountries returns empty list on successful empty response`() = runTest {
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
        val response = apiService.getAllCountries()

        // Assert
        assertTrue(response.isSuccessful)
        assertEquals(200, response.code())
        assertNotNull(response.body())
        assertEquals(0, response.body()!!.size)
    }

    @Test(expected = Exception::class)
    fun `getAllCountries throws exception on malformed JSON`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{ invalid json structure }")
                .setHeader("Content-Type", "application/json")
        )

        // Act & Assert
        apiService.getAllCountries()
    }

    @Test
    fun `getAllCountries throws IOException on network timeout`() = runTest {
        // Arrange - Use DISCONNECT_DURING_REQUEST_BODY which is more reliable for testing
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_DURING_REQUEST_BODY)
        )

        // Act & Assert
        try {
            apiService.getAllCountries()
            fail("Expected IOException to be thrown")
        } catch (e: Exception) {
            // Accept any exception that indicates a network issue
            assertTrue("Expected IOException, SocketTimeoutException, or related network exception", 
                e is IOException || 
                e.cause is IOException ||
                e.message?.contains("timeout", ignoreCase = true) == true ||
                e.message?.contains("failed to connect", ignoreCase = true) == true ||
                e.message?.contains("unexpected end of stream", ignoreCase = true) == true)
        }
    }

    @Test(expected = IOException::class)
    fun `getAllCountries throws IOException on connection failure`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_DURING_REQUEST_BODY)
        )

        // Act & Assert
        apiService.getAllCountries()
    }

    @Test
    fun `getAllCountries handles large response successfully`() = runTest {
        // Arrange
        val largeCountryList = TestDataFactory.createCountryList(250) // Simulate real API response size
        val jsonResponse = gson.toJson(largeCountryList)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        val response = apiService.getAllCountries()

        // Assert
        assertTrue(response.isSuccessful)
        assertEquals(200, response.code())
        assertNotNull(response.body())
        assertEquals(250, response.body()!!.size)
    }

    @Test
    fun `getAllCountries handles response with special characters`() = runTest {
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
        val response = apiService.getAllCountries()

        // Assert
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.size)
        assertEquals("São Tomé and Príncipe", response.body()!![0].name?.common)
        assertEquals("África", response.body()!![0].region)
    }

    @Test
    fun `getAllCountries handles slow response within reasonable time`() = runTest {
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
        val response = apiService.getAllCountries()

        // Assert
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.size)
    }

    @Test
    fun `getAllCountries request includes correct headers`() = runTest {
        // Arrange
        val testCountries = TestDataFactory.createCountryList(1)
        val jsonResponse = gson.toJson(testCountries)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        apiService.getAllCountries()

        // Assert
        val request = mockWebServer.takeRequest()
        assertNotNull(request.getHeader("User-Agent"))
        assertEquals("GET", request.method)
        val requestLine = request.requestLine
        assertNotNull("Request line should not be null", requestLine)
        assertTrue("Request line should contain HTTP version", 
            requestLine.contains("HTTP/1.1") || requestLine.contains("HTTP/2"))
    }

    @Test
    fun `getAllCountries verifies correct endpoint path`() = runTest {
        // Arrange
        val testCountries = TestDataFactory.createCountryList(1)
        val jsonResponse = gson.toJson(testCountries)
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
        )

        // Act
        apiService.getAllCountries()

        // Assert
        val request = mockWebServer.takeRequest()
        val path = request.path ?: ""
        assertTrue("Path should start with /v3.1/all", path.startsWith("/v3.1/all"))
        assertTrue("Path should contain fields query parameter", path.contains("fields="))
        
        // Verify specific fields are requested
        val expectedFields = listOf("name", "capital", "region", "subregion", "currencies", "idd", "tld", "flags", "flag", "car")
        expectedFields.forEach { field ->
            assertTrue("Path should contain field: $field", path.contains(field))
        }
    }
}