package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.ICountryRepository
import kotlinx.coroutines.launch


class CountryOperationViewModel(private val repository: ICountryRepository) : ViewModel() {

    val allCountries: MutableState<List<Country>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            fetchAndInsertAll()
        }
    }

    private suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
    }

    suspend fun deleteCountry(country: Country) {
        country.let {
            repository.deleteCountry(it)
        }
        getAllCountries()
    }

    private suspend fun fetchAndInsertAll() {
        val job = viewModelScope.launch {
            repository.fetchAndInsertAll()
        }
        job.join()
        getAllCountries()
    }

    suspend fun updateCapital(country: Country, newCapital: String) {
        country.let {
            it.let {
                repository.updateCapital(it, newCapital)
            }
        }
        getAllCountries()
    }
}