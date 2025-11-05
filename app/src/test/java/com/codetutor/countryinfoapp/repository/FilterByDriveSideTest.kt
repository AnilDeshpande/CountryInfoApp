package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Car
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Name
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class FilterByDriveSideTest {
    @Test
    fun filterShouldReturnCountriesMatchingDriveSide() =
        runBlocking {
            // Arrange
            val countries =
                listOf(
                    Country(name = Name("Country1"), car = Car(side = "left")),
                    Country(name = Name("Country2"), car = Car(side = "right")),
                    Country(name = Name("Country3"), car = Car(side = "left")),
                )
            // Act
            val filter = FilterByDriveSide("left")
            val leftFilteredCountries = filter.filter(countries)

            // Assert
            Assert.assertEquals(2, leftFilteredCountries.size)
            leftFilteredCountries.forEach {
                    country ->
                assert(country.car?.side == "left")
            }
        }
}
