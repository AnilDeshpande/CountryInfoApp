package com.codetutor.countryinfoapp.repository.service

import com.codetutor.countryinfoapp.data.Country

interface CountryListServiceProvider {
    suspend fun getCountryList(): MutableList<Country>
}