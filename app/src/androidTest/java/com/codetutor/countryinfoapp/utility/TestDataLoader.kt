package com.codetutor.countryinfoapp.utility

import com.codetutor.countryinfoapp.data.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object TestDataLoader {
    fun loadCountries(): List<Country> {
        val inputStream = TestDataLoader::class.java.classLoader
            ?.getResourceAsStream("assets/test_data_countries.json")
            ?: throw IllegalStateException("Cannot find test_data_countries.json")

        return InputStreamReader(inputStream).use { reader ->
            val listType = object : TypeToken<List<Country>>() {}.type
            Gson().fromJson(reader, listType)
        }
    }
}