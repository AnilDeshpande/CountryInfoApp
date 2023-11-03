package com.codetutor.countryinfoapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
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
//        shape = MaterialTheme.shapes.small,
        shape = CutCornerShape(bottomEndPercent = 20),
//        border  = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer) ,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(5.dp)
            .wrapContentHeight(align = Alignment.Top),
    ) {
        CountryCardWithConstraintLayout(country = countryInfo)
    }
}