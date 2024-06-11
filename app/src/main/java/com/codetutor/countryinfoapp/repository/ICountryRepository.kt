package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country

interface ICountryRepository {
    suspend fun fetchAndInsertAll()
    suspend fun getAllCountries(): List<Country>
    suspend fun deleteCountry(country: Country)
    suspend fun updateCapital(country: Country, newCapital: String)
    suspend fun filterCountries(filterCriteria: FilterCriteria): List<Country>
}