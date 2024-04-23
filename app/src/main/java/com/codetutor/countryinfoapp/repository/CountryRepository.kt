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

    suspend fun fetchAndInsertAll(context: Context) = withContext(Dispatchers.IO) {
        if(getAllCountries() != null) {
            return@withContext

        } else {
            val mutableCountryList: MutableList<Country> = getCountryList(context)
            val countryList: List<Country> = mutableCountryList.toList()
            val countryEntityList: List<CountryEntity> = countryList.map { countryToEntity(it) }
            countryDao.insertAll(countryEntityList)
            return@withContext
        }
    }

    suspend fun getAllCountries(): List<CountryEntity> = withContext(Dispatchers.IO) {
        countryDao.getAllCountries()
    }
}