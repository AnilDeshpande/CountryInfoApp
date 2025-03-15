package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Languages
import com.codetutor.countryinfoapp.data.Name
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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
            languages = Languages(nor = "English"),
            capital = listOf("City1")
        ),
        Country(
            id = 2,
            name = Name("Country2"),
            continents = listOf("Asia"),
            languages = Languages(nor = "Japanese"),
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
        assertEquals(testCountries.size, countries.size)
        // For example, compare the first country's name.
        assertEquals("Country1", countries[0].name?.common)
    }

    @Test
    fun testDeleteCountry_deletesCountry() = runBlocking {
        repository.fetchAndInsertAll()
        val initialCount = repository.getAllCountries().size

        // Delete first country.
        repository.deleteCountry(testCountries[0])
        val countriesAfterDelete = repository.getAllCountries()
        assertEquals(initialCount - 1, countriesAfterDelete.size)
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
        assertNotNull(updatedCountry)
        assertEquals(listOf(newCapital), updatedCountry?.capital)
    }

    @Test
    fun testFilterCountries_returnsMatchingCountries() = runBlocking {
        repository.fetchAndInsertAll()
        // Create a filter that expects countries in Europe.
        val filter = FilterByContinent("Europe")
        val filteredCountries = repository.filterCountries(filter)
        // Verify that the filtered list returns only countries that have Europe as a continent.
        assertTrue(filteredCountries.all { it.continents?.contains("Europe") == true })
    }
}