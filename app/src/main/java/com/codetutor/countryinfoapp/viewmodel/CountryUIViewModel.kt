package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.codetutor.countryinfoapp.data.Country

class CountryUIViewModel: ViewModel() {
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

    //Delete Related Functionality
    val showDeleteAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    var selectedCountryForDeletion: MutableState<Country?> = mutableStateOf(null)

    //Update related functionality
    val showUpdateCapitalDialog: MutableState<Boolean> = mutableStateOf(false)
    var updateCountryInfo: MutableState<Country?> = mutableStateOf(null)


}