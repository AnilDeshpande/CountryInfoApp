package com.codetutor.countryinfoapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel

@Composable
fun ObserveFilterKeyChanges(filterByKey: MutableState<String>,
                            selectedFilter: MutableState<String?>,
                            viewModelCountryOps: CountryOperationViewModel
) {
    val filterKey by filterByKey
    val selectedFilterValue by selectedFilter

    LaunchedEffect(filterKey, selectedFilterValue) {
        if (selectedFilterValue != null) {
            if(filterKey.isNotEmpty()){
                val filterCriteria = when(selectedFilterValue) {
                    "Continent" -> {
                        FilterByContinent(filterKey)
                    }
                    "Drive Side" -> {
                        FilterByDriveSide(filterKey)
                    }
                    else -> {
                        null
                    }
                }
                filterCriteria?.let {
                    viewModelCountryOps.filterCountries(it)
                }
            } else {
                viewModelCountryOps.getAllCountries()
            }
        }
    }
}