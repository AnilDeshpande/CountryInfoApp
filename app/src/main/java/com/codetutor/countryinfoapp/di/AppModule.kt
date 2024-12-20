package com.codetutor.countryinfoapp.di

import android.content.Context
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import com.codetutor.countryinfoapp.database.dao.ICountryDao
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.ICountryRepository
import com.codetutor.countryinfoapp.repository.service.CountryListProviderViaNetwork
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProviderImpl
import com.codetutor.countryinfoapp.repository.service.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context) as AppDatabase
    }

    @Provides
    fun provideCountryDao(database: AppDatabase): ICountryDao = database.countryDao()


    @Provides
    @Singleton
    fun provideCountryListServiceProvider(apiService: ApiService): CountryListServiceProvider {
        return CountryListProviderViaNetwork(apiService)
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideCountryRepository(
        countryDao: ICountryDao,
        countryListServiceProvider: CountryListServiceProvider,
        dispatcher: CoroutineDispatcher
    ): ICountryRepository {
        return CountryRepository(countryDao, countryListServiceProvider, dispatcher)
    }

    @LocalProvider
    @Provides
    @Singleton
    fun provideCountryListServiceProviderImpl(context: Context): CountryListServiceProvider {
        return CountryListServiceProviderImpl(context)
    }
}