package com.codetutor.countryinfoapp.repository

import CountryEntity
import android.content.Context
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.CountryDao
import com.codetutor.countryinfoapp.util.getCountryList
import countryToEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository(private val context: Context, private val countryDao: CountryDao) {

    private val contextForRepo: Context = context

    suspend fun fetchAndInsertAll() = withContext(Dispatchers.IO) {
        if(getAllCountries() != null) {
            return@withContext

        } else {
            val mutableCountryList: MutableList<Country> = getCountryList(contextForRepo)
            val countryList: List<Country> = mutableCountryList.toList()
            countryDao.insertAll(countryList)
            return@withContext
        }
    }

    suspend fun getAllCountries(): List<Country> = withContext(Dispatchers.IO) {
        countryDao.getAllCountries()
    }
}