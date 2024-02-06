package com.codetutor.countryinfoapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.screens.CountryInfoScreen
import com.codetutor.countryinfoapp.screens.CountryListScreen

@Composable
fun CountryApplication(countryList: MutableList<Country>, paddingValues: PaddingValues){
    val navController  = rememberNavController()

    NavHost(navController = navController, startDestination = "country_list") {
        composable("country_list") {
            CountryListScreen(countryList, navController, paddingValues)
        }

        composable("country_info") {
            CountryInfoScreen(navController, paddingValues)
        }
    }
}