package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country

class FilterByContinent(private val continent: String?) : FilterCriteria {
    override suspend fun filter(countries: List<Country>): List<Country> {
        return if (continent.isNullOrEmpty()) {
            countries
        } else {
            countries.filter { it.region?.equals(continent, ignoreCase = true) == true }
        }
    }
}