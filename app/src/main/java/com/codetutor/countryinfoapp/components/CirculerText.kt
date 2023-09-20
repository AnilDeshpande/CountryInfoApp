package com.codetutor.countryinfoapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CirculerText(text: String, modifier: Modifier){
    Text(
        modifier = modifier
            .padding(2.dp)
            .drawBehind {
                drawCircle(
                    color = Color.LightGray,
                    radius = this.size.maxDimension
                )
            },
        text = text,
    )
}