package com.codetutor.countryinfoapp.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val replyShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(5.dp),
//    medium = RoundedCornerShape(10.dp),
    medium = CutCornerShape(bottomEndPercent = 10),
    large = RoundedCornerShape(15.dp),
    extraLarge = RoundedCornerShape(20.dp)
)