package com.codetutor.countryinfoapp

import android.content.Context
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import com.codetutor.countryinfoapp.database.dao.CountryDao
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.ICountryRepository
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCountryDao(@ApplicationContext context: Context): CountryDao {
        return AppDatabase.getDatabase(context).countryDao()
    }

    @Provides
    @Singleton
    fun provideCountryListServiceProvider(@ApplicationContext context: Context): CountryListServiceProvider {
        return CountryListServiceProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideCountryRepository(countryDao: CountryDao, countryListServiceProvider: CountryListServiceProvider): ICountryRepository {
        return CountryRepository(countryDao, countryListServiceProvider, Dispatchers.IO)
    }
}