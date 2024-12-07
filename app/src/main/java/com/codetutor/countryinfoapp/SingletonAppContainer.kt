package com.codetutor.countryinfoapp

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.service.CountryListProviderViaNetwork
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.viewmodel.CountryViewModelFactory
import kotlinx.coroutines.Dispatchers

class SingletonAppContainer private constructor(context: Context) {
    private val countryDao by lazy {
        AppDatabase.getDatabase(context.applicationContext).countryDao()
    }

    private val countryListProvider: CountryListServiceProvider by lazy {
        CountryListProviderViaNetwork()
    }

    val repository: CountryRepository by lazy {
        CountryRepository(countryDao, countryListProvider, Dispatchers.IO)
    }

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        CountryViewModelFactory(repository)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppContainer? = null

        fun getInstance(context: Context): AppContainer {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppContainer(context).also { INSTANCE = it }
            }
        }
    }
}