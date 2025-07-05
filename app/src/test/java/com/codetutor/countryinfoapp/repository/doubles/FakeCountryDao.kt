package com.codetutor.countryinfoapp.repository.doubles

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.dao.ICountryDao

class FakeCountryDao : ICountryDao {
    private val countryList = mutableListOf<Country>()

    override suspend fun insertAll(countries: List<Country>) {
        countryList.clear()
        countryList.addAll(countries)
    }

    override suspend fun getAllCountries(): List<Country> = countryList.toList()

    override suspend fun delete(country: Country) {
        countryList.remove(country)
    }

    override suspend fun updateCountry(country: Country): Int {
        val index = countryList.indexOfFirst { it.name == country.name }
        return if (index != -1) {
            countryList[index] = country
            1
        } else {
            0
        }
    }

    override suspend fun getCountriesByContinent(continent: String): List<Country> {
        return countryList.filter { it.continents?.contains(continent) == true}
    }

    override suspend fun updateCapital(capital: List<String>, id: Int): Int {
        val index = countryList.indexOfFirst { it.id == id }
        return if (index != -1) {
            val oldCountry = countryList[index]
            countryList[index] = oldCountry.copy(capital = capital)
            1
        } else {
            0
        }
    }
}