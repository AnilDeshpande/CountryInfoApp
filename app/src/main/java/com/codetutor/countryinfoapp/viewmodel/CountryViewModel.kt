package com.codetutor.countryinfoapp.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.ICountryRepository
import kotlinx.coroutines.launch


class CountryViewModel(private val repository: ICountryRepository) : ViewModel() {

    val allCountries: MutableState<List<Country>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

    //Delete Related Functionality
    val showDeleteAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    var selectedCountryForDeletion: MutableState<Country?> = mutableStateOf(null)

    //Update related functionality
    val showUpdateCapitalDialog: MutableState<Boolean> = mutableStateOf(false)
    var updateCountryInfo: MutableState<Country?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            fetchAndInsertAll()
        }
    }

    private suspend fun getAllCountries() {
        allCountries.value = repository.getAllCountries()
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