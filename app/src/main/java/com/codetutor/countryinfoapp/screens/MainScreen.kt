package com.codetutor.countryinfoapp.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.codetutor.countryinfoapp.components.CountryCard
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.CountryInfo
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme
import com.codetutor.countryinfoapp.util.getCountryListFromJson

@Composable
fun MainScreen(countryList: MutableList<Country>, innerPaddingValues: PaddingValues) {

    CountryInfoAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues = innerPaddingValues),
            color = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn {
                items(countryList) {
                    CountryCard(countryInfo = it)
                }
            }
        }
    }
}