package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codetutor.countryinfoapp.components.CountryInfoAppScaffold
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.CountryInfo
import com.codetutor.countryinfoapp.screens.MainScreen
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme
import com.codetutor.countryinfoapp.ui.theme.MyCustomAppTheme
import com.codetutor.countryinfoapp.util.getCountryList
import com.codetutor.countryinfoapp.util.getCountryListFromJson

class MainActivity : ComponentActivity() {


    private lateinit var countryList: MutableList<Country>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        countryList = getCountryListFromJson(this.applicationContext)
        setContent {
            MyCustomAppTheme {
                CountryInfoAppScaffold(countryList)
            }
        }
    }
}

