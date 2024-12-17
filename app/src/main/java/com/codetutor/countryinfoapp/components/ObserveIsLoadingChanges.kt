package com.codetutor.countryinfoapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel

@Composable
fun ObserveIsLoadingChanges(isLoading: MutableState<Boolean>,
                            operationViewModel: CountryOperationViewModel) {
    val allCountries = operationViewModel.allCountries.value

    LaunchedEffect(allCountries) {
        isLoading.value = allCountries.isEmpty()
    }

}