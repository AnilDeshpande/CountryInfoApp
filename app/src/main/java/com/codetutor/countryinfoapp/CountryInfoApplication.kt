package com.codetutor.countryinfoapp

import android.app.Application

class CountryInfoApplication: Application() {
    lateinit var applicationContainer : AppContainer
    override fun onCreate() {
        super.onCreate()
        applicationContainer = AppContainer(this)
    }
}