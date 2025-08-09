package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Languages
import com.codetutor.countryinfoapp.data.Name
import com.codetutor.countryinfoapp.repository.FilterByLanguage
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterByLanguageTest {

    @Test
    fun filterShouldReturnCountriesMatchingLanguage() = runBlocking {
        // Countries with matching language "English"
        val countries = listOf(
            Country(
                name = Name("United States"), 
                languages = Languages(mapOf("en" to "English"))
            ),
            Country(
                name = Name("Germany"), 
                languages = Languages(mapOf("de" to "German"))
            ),
            Country(
                name = Name("United Kingdom"), 
                languages = Languages(mapOf("en" to "English"))
            ),
            Country(
                name = Name("France"), 
                languages = Languages(mapOf("fr" to "French"))
            )
        )
        val filter = FilterByLanguage("English")
        val result = filter.filter(countries)
        
        assertEquals(2, result.size)
        result.forEach { country -> 
            assert(country.languages?.languages?.containsValue("English") == true)
        }
    }

    @Test
    fun filterShouldReturnEmptyWhenNoCountryMatches() = runBlocking {
        // No country has language "Spanish"
        val countries = listOf(
            Country(
                name = Name("Germany"), 
                languages = Languages(mapOf("de" to "German"))
            ),
            Country(
                name = Name("France"), 
                languages = Languages(mapOf("fr" to "French"))
            )
        )
        val filter = FilterByLanguage("Spanish")
        val result = filter.filter(countries)
        
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldReturnAllCountriesForEmptyInput() = runBlocking {
        // Empty language should return all countries
        val countries = listOf(
            Country(
                name = Name("Germany"), 
                languages = Languages(mapOf("de" to "German"))
            ),
            Country(
                name = Name("France"), 
                languages = Languages(mapOf("fr" to "French"))
            )
        )
        val filter = FilterByLanguage("")
        val result = filter.filter(countries)
        
        assertEquals(countries.size, result.size)
        assertEquals(countries, result)
    }

    @Test
    fun filterShouldReturnAllCountriesForNullInput() = runBlocking {
        // Null language should return all countries
        val countries = listOf(
            Country(
                name = Name("Germany"), 
                languages = Languages(mapOf("de" to "German"))
            ),
            Country(
                name = Name("France"), 
                languages = Languages(mapOf("fr" to "French"))
            )
        )
        val filter = FilterByLanguage(null)
        val result = filter.filter(countries)
        
        assertEquals(countries.size, result.size)
        assertEquals(countries, result)
    }

    @Test
    fun filterShouldReturnEmptyForEmptyCountryList() = runBlocking {
        // Empty country list should return empty list
        val filter = FilterByLanguage("English")
        val result = filter.filter(emptyList())
        
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldHandleCountriesWithNullLanguages() = runBlocking {
        // Countries with null languages should be ignored by the filter
        val countries = listOf(
            Country(name = Name("Country1"), languages = null),
            Country(
                name = Name("United States"), 
                languages = Languages(mapOf("en" to "English"))
            )
        )
        val filter = FilterByLanguage("English")
        val result = filter.filter(countries)
        
        assertEquals(1, result.size)
        assertEquals("United States", result.first().name?.common)
    }

    @Test
    fun filterShouldHandleCountriesWithEmptyLanguagesMap() = runBlocking {
        // Countries with empty languages map should be ignored by the filter
        val countries = listOf(
            Country(
                name = Name("Country1"), 
                languages = Languages(emptyMap())
            ),
            Country(
                name = Name("United States"), 
                languages = Languages(mapOf("en" to "English"))
            )
        )
        val filter = FilterByLanguage("English")
        val result = filter.filter(countries)
        
        assertEquals(1, result.size)
        assertEquals("United States", result.first().name?.common)
    }

    @Test
    fun filterShouldHandleCountriesWithNullLanguagesMapInLanguagesObject() = runBlocking {
        // Countries with Languages object but null languages map should be ignored
        val countries = listOf(
            Country(
                name = Name("Country1"), 
                languages = Languages(null)
            ),
            Country(
                name = Name("United States"), 
                languages = Languages(mapOf("en" to "English"))
            )
        )
        val filter = FilterByLanguage("English")
        val result = filter.filter(countries)
        
        assertEquals(1, result.size)
        assertEquals("United States", result.first().name?.common)
    }

    @Test
    fun filterShouldHandleBlankLanguageKey() = runBlocking {
        // Blank language key (spaces only) should be treated as a search term and return no matches
        val countries = listOf(
            Country(
                name = Name("Germany"), 
                languages = Languages(mapOf("de" to "German"))
            ),
            Country(
                name = Name("France"), 
                languages = Languages(mapOf("fr" to "French"))
            )
        )
        val filter = FilterByLanguage("   ") // blank spaces
        val result = filter.filter(countries)
        
        // Since "   " is not null or empty, it will be used as search term and won't match any language
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldHandleMultipleLanguagesInCountry() = runBlocking {
        // Countries with multiple languages should match if any language matches
        val countries = listOf(
            Country(
                name = Name("Switzerland"), 
                languages = Languages(mapOf(
                    "de" to "German",
                    "fr" to "French",
                    "it" to "Italian"
                ))
            ),
            Country(
                name = Name("Germany"), 
                languages = Languages(mapOf("de" to "German"))
            )
        )
        val filter = FilterByLanguage("French")
        val result = filter.filter(countries)
        
        assertEquals(1, result.size)
        assertEquals("Switzerland", result.first().name?.common)
    }
}