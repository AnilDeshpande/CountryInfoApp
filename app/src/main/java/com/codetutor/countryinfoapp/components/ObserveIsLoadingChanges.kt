package com.codetutor.countryinfoapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel

@Composable
fun ObserveIsLoadingChanges(isLoading: MutableState<Boolean>,
                            operationViewModel: CountryOperationViewModel) {

    SideEffect {
        isLoading.value = operationViewModel.allCountries.value.isEmpty()
    }

}