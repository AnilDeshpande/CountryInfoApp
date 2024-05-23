package com.codetutor.countryinfoapp.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.UpdateCountryInfo
import com.codetutor.countryinfoapp.repository.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: CountryRepository) : ViewModel() {

    val allCountries: MutableState<List<Country>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val showDeleteAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val showUpdateCapitalDialog: MutableState<Boolean> = mutableStateOf(false)

    //country selected for deletion
    var selectedCountryForDeletion: MutableState<Country?> = mutableStateOf(null)

    var updateCountryInfo: MutableState<Country?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            Log.i("CountryViewModel", "CountryViewModel init before fetched: ${allCountries.value.size}")
            fetchAndInsertAll()
            Log.i("CountryViewModel", "CountryViewModel init after fetched: ${allCountries.value.size}")
        }
    }

    private suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
        Log.i("CountryViewModel", "getAllCountries init after fetched: ${allCountries.value.size}")
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

    suspend fun updateCapital(newCapital: String) {
        Log.i("CountryViewModel", "updateCapital: ${updateCountryInfo.value}")
        updateCountryInfo.value?.let {
            it?.let {
                repository.updateCapital(it, newCapital)
            }

        }
        getAllCountries()
        updateCountryInfo.value = null
        showUpdateCapitalDialog.value = false
    }
}