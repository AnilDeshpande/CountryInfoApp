package com.codetutor.countryinfoapp.repository

import CountryEntity
import com.codetutor.countryinfoapp.database.CountryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository(private val countryDao: CountryDao) {

    suspend fun insertAll(countries: List<CountryEntity>) = withContext(Dispatchers.IO) {
        countryDao.insertAll(countries)
    }

    suspend fun getAllCountries(): List<CountryEntity> = withContext(Dispatchers.IO) {
        countryDao.getAllCountries()
    }
}