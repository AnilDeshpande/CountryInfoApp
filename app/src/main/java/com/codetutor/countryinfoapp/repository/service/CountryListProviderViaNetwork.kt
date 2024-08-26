package com.codetutor.countryinfoapp.repository.service

import android.content.Context
import androidx.compose.foundation.text2.input.rememberTextFieldState
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.service.network.ApiService

class CountryListProviderViaNetwork(private val apiService: ApiService) : CountryListServiceProvider {

    override suspend fun getCountryList(): MutableList<Country> {
        val response = apiService.getAllCountries()
        return if (response.isSuccessful) {
            response.body() ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }
}