package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.codetutor.countryinfoapp.components.CountryInfoAppScaffold
import com.codetutor.countryinfoapp.ui.theme.MyCustomAppTheme
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppContainer is accessible any where in the app

        setContent {
            MyCustomAppTheme {
                CountryInfoAppScaffold()
            }
        }
    }
}

