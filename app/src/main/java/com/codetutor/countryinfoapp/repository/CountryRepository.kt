package com.codetutor.countryinfoapp.repository

import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.dao.ICountryDao
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CountryRepository(
    private val countryDao: ICountryDao,
    private val countryListServiceProvider: CountryListServiceProvider,
    private val dispatcher: CoroutineDispatcher,
) : ICountryRepository {
    private var allCountries: List<Country> = emptyList()

    override suspend fun fetchAndInsertAll() =
        withContext(dispatcher) {
            if (getAllCountries().isNotEmpty()) {
                return@withContext
            } else {
                val mutableCountryList: MutableList<Country> = countryListServiceProvider.getCountryList()
                val countryList: List<Country> = mutableCountryList.toList()
                countryDao.insertAll(countryList)
                return@withContext
            }
        }

    override suspend fun getAllCountries(): List<Country> =
        withContext(dispatcher) {
            if (allCountries.isNotEmpty()) {
                return@withContext allCountries
            } else {
                allCountries = countryDao.getAllCountries()
                return@withContext allCountries
            }
        }

    override suspend fun deleteCountry(country: Country) =
        withContext(dispatcher) {
            countryDao.delete(country)
            allCountries = countryDao.getAllCountries()
        }

    override suspend fun updateCapital(
        country: Country,
        newCapital: String,
    ) = withContext(dispatcher) {
        val countryWithNewCapital = country?.copy(capital = listOf(newCapital))
        countryDao.updateCountry(countryWithNewCapital!!)
        allCountries = countryDao.getAllCountries()
    }

    override suspend fun filterCountries(filterCriteria: FilterCriteria): List<Country> =
        withContext(dispatcher) {
            return@withContext filterCriteria.filter(allCountries)
        }
}
