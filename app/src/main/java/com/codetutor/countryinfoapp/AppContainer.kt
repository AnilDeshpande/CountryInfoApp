package com.codetutor.countryinfoapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.service.CountryListProviderViaNetwork
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryViewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppContainer(context: Context, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val countryDao = AppDatabase.getDatabase(context.applicationContext).countryDao()
    private val countryListProvider: CountryListServiceProvider = CountryListProviderViaNetwork()
    val repository = CountryRepository(countryDao,countryListProvider, dispatcher)
    val viewModelFactory = CountryViewModelFactory(repository)
}



