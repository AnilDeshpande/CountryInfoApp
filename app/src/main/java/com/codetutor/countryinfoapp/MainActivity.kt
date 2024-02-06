package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codetutor.countryinfoapp.components.CountryInfoAppScaffold
import com.codetutor.countryinfoapp.ui.theme.MyCustomAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCustomAppTheme {
                CountryInfoAppScaffold()
            }
        }
    }
}

