package com.codetutor.countryinfoapp.di

import android.app.Application
import android.content.Context
import com.codetutor.countryinfoapp.database.appdb.AppDataBase
import com.codetutor.countryinfoapp.database.dao.CountryDao
import com.codetutor.countryinfoapp.database.dao.ICountryDao
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.ICountryRepository
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProviderImpl
import dagger.Binds
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
object AppModule{

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDataBase{
        return AppDataBase.getDataBase(context) as AppDataBase
    }

    @Provides
    @Singleton
    fun provideCountryDao(database: AppDataBase): ICountryDao = database.countryDao()

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO



    @Provides
    @Singleton
    fun provideCountryRepository(
        countryDao: CountryDao,
        @NetworkProvider countryListServiceProvider: CountryListServiceProvider,
        dispatcher: CoroutineDispatcher
    ): ICountryRepository {
        return CountryRepository(countryListServiceProvider, countryDao, dispatcher)
    }

    @LocalProvider
    @Provides
    @Singleton
    fun provideCountryListServiceProviderImpl(context: Context): CountryListServiceProvider{
        return CountryListServiceProviderImpl(context)
    }

}