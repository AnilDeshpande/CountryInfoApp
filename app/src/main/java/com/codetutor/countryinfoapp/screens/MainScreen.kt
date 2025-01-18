package com.codetutor.countryinfoapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.codetutor.countryinfoapp.components.CountryCard
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codetutor.countryinfoapp.components.ObserveIsLoadingChanges
import com.codetutor.countryinfoapp.dialogs.DialogDeleteCountry
import com.codetutor.countryinfoapp.dialogs.DialogUpdateCountry
import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByLanguage
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProviderImpl
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MainScreen( innerPaddingValues: PaddingValues, viewModelCountryOps: CountryOperationViewModel, viewModelUI: CountryUIViewModel ) {

    val countryList = viewModelCountryOps.allCountries.value
    val isLoading = remember {
        mutableStateOf(value = true)
    }

    val showDeleteAlertDialog = viewModelUI.showDeleteAlertDialog
    val showUpdateCapitalDialog = viewModelUI.showUpdateCapitalDialog
    val selectedCountry = viewModelUI.selectedCountryForDeletion
    val updateCountryInfo = viewModelUI.updateCountryInfo.value

    ObserveIsLoadingChanges(isLoading = isLoading, operationViewModel = viewModelCountryOps)

    CountryInfoAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPaddingValues),
            color = MaterialTheme.colorScheme.surface
        ) {
            when {
                isLoading.value -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else -> {
                    LazyColumn {
                        items(items = countryList, key = { country -> country.id ?: 0 }) { country ->
                            CountryCard(
                                countryInfo = country,
                                showDeleteAlertDialog = showDeleteAlertDialog,
                                selectedCountry = selectedCountry,
                                viewModel = viewModelUI
                            )
                        }
                    }
                }
            }
        }
    }

    DialogDeleteCountry(showDialog = showDeleteAlertDialog,
        title = "Delete confirmation",
        message = "Do you want to delete this country?",
        positiveAction = {
            viewModelCountryOps.viewModelScope.launch {
                selectedCountry.let {
                    viewModelCountryOps.deleteCountry(it.value!!)
                    selectedCountry.value = null
                }
            }
        }
    )

    DialogUpdateCountry(showDialog = showUpdateCapitalDialog,
        title = "Update Capital",
        message = "Enter new capital",
        currentCapital = updateCountryInfo?.capital?.get(0) ?: "",
        positiveAction = {  newCapital ->
            viewModelCountryOps.viewModelScope.launch {
                updateCountryInfo?.let {
                    viewModelCountryOps.updateCapital( it ,newCapital)
                }

            }
        }
    )
}
