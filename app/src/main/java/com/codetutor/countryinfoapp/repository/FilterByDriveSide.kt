package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country

class FilterByDriveSide(private val driveSide: String?) : FilterCriteria {
    override suspend fun filter(countries: List<Country>): List<Country> {
        return if (driveSide.isNullOrEmpty()) {
            emptyList()
        } else {
            countries.filter { it.car?.side?.equals(driveSide, ignoreCase = true) == true }
        }
    }
}