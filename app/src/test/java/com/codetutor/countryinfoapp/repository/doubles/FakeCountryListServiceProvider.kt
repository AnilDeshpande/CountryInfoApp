package com.codetutor.countryinfoapp.repository.doubles

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider

/**
 * A fake implementation of CountryListServiceProvider for testing purposes.
 * It returns a predefined list of countries instead of making actual API calls.
 */
class FakeCountryListServiceProvider(
    private val testCountries: List<Country> = emptyList()
) : CountryListServiceProvider {

    /**
     * Returns a mutable list of predefined test countries.
     * @return MutableList<Country> The list of test countries.
     */
    override suspend fun getCountryList(): MutableList<Country> {
        return testCountries.toMutableList()
    }
}