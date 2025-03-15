package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Name
import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.util.TestDataLoader
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterByContinentTest {

    private lateinit var testCountries: List<Country>

    @Before
    fun setup() {
        testCountries = TestDataLoader.loadCountries()
    }

    @Test
    fun filterShouldReturnCountriesInSpecifiedContinent() = runBlocking {
        // Given
        val filter = FilterByContinent("Europe")

        // When
        val result = filter.filter(testCountries)

        // Then
        assert(result.isNotEmpty())
        assert(result.all { it.continents?.contains("Europe") == true })
    }

    @Test
    fun filterShouldReturnEmptyWhenNoCountryMatches() = runBlocking {
        // Create a list with no countries in "Africa"
        val countries = listOf(
            Country(name = Name("Country1"), continents = listOf("Europe")),
            Country(name = Name("Country2"), continents = listOf("Asia"))
        )
        val filter = FilterByContinent("Africa")
        val result = filter.filter(countries)
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldReturnEmptyForEmptyCountryList() = runBlocking {
        // Given an empty country list
        val filter = FilterByContinent("Europe")
        val result = filter.filter(emptyList())
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldReturnEmptyWhenContinentIsNull() = runBlocking {
        // Filtering when continent is null should yield an empty list
        val countries = listOf(
            Country(name = Name("Country1"), continents = listOf("Europe")),
            Country(name = Name("Country2"), continents = listOf("Asia"))
        )
        val filter = FilterByContinent(null)
        val result = filter.filter(countries)
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldReturnOnlyMatchingCountriesAmongMultiple() = runBlocking {
        // Given some countries have "Asia" and some do not
        val countries = listOf(
            Country(name = Name("Country1"), continents = listOf("Asia", "Europe")),
            Country(name = Name("Country2"), continents = listOf("Asia")),
            Country(name = Name("Country3"), continents = listOf("Europe"))
        )
        val filter = FilterByContinent("Asia")
        val result = filter.filter(countries)
        assertEquals(2, result.size)
        assert(result.all { it.continents?.contains("Asia") == true })
    }


}