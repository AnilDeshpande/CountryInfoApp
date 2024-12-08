package com.codetutor.countryinfoapp

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.codetutor.countryinfoapp.database.appdb.AppDataBase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.service.CountryListProviderViaNetwork
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.viewmodel.CountryViewModelFactory
import kotlinx.coroutines.Dispatchers

class SingletonAppContainer private constructor(context: Context) {
    private val countryDao by lazy {
        AppDataBase.getDataBase(context.applicationContext)?.countryDao()
    }

    private val countryListProvider: CountryListServiceProvider by lazy {
        CountryListProviderViaNetwork()
    }

    val repository: CountryRepository by lazy {
        CountryRepository(countryListProvider, countryDao!!, Dispatchers.IO)
    }

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        CountryViewModelFactory(repository)
    }

    companion object {
        private var instance: SingletonAppContainer? = null
        fun getInstance(context: Context): SingletonAppContainer {
            return instance ?: synchronized(this) {
                instance ?: SingletonAppContainer(context).also { instance = it }
            }
        }
    }
}