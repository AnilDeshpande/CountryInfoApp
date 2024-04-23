package com.codetutor.countryinfoapp.viewmodel

import CountryEntity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.repository.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: CountryRepository) : ViewModel() {

    val allCountries: MutableLiveData<List<CountryEntity>> = MutableLiveData()

    init {
        viewModelScope.launch {
            getAllCountries()
        }
    }
    suspend fun getAllCountries() {
        repository.getAllCountries()
    }

    suspend fun insertAll(countries: List<CountryEntity>) = viewModelScope.launch {
        repository.insertAll(countries)
    }
}