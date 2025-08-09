package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Name
import com.codetutor.countryinfoapp.repository.FilterByContinent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterByContinentTest {

    @Test
    fun filterShouldReturnCountriesMatchingContinent() = runBlocking {
        // Countries with matching region "Europe"
        val countries = listOf(
            Country(name = Name("Germany"), region = "Europe"),
            Country(name = Name("Brazil"), region = "South America"),
            Country(name = Name("France"), region = "Europe"),
            Country(name = Name("Japan"), region = "Asia")
        )
        val filter = FilterByContinent("Europe")
        val result = filter.filter(countries)
        
        assertEquals(2, result.size)
        result.forEach { country -> 
            assertEquals("Europe", country.region)
        }
    }

    @Test
    fun filterShouldReturnCountriesMatchingContinentIgnoringCase() = runBlocking {
        // Test case insensitive matching
        val countries = listOf(
            Country(name = Name("Germany"), region = "Europe"),
            Country(name = Name("Brazil"), region = "South America")
        )
        val filter = FilterByContinent("europe") // lowercase
        val result = filter.filter(countries)
        
        assertEquals(1, result.size)
        assertEquals("Europe", result.first().region)
    }

    @Test
    fun filterShouldReturnEmptyWhenNoCountryMatches() = runBlocking {
        // No country has region "Antarctica"
        val countries = listOf(
            Country(name = Name("Germany"), region = "Europe"),
            Country(name = Name("Brazil"), region = "South America")
        )
        val filter = FilterByContinent("Antarctica")
        val result = filter.filter(countries)
        
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldReturnAllCountriesForEmptyInput() = runBlocking {
        // Empty continent should return all countries
        val countries = listOf(
            Country(name = Name("Germany"), region = "Europe"),
            Country(name = Name("Brazil"), region = "South America")
        )
        val filter = FilterByContinent("")
        val result = filter.filter(countries)
        
        assertEquals(countries.size, result.size)
        assertEquals(countries, result)
    }

    @Test
    fun filterShouldReturnAllCountriesForNullInput() = runBlocking {
        // Null continent should return all countries
        val countries = listOf(
            Country(name = Name("Germany"), region = "Europe"),
            Country(name = Name("Brazil"), region = "South America")
        )
        val filter = FilterByContinent(null)
        val result = filter.filter(countries)
        
        assertEquals(countries.size, result.size)
        assertEquals(countries, result)
    }

    @Test
    fun filterShouldReturnEmptyForEmptyCountryList() = runBlocking {
        // Empty country list should return empty list
        val filter = FilterByContinent("Europe")
        val result = filter.filter(emptyList())
        
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldHandleCountriesWithNullRegion() = runBlocking {
        // Countries with null region should be ignored by the filter
        val countries = listOf(
            Country(name = Name("Country1"), region = null),
            Country(name = Name("Germany"), region = "Europe")
        )
        val filter = FilterByContinent("Europe")
        val result = filter.filter(countries)
        
        assertEquals(1, result.size)
        assertEquals("Europe", result.first().region)
    }

    @Test
    fun filterShouldHandleBlankContinentKey() = runBlocking {
        // Blank continent key (spaces only) should be treated as a search term and return no matches
        val countries = listOf(
            Country(name = Name("Germany"), region = "Europe"),
            Country(name = Name("Brazil"), region = "South America")
        )
        val filter = FilterByContinent("   ") // blank spaces
        val result = filter.filter(countries)
        
        // Since "   " is not null or empty, it will be used as search term and won't match any region
        assert(result.isEmpty())
    }
}