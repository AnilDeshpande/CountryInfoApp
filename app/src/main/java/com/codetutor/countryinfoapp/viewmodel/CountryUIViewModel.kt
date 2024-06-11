package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import kotlinx.coroutines.launch

class CountryUIViewModel(private val countryOperationViewModel: CountryOperationViewModel): ViewModel() {
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

    //Delete Related Functionality
    val showDeleteAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    var selectedCountryForDeletion: MutableState<Country?> = mutableStateOf(null)


    var selectedFilter: MutableState<String?> =  mutableStateOf(null)
    var filterByKey : MutableState<String> =  mutableStateOf("")

    //Update related functionality
    val showUpdateCapitalDialog: MutableState<Boolean> = mutableStateOf(false)
    var updateCountryInfo: MutableState<Country?> = mutableStateOf(null)

    var selectedCountry: MutableState<Country?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            countryOperationViewModel.countriesLoaded.collect { loaded ->
                isLoading.value = !loaded
            }
        }
    }

}