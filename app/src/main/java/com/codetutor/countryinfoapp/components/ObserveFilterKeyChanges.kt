package com.codetutor.countryinfoapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel

@Composable
fun ObserveFilterKeyChanges(filterByKey: MutableState<String>,
                            selectedFilter: MutableState<String?>,
                            viewModelCountryOps: CountryOperationViewModel
) {
    val filterKey by filterByKey
    val selectedFilterValue by selectedFilter

    LaunchedEffect(filterKey, selectedFilterValue) {
        if (selectedFilterValue != null) {
            filterBy(filterKey, selectedFilterValue, viewModelCountryOps)
        }
    }
}

suspend fun filterBy(
    filterKey: String,
    selectedFilterValue: String?,
    viewModelCountryOps: CountryOperationViewModel
) {
    if (filterKey.isNotEmpty()) {
        val filterCriteria = determineFilterCriteria(selectedFilterValue!!, filterKey)
        filterCriteria?.let {
            viewModelCountryOps.filterCountries(it)
        }
    } else {
        viewModelCountryOps.getAllCountries()
    }
}

fun determineFilterCriteria(selectedFilterValue: String, filterKey: String) = when(selectedFilterValue) {
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

