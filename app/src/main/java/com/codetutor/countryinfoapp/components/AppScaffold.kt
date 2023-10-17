package com.codetutor.countryinfoapp.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.codetutor.countryinfoapp.screens.MainScreen
import com.codetutor.countryinfoapp.util.getCountryListFromJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryInfoAppScaffold() {

    var countryList = getCountryListFromJson(LocalContext.current)
    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(text = "Country Info App")
                }
            )
        }
    ){ innerPadding ->
        MainScreen( countryList, innerPadding = innerPadding)
    }
}