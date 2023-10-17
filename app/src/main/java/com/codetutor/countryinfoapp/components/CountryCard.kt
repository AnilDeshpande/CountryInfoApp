package com.codetutor.countryinfoapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codetutor.countryinfoapp.data.Country

@Composable
fun CountryCard(countryInfo: Country) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(5.dp)
            .border(1.dp, Color.LightGray)
            .wrapContentHeight(align = Alignment.Top),
        shadowElevation = 2.dp

    ) {
        CountryCardWithConstraintLayout(country = countryInfo)
    }
}