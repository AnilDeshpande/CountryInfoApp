package com.codetutor.countryinfoapp.repository

import android.content.Context
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.dao.CountryDao
import com.codetutor.countryinfoapp.database.dao.ICountryDao
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.util.getCountryList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class CountryRepository(private val countryListServiceProvider: CountryListServiceProvider,
                        private val countryDao: ICountryDao,
                        private val dispatcher: CoroutineDispatcher
): ICountryRepository {
    private var allCountries: List<Country> = emptyList()

    //fetchAndInsertAll
    override suspend fun fetchAndInsertAll() = withContext(dispatcher){
        val allCountries = getAllCountries()
        if(allCountries.isNotEmpty()){
            return@withContext
        }else {
            val countryList: List<Country> = countryListServiceProvider.getCountryList()
            countryDao.insertAll(countryList)
            return@withContext
        }
    }

    override suspend fun getAllCountries(): List<Country> = withContext(dispatcher){
        if(allCountries.isNotEmpty()){
            return@withContext allCountries
        }else {
            allCountries = countryDao.getAllCountries()
            return@withContext allCountries
        }
    }

    override suspend fun deleteCountry(country: Country) = withContext(dispatcher){
        countryDao.delete(country)
        allCountries = countryDao.getAllCountries()
    }

    override suspend fun updateCapital(country: Country, newCapital: String) = withContext(dispatcher){
        val parsedString = "[\"${newCapital}\"]"
        val parsedArray = Json.decodeFromString<List<String>>(parsedString)
        val updatedCountry = country?.copy(capital = parsedArray)
        updatedCountry.let {
            countryDao.updateCountry(it!!)
            allCountries = countryDao.getAllCountries()
        }
    }

    override suspend fun filterCountries(filterCriteria: FilterCriteria): List<Country> = withContext(dispatcher) {
        return@withContext filterCriteria.filter(allCountries)
    }
}