package com.codetutor.countryinfoapp.viewmodel

import CountryEntity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: CountryRepository) : ViewModel() {

    val allCountries: MutableLiveData<List<Country>> = MutableLiveData()

    init {
        viewModelScope.launch {
            fetchAndInsertAll()
            getAllCountries()
        }
    }

    private suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
    }

    private suspend fun fetchAndInsertAll() = viewModelScope.launch {
        repository.fetchAndInsertAll()
    }
}