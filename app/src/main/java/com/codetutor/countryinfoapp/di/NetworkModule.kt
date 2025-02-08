package com.codetutor.countryinfoapp.di

import com.codetutor.countryinfoapp.repository.service.CountryListProviderViaNetwork
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @NetworkProvider
    @Binds
    @Singleton
    abstract fun bindCountryListServiceProvider(
        countryListProviderViaNetwork: CountryListProviderViaNetwork
    ):CountryListServiceProvider
}