package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Car
import com.codetutor.countryinfoapp.data.Name
import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterByDriveSideTest {
    @Test
    fun filterShouldReturnCountriesMatchingDriveSide() = runBlocking {
        // Countries with matching drive side "left"
        val countries = listOf(
            Country(name = Name("Country1"), car = Car(side = "left")),
            Country(name = Name("Country2"), car = Car(side = "right")),
            Country(name = Name("Country3"), car = Car(side = "left"))
        )
        val filter = FilterByDriveSide("left")
        val result = filter.filter(countries)
        assertEquals(2, result.size)
        result.forEach { country -> assert(country.car?.side == "left") }
    }

    @Test
    fun filterShouldReturnEmptyWhenNoCountryMatches() = runBlocking {
        // No country has a car side "right"
        val countries = listOf(
            Country(name = Name("Country1"), car = Car(side = "left")),
            Country(name = Name("Country2"), car = Car(side = "left"))
        )
        val filter = FilterByDriveSide("right")
        val result = filter.filter(countries)
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldReturnEmptyForEmptyCountryList() = runBlocking {
        // Empty country list should return empty list
        val filter = FilterByDriveSide("left")
        val result = filter.filter(emptyList())
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldReturnEmptyWhenDriveSideIsNull() = runBlocking {
        // Null drive side should yield an empty result
        val countries = listOf(
            Country(name = Name("Country1"), car = Car(side = "left")),
            Country(name = Name("Country2"), car = Car(side = "right"))
        )
        val filter = FilterByDriveSide(null)
        val result = filter.filter(countries)
        assert(result.isEmpty())
    }

    @Test
    fun filterShouldHandleCountriesWithNullCar() = runBlocking {
        // Countries with null car property should be ignored by the filter
        val countries = listOf(
            Country(name = Name("Country1"), car = null),
            Country(name = Name("Country2"), car = Car(side = "right"))
        )
        val filter = FilterByDriveSide("right")
        val result = filter.filter(countries)
        assertEquals(1, result.size)
        result.forEach { country -> assert(country.car?.side == "right") }
    }
}