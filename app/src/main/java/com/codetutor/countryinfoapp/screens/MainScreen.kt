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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codetutor.countryinfoapp.components.CountryCard
import com.codetutor.countryinfoapp.database.AppDatabase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme
import com.codetutor.countryinfoapp.viewmodel.CountryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewModelScope
import com.codetutor.countryinfoapp.MyAlertDialog
import kotlinx.coroutines.launch


@Composable
fun MainScreen( innerPaddingValues: PaddingValues) {

    val context = LocalContext.current
    val countryDao = AppDatabase.getDatabase(context.applicationContext).countryDao()
    val repository = CountryRepository(context,countryDao)
    val viewModel: CountryViewModel = viewModel(factory = CountryViewModelFactory(repository))

    val countryList = viewModel.allCountries.observeAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.observeAsState(initial = true)

    val showDeleteAlertDialog = viewModel.showDeleteAlertDialog
    val selectedCountry = viewModel.selectedCountryForDeletion

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
                }
                else -> {
                    LazyColumn {
                        items(countryList.value) {
                            CountryCard(countryInfo = it,
                                showDeleteAlertDialog = showDeleteAlertDialog,
                                selectedCountry = selectedCountry)
                        }
                    }
                }
            }
        }
    }

    MyAlertDialog(showDialog = showDeleteAlertDialog,
        title = "Delete confirmation",
        message = "Do you want to delete this country?", positiveAction = {
            viewModel.viewModelScope.launch {
                viewModel.deleteCountry()
                selectedCountry.value = null
            }

        })
}

class CountryViewModelFactory(private val repository: CountryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
