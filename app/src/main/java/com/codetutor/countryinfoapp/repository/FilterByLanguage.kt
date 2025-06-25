package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country

class FilterByLanguage(private val language: String?) : FilterCriteria {
    override suspend fun filter(countries: List<Country>): List<Country> {
        return if (language.isNullOrEmpty()) {
            countries
        } else {
            countries.filter { country ->
                country.languages?.languages?.containsValue(language) == true
            }
        }
    }
}