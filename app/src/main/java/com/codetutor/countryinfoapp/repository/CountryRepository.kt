package com.codetutor.countryinfoapp.repository

import CountryEntity
import android.content.Context
import android.util.Log
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.CountryDao
import com.codetutor.countryinfoapp.database.UpdateCountryInfo
import com.codetutor.countryinfoapp.util.getCountryList
import countryToEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class CountryRepository(private val context: Context, private val countryDao: CountryDao) {

    private val contextForRepo: Context = context

    private var allCountries: List<Country> = emptyList()

    suspend fun fetchAndInsertAll() = withContext(Dispatchers.IO) {
        if(getAllCountries() != null && getAllCountries().isNotEmpty()) {
            Log.i("Room", "Countries already exist in the database ${getAllCountries().size}")
            return@withContext

        } else {
            Log.i("Room", "Fetching and inserting countries")
            val mutableCountryList: MutableList<Country> = getCountryList(contextForRepo)
            Log.i("Room", "Fetched countries: ${mutableCountryList.size}")
            val countryList: List<Country> = mutableCountryList.toList()
            Log.i("Room", "Inserting Country list: ${countryList.size}")
            countryDao.insertAll(countryList)
            return@withContext
        }
    }

    suspend fun getAllCountries(): List<Country> = withContext(Dispatchers.IO) {
        if(allCountries.isNotEmpty()) {
            Log.i("Room", "getAllCountries : ${allCountries.size}")
            return@withContext allCountries
        } else {
            allCountries = countryDao.getAllCountries()
            Log.i("Room", "getAllCountries else : ${allCountries.size}")
            return@withContext allCountries
        }
    }

    suspend fun deleteCountry(country: Country) = withContext(Dispatchers.IO) {
        Log.i("Room", " deleteCountry Countries Size before Deletion: ${allCountries.size}")
        countryDao.delete(country)
        allCountries = countryDao.getAllCountries()
        Log.i("Room", "deleteCountry Countries Size after Deletion: ${allCountries.size}")
    }

    suspend fun updateCapital(updateCountryInfo: UpdateCountryInfo) = withContext(Dispatchers.IO) {
        val parsedString = "[\"${updateCountryInfo.newCapital}\"]"
        val parsedArray = Json.decodeFromString<List<String>>(parsedString)
        Log.i("Room", " updateCapital count: $parsedArray")
        //val count = countryDao.updateCapital(parsedArray, updateCountryInfo.currentCountry?.id!!)
        val country = updateCountryInfo.currentCountry?.copy(capital = parsedArray)
        countryDao.updateCountry(country!!)
        allCountries = countryDao.getAllCountries()

    }
}