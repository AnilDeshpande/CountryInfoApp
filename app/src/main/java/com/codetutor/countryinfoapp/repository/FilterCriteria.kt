package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country

interface FilterCriteria {
    suspend fun filter(countries: List<Country>): List<Country>
}