package com.codetutor.countryinfoapp.viewmodel

import CountryEntity
import android.app.Application
import android.content.Context
import android.util.Log
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
            Log.i("Room", "CountryViewModel init before fetched: ${allCountries.value}")
            fetchAndInsertAll()
            getAllCountries()
            Log.i("Room", "CountryViewModel init after fetched: ${allCountries.value}")
        }
    }

    private suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
        Log.i("Room", "getAllCountries init after fetched: ${allCountries.value}")
    }

    private suspend fun fetchAndInsertAll() = viewModelScope.launch {
        repository.fetchAndInsertAll()

    }
}