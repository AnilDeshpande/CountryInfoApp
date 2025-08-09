package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.data.*

/**
 * Test data factory for creating test objects with minimal required data
 */
object TestDataFactory {

    fun createCountry(
        id: Int? = null,
        name: String = "Test Country",
        region: String? = "Test Region",
        capital: List<String>? = listOf("Test Capital"),
        population: Int? = 1000000,
        area: Double? = 100000.0,
        languages: Languages? = createLanguages(),
        car: Car? = createCar(),
        flags: Flags? = createFlags()
    ) = Country(
        id = id,
        name = createName(name),
        region = region,
        capital = capital,
        population = population,
        area = area,
        languages = languages,
        car = car,
        flags = flags
    )

    fun createName(
        common: String = "Test Country",
        official: String = "Official Test Country"
    ) = Name(
        common = common,
        official = official
    )

    fun createLanguages(
        languages: Map<String, String> = mapOf("en" to "English")
    ) = Languages(
        languages = languages
    )

    fun createCar(
        side: String = "right"
    ) = Car(
        side = side
    )

    fun createFlags(
        png: String = "https://test.com/flag.png",
        svg: String = "https://test.com/flag.svg"
    ) = Flags(
        png = png,
        svg = svg
    )

    fun createCapitalInfo(
        latlng: List<Double> = listOf(0.0, 0.0)
    ) = CapitalInfo(
        latlng = latlng
    )

    fun createCurrency(
        name: String = "Test Dollar",
        symbol: String = "$"
    ) = Currency(
        name = name,
        symbol = symbol
    )

    fun createIdd(
        root: String = "+1",
        suffixes: List<String> = listOf("234")
    ) = Idd(
        root = root,
        suffixes = suffixes
    )

    fun createMaps(
        googleMaps: String = "https://maps.google.com/test",
        openStreetMaps: String = "https://openstreetmap.org/test"
    ) = Maps(
        googleMaps = googleMaps,
        openStreetMaps = openStreetMaps
    )

    /**
     * Creates a list of test countries for testing scenarios
     */
    fun createCountryList(count: Int = 3): List<Country> {
        return (1..count).map { index ->
            createCountry(
                id = index,
                name = "Country $index",
                region = if (index % 2 == 0) "Europe" else "Asia",
                car = createCar(if (index % 2 == 0) "left" else "right")
            )
        }
    }

    /**
     * Creates countries with specific attributes for filtering tests
     */
    fun createEuropeanCountries(): List<Country> {
        return listOf(
            createCountry(id = 1, name = "Germany", region = "Europe"),
            createCountry(id = 2, name = "France", region = "Europe"),
            createCountry(id = 3, name = "Italy", region = "Europe")
        )
    }

    fun createAsianCountries(): List<Country> {
        return listOf(
            createCountry(id = 4, name = "Japan", region = "Asia"),
            createCountry(id = 5, name = "China", region = "Asia"),
            createCountry(id = 6, name = "India", region = "Asia")
        )
    }

    fun createLeftDriveCountries(): List<Country> {
        return listOf(
            createCountry(id = 7, name = "UK", car = createCar("left")),
            createCountry(id = 8, name = "Australia", car = createCar("left"))
        )
    }

    fun createEnglishSpeakingCountries(): List<Country> {
        return listOf(
            createCountry(
                id = 9, 
                name = "USA", 
                languages = createLanguages(mapOf("en" to "English"))
            ),
            createCountry(
                id = 10, 
                name = "Canada", 
                languages = createLanguages(mapOf("en" to "English", "fr" to "French"))
            )
        )
    }
}