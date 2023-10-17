package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.screens.MainScreen
import com.codetutor.countryinfoapp.util.getCountryListFromJson

class MainActivity : ComponentActivity() {

    private lateinit var countryList: MutableList<Country>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        countryList = getCountryListFromJson(context = this.application)
        setContent {
            MainScreen(countryList = countryList)
        }
    }
}

