package com.codetutor.countryinfoapp.repository

import android.content.Context
import android.util.Log
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.dao.CountryDao
import com.codetutor.countryinfoapp.util.getCountryList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class CountryRepository(private val context: Context, private val countryDao: CountryDao) {

    private val contextForRepo: Context = context

    private var allCountries: List<Country> = emptyList()

    suspend fun fetchAndInsertAll() = withContext(Dispatchers.IO) {
        if(getAllCountries() != null && getAllCountries().isNotEmpty()) {
            return@withContext

        } else {
            val mutableCountryList: MutableList<Country> = getCountryList(contextForRepo)
            val countryList: List<Country> = mutableCountryList.toList()
            countryDao.insertAll(countryList)
            return@withContext
        }
    }

    suspend fun getAllCountries(): List<Country> = withContext(Dispatchers.IO) {
        if(allCountries.isNotEmpty()) {
            return@withContext allCountries
        } else {
            allCountries = countryDao.getAllCountries()
            return@withContext allCountries
        }
    }

    suspend fun deleteCountry(country: Country) = withContext(Dispatchers.IO) {
        countryDao.delete(country)
        allCountries = countryDao.getAllCountries()
    }

    suspend fun updateCapital(country: Country, newCapital: String) = withContext(Dispatchers.IO) {
        val parsedString = "[\"${newCapital}\"]"
        val parsedArray = Json.decodeFromString<List<String>>(parsedString)
        //val count = countryDao.updateCapital(parsedArray, country?.id!!)
        val country = country?.copy(capital = parsedArray)
        countryDao.updateCountry(country!!)
        allCountries = countryDao.getAllCountries()
    }
}