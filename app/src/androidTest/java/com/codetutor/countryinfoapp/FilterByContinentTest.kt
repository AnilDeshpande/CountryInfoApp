package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.data.Country
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

}