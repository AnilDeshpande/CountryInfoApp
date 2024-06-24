package com.codetutor.countryinfoapp.repository.service

import android.content.Context
import com.codetutor.countryinfoapp.R
import com.codetutor.countryinfoapp.data.Country
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryListServiceProviderImpl @Inject constructor(context: Context) : CountryListServiceProvider {

    private val contextForProvider: Context = context

    private fun getJsonString(): String {
        val inputStream = contextForProvider.resources.openRawResource(R.raw.countries)
        return inputStream.bufferedReader().use { it.readText() }
    }

    override suspend fun getCountryList(): MutableList<Country> {
        val jsonStringFromRaw = getJsonString()
        delay(2000)
        return Json{ignoreUnknownKeys = true}.decodeFromString<MutableList<Country>>(jsonStringFromRaw)
    }
}