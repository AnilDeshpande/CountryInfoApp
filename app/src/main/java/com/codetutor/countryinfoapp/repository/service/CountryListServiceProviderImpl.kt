package com.codetutor.countryinfoapp.repository.service

import android.content.Context
import com.codetutor.countryinfoapp.R
import com.codetutor.countryinfoapp.data.Country
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CountryListServiceProviderImpl @Inject constructor (private val context: Context) : CountryListServiceProvider {

    private fun getJsonString(): String {
        val inputStream = context.resources.openRawResource(R.raw.countries)
        return inputStream.bufferedReader().use { it.readText() }
    }

    override suspend fun getCountryList(): MutableList<Country> {
        val jsonStringFromRaw = getJsonString()
        delay(2000)
        return Json{ignoreUnknownKeys = true}.decodeFromString<MutableList<Country>>(jsonStringFromRaw)
    }
}