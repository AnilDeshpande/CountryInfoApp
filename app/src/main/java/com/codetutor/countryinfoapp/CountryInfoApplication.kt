package com.codetutor.countryinfoapp

import android.app.Application

class CountryInfoApplication: Application() {
    /*val applicationContainer by lazy {
        AppContainer(this)
    }*/

    val applicationContainer by lazy {
        SingletonAppContainer.getInstance(this)
    }
}