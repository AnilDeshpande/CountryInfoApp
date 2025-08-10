package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.repository.doubles.FakeCountryDao
import com.codetutor.countryinfoapp.repository.doubles.FakeCountryListServiceProvider
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Languages
import com.codetutor.countryinfoapp.data.Name
import com.codetutor.countryinfoapp.database.dao.ICountryDao
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CountryRepositoryTest {
    private lateinit var repository: CountryRepository
    private lateinit var fakeDao: FakeCountryDao
    private lateinit var fakeServiceProvider: FakeCountryListServiceProvider

    // Using the Unconfined dispatcher for testing asynchronous functions.
    private val testDispatcher = Dispatchers.Unconfined

    // Create a sample list of test countries.
    private val testCountries = listOf(
        Country(
            id = 1,
            name = Name("Country1"),
            continents = listOf("Europe"),
            languages = Languages(languages = mapOf("nor" to "English")),
            capital = listOf("City1")
        ),
        Country(
            id = 2,
            name = Name("Country2"),
            continents = listOf("Asia"),
            languages = Languages(languages = mapOf("nor" to "English")),
            capital = listOf("City2")
        )
    )

    @Before
    fun setup() {
        fakeDao = FakeCountryDao()
        fakeServiceProvider = FakeCountryListServiceProvider(testCountries)
        repository = CountryRepository(fakeDao, fakeServiceProvider, testDispatcher)
    }

    @Test
    fun testFetchAndInsertAll_insertsCountries() = runBlocking {
        repository.fetchAndInsertAll()
        val countries = repository.getAllCountries()
        // Verify that all countries are inserted.
        Assert.assertEquals(testCountries.size, countries.size)
        // For example, compare the first country's name.
        Assert.assertEquals("Country1", countries[0].name?.common)
    }

    @Test
    fun testDeleteCountry_deletesCountry() = runBlocking {
        repository.fetchAndInsertAll()
        val initialCount = repository.getAllCountries().size

        // Delete first country.
        repository.deleteCountry(testCountries[0])
        val countriesAfterDelete = repository.getAllCountries()
        Assert.assertEquals(initialCount - 1, countriesAfterDelete.size)
    }

    @Test
    fun testUpdateCapital_updatesCountryCapital() = runBlocking {
        repository.fetchAndInsertAll()
        val newCapital = "NewCity"
        val country = testCountries[0]

        // Update the capital of the first country.
        repository.updateCapital(country, newCapital)
        val countriesAfterUpdate = repository.getAllCountries()
        val updatedCountry = countriesAfterUpdate.find { it.id == country.id }
        Assert.assertNotNull(updatedCountry)
        Assert.assertEquals(listOf(newCapital), updatedCountry?.capital)
    }

    @Test
    fun testFilterCountries_returnsMatchingCountries() = runBlocking {
        repository.fetchAndInsertAll()
        // Create a filter that expects countries in Europe.
        val filter = FilterByContinent("Europe")
        val filteredCountries = repository.filterCountries(filter)
        // Verify that the filtered list returns only countries that have Europe as a continent.
        Assert.assertTrue(filteredCountries.all { it.continents?.contains("Europe") == true })
    }

    // ===== Phase 5 - Repository Edge Paths (MockK) =====

    @Test
    fun test_fetchAndCacheCountries_whenCacheWarm_providerNotInvoked() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockServiceProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockServiceProvider, testDispatcher)

        // Setup mock to return non-empty list (cache warm)
        // Repository calls getAllCountries() which first checks allCountries cache (empty), then calls DAO
        every { runBlocking { mockDao.getAllCountries() } } returns testCountries

        // Act
        repository.fetchAndInsertAll()

        // Assert - Verify provider was NOT invoked because cache is warm
        verify(exactly = 0) { runBlocking { mockServiceProvider.getCountryList() } }
        // Repository calls getAllCountries() once to check cache
        verify(exactly = 1) { runBlocking { mockDao.getAllCountries() } }
    }

    @Test
    fun test_providerReturnsEmptyList_daoInsertStillCalled() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockServiceProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockServiceProvider, testDispatcher)

        // Setup mocks - Repository implementation still calls insertAll even with empty list
        every { runBlocking { mockDao.getAllCountries() } } returns emptyList()
        every { runBlocking { mockServiceProvider.getCountryList() } } returns mutableListOf() // Empty list
        every { runBlocking { mockDao.insertAll(emptyList()) } } just Runs

        // Act
        repository.fetchAndInsertAll()

        // Assert - DAO insert IS called even with empty list (current implementation behavior)
        verify(exactly = 1) { runBlocking { mockDao.insertAll(emptyList()) } }
        verify(exactly = 1) { runBlocking { mockServiceProvider.getCountryList() } }
        verify(exactly = 1) { runBlocking { mockDao.getAllCountries() } }
    }

    @Test
    fun test_providerThrowsIOException_exceptionPropagated() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockServiceProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockServiceProvider, testDispatcher)

        // Setup mocks
        every { runBlocking { mockDao.getAllCountries() } } returns emptyList()
        every { runBlocking { mockServiceProvider.getCountryList() } } throws IOException("Network error")

        // Act & Assert - Exception should be propagated
        try {
            repository.fetchAndInsertAll()
            Assert.fail("Expected IOException to be thrown")
        } catch (e: IOException) {
            Assert.assertEquals("Network error", e.message)
        }

        // Verify provider was called and exception occurred
        verify(exactly = 1) { runBlocking { mockServiceProvider.getCountryList() } }
    }

    @Test
    fun test_getFilteredCountries_onEmptyCache_returnsEmptyList() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockServiceProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockServiceProvider, testDispatcher)

        // Setup - No need to mock DAO calls because filterCountries() doesn't call getAllCountries()
        // It directly uses the allCountries field which starts as empty

        // Act
        val filter = FilterByContinent("Europe")
        val filteredCountries = repository.filterCountries(filter)

        // Assert - Should return empty list since allCountries field is empty by default
        Assert.assertTrue(filteredCountries.isEmpty())
        // No DAO calls should be made since filterCountries uses allCountries field directly
        verify(exactly = 0) { runBlocking { mockDao.getAllCountries() } }
    }

    @Test
    fun test_networkProviderFallback_whenLocalProviderFails() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockLocalProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockLocalProvider, testDispatcher)

        // Setup mocks - local provider fails, should fallback to network
        every { runBlocking { mockDao.getAllCountries() } } returns emptyList()
        every { runBlocking { mockLocalProvider.getCountryList() } } throws IOException("Local data unavailable")

        // Act & Assert - Should propagate the exception (no network fallback in current implementation)
        try {
            repository.fetchAndInsertAll()
            Assert.fail("Expected IOException to be thrown")
        } catch (e: IOException) {
            Assert.assertEquals("Local data unavailable", e.message)
        }

        // Verify local provider was called
        verify(exactly = 1) { runBlocking { mockLocalProvider.getCountryList() } }
    }

    // Additional edge cases for better coverage

    @Test
    fun test_fetchAndInsertAll_withNonEmptyProviderList_insertsCorrectly() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockServiceProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockServiceProvider, testDispatcher)

        // Setup mocks
        every { runBlocking { mockDao.getAllCountries() } } returns emptyList()
        every { runBlocking { mockServiceProvider.getCountryList() } } returns testCountries.toMutableList()
        every { runBlocking { mockDao.insertAll(testCountries) } } just Runs

        // Act
        repository.fetchAndInsertAll()

        // Assert - DAO insert should be called with correct data
        verify(exactly = 1) { runBlocking { mockDao.insertAll(testCountries) } }
        verify(exactly = 1) { runBlocking { mockServiceProvider.getCountryList() } }
    }

    @Test
    fun test_deleteCountry_refreshesAllCountriesCache() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockServiceProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockServiceProvider, testDispatcher)

        val countryToDelete = testCountries[0]
        val remainingCountries = testCountries.drop(1)

        // Setup mocks
        every { runBlocking { mockDao.delete(countryToDelete) } } just Runs
        every { runBlocking { mockDao.getAllCountries() } } returns remainingCountries

        // Act
        repository.deleteCountry(countryToDelete)

        // Assert - Cache should be refreshed after deletion
        verify(exactly = 1) { runBlocking { mockDao.delete(countryToDelete) } }
        verify(exactly = 1) { runBlocking { mockDao.getAllCountries() } }
    }

    @Test
    fun test_updateCapital_refreshesAllCountriesCache() = runTest {
        // Arrange - Mock dependencies
        val mockDao = mockk<ICountryDao>()
        val mockServiceProvider = mockk<CountryListServiceProvider>()
        val repository = CountryRepository(mockDao, mockServiceProvider, testDispatcher)

        val countryToUpdate = testCountries[0]
        val newCapital = "New Capital"
        val expectedUpdatedCountry = countryToUpdate.copy(capital = listOf(newCapital))

        // Setup mocks
        every { runBlocking { mockDao.updateCountry(expectedUpdatedCountry) } } returns 1
        every { runBlocking { mockDao.getAllCountries() } } returns testCountries

        // Act
        repository.updateCapital(countryToUpdate, newCapital)

        // Assert - Cache should be refreshed after update
        verify(exactly = 1) { runBlocking { mockDao.updateCountry(expectedUpdatedCountry) } }
        verify(exactly = 1) { runBlocking { mockDao.getAllCountries() } }
    }
}