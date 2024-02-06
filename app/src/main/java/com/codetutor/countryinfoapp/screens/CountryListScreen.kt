package com.codetutor.countryinfoapp.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codetutor.countryinfoapp.components.CountryCard
import com.codetutor.countryinfoapp.data.Country

@Composable
fun CountryListScreen(countryList: MutableList<Country>,navController: NavController, innerPadding: PaddingValues = PaddingValues(2.dp)) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
    ) {
        LazyColumn {
            items(countryList, ) {
                CountryCard(countryInfo = it, navController)
            }
        }
    }
}