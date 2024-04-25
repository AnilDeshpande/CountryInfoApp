package com.codetutor.countryinfoapp.screens

import CountryEntity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codetutor.countryinfoapp.components.CountryCard
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.AppDatabase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme
import com.codetutor.countryinfoapp.util.getCountryList
import com.codetutor.countryinfoapp.viewmodel.CountryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState


@Composable
fun MainScreen( innerPaddingValues: PaddingValues) {

    val context = LocalContext.current
    val countryDao = AppDatabase.getDatabase(context.applicationContext).countryDao()
    val repository = CountryRepository(context,countryDao)

    val viewModel: CountryViewModel = viewModel(factory = CountryViewModelFactory(repository))
    val countryList = viewModel.allCountries.observeAsState(initial = emptyList())

    CountryInfoAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPaddingValues),
            color = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn {
                items(countryList.value) {
                    CountryCard(countryInfo = it)
                }
            }
        }
    }
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
