package com.codetutor.countryinfoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codetutor.countryinfoapp.repository.CountryRepository

class CountryViewModelFactory(private val countryRepository: CountryRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryOperationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountryOperationViewModel(countryRepository) as T
        }
        if (modelClass.isAssignableFrom(CountryUIViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountryUIViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}