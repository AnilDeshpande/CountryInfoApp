package com.codetutor.countryinfoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryUIViewModel @Inject constructor(): ViewModel() {
    //Delete Related Functionality
    val showDeleteAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    var selectedCountryForDeletion: MutableState<Country?> = mutableStateOf(null)

    // Filter functionality
    var selectedFilter: MutableState<String?> =  mutableStateOf(null)
    var filterByKey : MutableState<String> =  mutableStateOf("")

    //Update related functionality
    val showUpdateCapitalDialog: MutableState<Boolean> = mutableStateOf(false)
    var updateCountryInfo: MutableState<Country?> = mutableStateOf(null)
}