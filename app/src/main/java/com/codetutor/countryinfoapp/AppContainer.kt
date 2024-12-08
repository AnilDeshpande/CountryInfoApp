package com.codetutor.countryinfoapp

import android.content.Context
import com.codetutor.countryinfoapp.database.appdb.AppDataBase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.service.CountryListProviderViaNetwork
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.viewmodel.CountryViewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppContainer(context: Context, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val countryDao = AppDataBase.getDataBase(context.applicationContext)?.countryDao()
    private val countryListProvider: CountryListServiceProvider = CountryListProviderViaNetwork()
    val repository = CountryRepository(countryListProvider, countryDao!!, dispatcher)
    val viewModelFactory = CountryViewModelFactory(repository)
}