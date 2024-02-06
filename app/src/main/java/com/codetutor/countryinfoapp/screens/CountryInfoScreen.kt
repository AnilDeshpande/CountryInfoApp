package com.codetutor.countryinfoapp.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CountryInfoScreen(navController: NavController, innerPadding: PaddingValues = PaddingValues(2.dp)){
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
    ) {
        Text(text = "This is a country info screen")
    }

}