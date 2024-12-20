package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.FilterCriteria
import com.codetutor.countryinfoapp.repository.ICountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryOperationViewModel @Inject constructor(private val repository: ICountryRepository) : ViewModel(), ICountryOperationViewModel {

    override val allCountries: MutableState<List<Country>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            fetchAndInsertAll()
        }
    }

    override suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
    }

    override suspend fun deleteCountry(country: Country) {
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

    override suspend fun updateCapital(country: Country, newCapital: String) {
        country.let {
            it.let {
                repository.updateCapital(it, newCapital)
            }
        }
        getAllCountries()
    }

    override suspend fun filterCountries(filterCriteria: FilterCriteria) {
        filterCriteria?.let { criteria ->
            allCountries.value = repository.filterCountries(criteria)
        }
    }
}