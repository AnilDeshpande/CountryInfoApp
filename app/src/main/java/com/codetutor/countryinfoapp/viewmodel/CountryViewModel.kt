package com.codetutor.countryinfoapp.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.UpdateCountryInfo
import com.codetutor.countryinfoapp.repository.CountryRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CountryViewModel(private val repository: CountryRepository) : ViewModel() {

    val allCountries: MutableLiveData<List<Country>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val showDeleteAlertDialog: MutableState<Boolean> = mutableStateOf(false)


    //country selected for deletion
    var selectedCountryForDeletion: MutableState<Country?> = mutableStateOf(null)

    var updateCountryInfo: MutableLiveData<UpdateCountryInfo?> = MutableLiveData(null)


    init {
        viewModelScope.launch {
            Log.i("CountryViewModel", "CountryViewModel init before fetched: ${allCountries.value}")
            fetchAndInsertAll()
            Log.i("CountryViewModel", "CountryViewModel init after fetched: ${allCountries.value}")
        }
    }

    private suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
        Log.i("CountryViewModel", "getAllCountries init after fetched: ${allCountries.value}")
    }

    suspend fun deleteCountry() {
        selectedCountryForDeletion.value?.let {
            repository.deleteCountry(it)
        }
        getAllCountries()
        selectedCountryForDeletion.value = null
    }

    private suspend fun fetchAndInsertAll() {
        val job = viewModelScope.launch {
        repository.fetchAndInsertAll()
        }
        job.join()
        getAllCountries()
        isLoading.value = false
    }

    suspend fun updateCapital() {
        Log.i("CountryViewModel", "updateCapital: ${updateCountryInfo.value}")
        updateCountryInfo.value?.let {
            repository.updateCapital(updateCountryInfo.value!!)
        }
        getAllCountries()
        updateCountryInfo.value = null
    }
}