package com.codetutor.countryinfoapp.viewmodel

import CountryEntity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.repository.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: CountryRepository) : AndroidViewModel(application = Application()) {

    val allCountries: MutableLiveData<List<CountryEntity>> = MutableLiveData()

    init {
        viewModelScope.launch {
            fetchAndInsertAll(getApplication<Application>().applicationContext)
            getAllCountries()
        }
    }
    private suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
    }

    private suspend fun fetchAndInsertAll(context: Context) = viewModelScope.launch {
        repository.fetchAndInsertAll(context)
    }
}