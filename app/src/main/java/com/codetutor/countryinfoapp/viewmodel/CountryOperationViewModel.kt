package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.FilterCriteria
import com.codetutor.countryinfoapp.repository.ICountryRepository
import kotlinx.coroutines.launch

class CountryOperationViewModel (private val countryRepository: ICountryRepository): ViewModel(), ICountryOperationViewModel {

    override val allCountries: MutableState<List<Country>> = mutableStateOf(emptyList())


    init {
        viewModelScope.launch {
            fetchAndInsertAll()
        }
    }

    private suspend fun fetchAndInsertAll(){
        val job = viewModelScope.launch {
            countryRepository.fetchAndInsertAll()
        }
        job.join()
        getAllCountries()
    }

    override suspend fun getAllCountries(){
        allCountries.value = countryRepository.getAllCountries()
    }

    override suspend fun deleteCountry(country: Country){
        country.let {
            countryRepository.deleteCountry(it)
        }
        getAllCountries()

    }

    override suspend fun updateCountry(country: Country, newCapital: String) {
        country.let {
            it.let {
                countryRepository.updateCapital(it,newCapital)
            }
        }
        getAllCountries()
    }

    override suspend fun filterCountries(filterCriteria: FilterCriteria) {
        filterCriteria?.let { criteria ->
            allCountries.value = countryRepository.filterCountries(criteria)
        }
    }
}