package com.codetutor.countryinfoapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel

@Composable
fun ObserveIsLoadingChanges(isLoading: MutableState<Boolean>, uiViewModel: CountryUIViewModel, operationViewModel: CountryOperationViewModel) {

    LaunchedEffect(isLoading) {
        isLoading.value = operationViewModel.allCountries.value.isEmpty()
    }

    SideEffect {
        isLoading.value = operationViewModel.allCountries.value.isEmpty()
    }

}