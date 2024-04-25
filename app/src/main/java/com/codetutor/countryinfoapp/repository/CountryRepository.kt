package com.codetutor.countryinfoapp.repository

import CountryEntity
import android.content.Context
import android.util.Log
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.CountryDao
import com.codetutor.countryinfoapp.util.getCountryList
import countryToEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository(private val context: Context, private val countryDao: CountryDao) {

    private val contextForRepo: Context = context

    suspend fun fetchAndInsertAll() = withContext(Dispatchers.IO) {
        if(getAllCountries() != null && getAllCountries().isNotEmpty()) {
            Log.i("Room", "Countries already exist in the database ${getAllCountries()}")
            return@withContext

        } else {
            Log.i("Room", "Fetching and inserting countries")
            val mutableCountryList: MutableList<Country> = getCountryList(contextForRepo)
            Log.i("Room", "Fetched countries: $mutableCountryList")
            val countryList: List<Country> = mutableCountryList.toList()
            Log.i("Room", "Inserting Country list: $countryList")
            countryDao.insertAll(countryList)
            return@withContext
        }
    }

    suspend fun getAllCountries(): List<Country> = withContext(Dispatchers.IO) {
        countryDao.getAllCountries()
    }
}