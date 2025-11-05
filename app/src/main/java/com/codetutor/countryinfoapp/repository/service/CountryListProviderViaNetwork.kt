package com.codetutor.countryinfoapp.repository.service

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.service.network.ApiService
import com.codetutor.countryinfoapp.repository.service.network.RetrofitInstance

class CountryListProviderViaNetwork() : CountryListServiceProvider {
    private val apiService: ApiService by lazy {
        RetrofitInstance.retrofit.create(ApiService::class.java)
    }

    override suspend fun getCountryList(): MutableList<Country> {
        return apiService.getAllCountries().body()!!
    }
}
