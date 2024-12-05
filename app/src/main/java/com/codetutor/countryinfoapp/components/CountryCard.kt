package com.codetutor.countryinfoapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel

@Composable
fun CountryCard(countryInfo: Country,
                showDeleteAlertDialog: MutableState<Boolean>,
                showUpdateAlertDialog: MutableState<Boolean>,
                viewModel: CountryUIViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(5.dp)
            .wrapContentHeight(align = Alignment.Top),
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.medium

    ) {
        CountryCardWithConstraintLayout(country = countryInfo,
            showDeleteAlertDialog,
            showUpdateAlertDialog,
            viewModel)
    }
}