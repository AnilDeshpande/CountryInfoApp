package com.codetutor.countryinfoapp.database.dao

import com.codetutor.countryinfoapp.data.Country

interface ICountryDao {
    suspend fun getAllCountries(): List<Country>
    suspend fun getCountriesByContinent(continent: String): List<Country>
    suspend fun insertAll(countries: List<Country>)
    suspend fun delete(country: Country)
    suspend fun updateCapital(capital: List<String>, id: Int): Int
    suspend fun updateCountry(country: Country): Int
}