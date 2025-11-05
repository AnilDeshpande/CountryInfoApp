package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.FilterCriteria

interface ICountryOperationViewModel {
    val allCountries: MutableState<List<Country>>

    suspend fun getAllCountries()

    suspend fun deleteCountry(country: Country)

    suspend fun updateCapital(
        country: Country,
        newCapital: String,
    )

    suspend fun filterCountries(filterCriteria: FilterCriteria)
}
