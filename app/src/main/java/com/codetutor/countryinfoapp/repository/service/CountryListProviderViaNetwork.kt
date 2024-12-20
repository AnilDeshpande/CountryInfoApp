package com.codetutor.countryinfoapp.repository.service

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.service.network.ApiService
import javax.inject.Inject

class CountryListProviderViaNetwork @Inject constructor(
    private val apiService: ApiService) : CountryListServiceProvider {

    override suspend fun getCountryList(): MutableList<Country> {
        return apiService.getAllCountries().body()!!
    }
}