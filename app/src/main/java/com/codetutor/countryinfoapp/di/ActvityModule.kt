// ActivityModule.kt
package com.codetutor.countryinfoapp.di

import com.codetutor.countryinfoapp.util.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    @Named("ActivityClassName")
    fun provideActivityClassName(): String {
        return "MainActivity"
    }

    @Provides
    @ActivityScoped
    @Named("MainActivity")
    fun provideLogger(@Named("ActivityClassName") className: String): Logger {
        return Logger(className)
    }
}