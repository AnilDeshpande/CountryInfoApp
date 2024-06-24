package com.codetutor.countryinfoapp.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.ICountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryUIViewModel @Inject constructor(private val repository: ICountryRepository): ViewModel() {

    private val countryOperationViewModel: CountryOperationViewModel = CountryOperationViewModel(repository) // Injecting the ViewModel - This need to be fixed

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