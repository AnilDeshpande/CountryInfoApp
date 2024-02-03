package com.codetutor.countryinfoapp.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val customShapes = Shapes (
    extraLarge = RoundedCornerShape(30.dp),
    large = RoundedCornerShape(25.dp),
    medium = CutCornerShape(bottomEndPercent = 10),
    small = RoundedCornerShape(10.dp),
    extraSmall = RoundedCornerShape(5.dp),
)