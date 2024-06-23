package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByLanguage
import com.codetutor.countryinfoapp.repository.FilterCriteria
import com.codetutor.countryinfoapp.repository.ICountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class CountryOperationViewModel @Inject constructor (private val repository: ICountryRepository) : ViewModel() {

    val allCountries: MutableState<List<Country>> = mutableStateOf(emptyList())
    val countriesLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            fetchAndInsertAll()
        }
    }

    suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
        countriesLoaded.value = true
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

    suspend fun filterCountries(filterCriteria: FilterCriteria) {
        filterCriteria?.let { criteria ->
            allCountries.value = repository.filterCountries(criteria)
        }
    }
}