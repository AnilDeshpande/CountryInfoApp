package com.codetutor.countryinfoapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import com.codetutor.countryinfoapp.util.FilterCriteriaFactoryProvider
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel

@Composable
fun ObserveFilterKeyChanges(
    filterByKey: MutableState<String>,
    selectedFilter: MutableState<String?>,
    viewModelCountryOps: CountryOperationViewModel,
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
    viewModelCountryOps: CountryOperationViewModel,
) {
    if (filterKey.isNotEmpty()) {
        val factory = FilterCriteriaFactoryProvider.getFactory(selectedFilterValue!!)
        val filterCriteria = factory?.createFilterCriteria(filterKey)
        filterCriteria?.let {
            viewModelCountryOps.filterCountries(it)
        }
    } else {
        viewModelCountryOps.getAllCountries()
    }
}
