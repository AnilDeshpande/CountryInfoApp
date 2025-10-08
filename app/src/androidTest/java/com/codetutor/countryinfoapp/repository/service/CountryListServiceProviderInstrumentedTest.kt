package com.codetutor.countryinfoapp.repository.service

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codetutor.countryinfoapp.data.Country
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android Instrumented Test for CountryListServiceProviderImpl
 * 
 * This is an instrumented test that runs on an Android device/emulator.
 * It can access real Android resources and doesn't require complex mocking.
 * 
 * Benefits over unit tests for this scenario:
 * 1. Access to real Android Context and Resources
 * 2. Can read actual resource files (R.raw.countries)
 * 3. No complex mocking of Android framework classes
 * 4. No JaCoCo instrumentation conflicts
 * 5. More realistic testing environment
 */
@RunWith(AndroidJUnit4::class)
class CountryListServiceProviderInstrumentedTest {

    private lateinit var context: Context
    private lateinit var serviceProvider: CountryListServiceProviderImpl

    @Before
    fun setup() {
        // Get real Android context - no mocking needed!
        context = ApplicationProvider.getApplicationContext()
        
        // Create service provider with real context
        serviceProvider = CountryListServiceProviderImpl(context)
    }

    /**
     * Test that verifies the service can successfully load and parse
     * the actual countries.json resource file.
     * 
     * This test:
     * 1. Uses the real Android context and resources
     * 2. Loads the actual R.raw.countries resource
     * 3. Verifies that countries are parsed correctly
     * 4. Checks that we get a reasonable number of countries
     */
    @Test
    fun testGetCountryList_validJsonFromResources() = runBlocking {
        // Call the method under test - this will use the real resource file
        val countries = serviceProvider.getCountryList()

        // Verify we got a valid result
        assertNotNull("Country list should not be null", countries)
        assertTrue("Should have countries loaded from resource", countries.isNotEmpty())
        
        // Verify we have a reasonable number of countries (should be 195+ countries in the world)
        assertTrue("Should have at least 190 countries", countries.size >= 190)
        
        // Pick a well-known country to verify parsing worked correctly
        val germany = countries.find { it.name?.common == "Germany" }
        assertNotNull("Germany should be in the list", germany)
        
        germany?.let { country ->
            assertEquals("Germany", country.name?.common)
            assertNotNull("Germany should have capital", country.capital)
            assertEquals("Berlin", country.capital?.get(0))
            assertEquals("Europe", country.region)
            assertEquals("🇩🇪", country.flag)
            assertNotNull("Germany should have languages", country.languages)
        }
        
        // Test another country to ensure multiple countries are parsed
        val brazil = countries.find { it.name?.common == "Brazil" }
        assertNotNull("Brazil should be in the list", brazil)
        
        brazil?.let { country ->
            assertEquals("Brazil", country.name?.common)
            assertEquals("Americas", country.region)
            assertEquals("🇧🇷", country.flag)
        }
    }

    /**
     * Test that verifies all countries have basic required fields
     */
    @Test
    fun testGetCountryList_allCountriesHaveBasicFields() = runBlocking {
        val countries = serviceProvider.getCountryList()
        
        assertNotNull("Country list should not be null", countries)
        assertTrue("Should have countries", countries.isNotEmpty())
        
        // Check that all countries have at least a common name
        countries.forEach { country ->
            assertNotNull("Each country should have a name", country.name)
            assertNotNull("Each country should have a common name", country.name?.common)
            assertFalse("Common name should not be empty", country.name?.common.isNullOrBlank())
        }
    }

    /**
     * Test that verifies specific country data integrity
     */
    @Test
    fun testGetCountryList_specificCountryData() = runBlocking {
        val countries = serviceProvider.getCountryList()
        
        // Test a few specific countries to ensure data integrity
        val testCases = mapOf(
            "United States" to "Americas",
            "Japan" to "Asia",
            "France" to "Europe",
            "Australia" to "Oceania",
            "Egypt" to "Africa"
        )
        
        testCases.forEach { (countryName, expectedRegion) ->
            val country = countries.find { it.name?.common == countryName }
            assertNotNull("$countryName should be in the list", country)
            assertEquals("$countryName should be in $expectedRegion", expectedRegion, country?.region)
        }
    }
}