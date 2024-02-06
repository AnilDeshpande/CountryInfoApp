package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codetutor.countryinfoapp.components.CountryInfoAppScaffold
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.screens.CountryInfoScreen
import com.codetutor.countryinfoapp.screens.CountryListScreen
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

@Composable
fun CountryApplication(countryList: MutableList<Country>, paddingValues: PaddingValues){
    val navController  = rememberNavController()

    NavHost(navController = navController, startDestination = "country_list" ){
        composable("country_list"){
            CountryListScreen(countryList, navController, paddingValues)
        }

        composable("country_info"){
            CountryInfoScreen(navController,paddingValues)
        }
    }
}

