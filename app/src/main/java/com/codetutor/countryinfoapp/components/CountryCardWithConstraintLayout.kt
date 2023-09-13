package com.codetutor.countryinfoapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.codetutor.countryinfoapp.R
import com.codetutor.countryinfoapp.data.CountryInfo

@Composable
fun CountryCardWithConstraintLayout(countryInfo: CountryInfo){
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        val (flag, commonName, capital, officialName, region, subregion, currencySymbol, currencyName, mobileCode, tld) = createRefs()
        val imageResId = countryInfo.flagId // Replace with your PNG image resource ID
        val imagePainter: Painter = painterResource(id = imageResId)

        val verticalChain  = createHorizontalChain(flag, commonName, capital, chainStyle = ChainStyle.Spread)

        Image(painter = imagePainter,
            contentDescription = "Country Flag",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(50.dp)
                .padding(2.dp)
                .constrainAs(flag) {

                })


        Text(
            text = countryInfo.commonName,
            modifier = Modifier
                .padding(2.dp)
                .constrainAs(commonName) {

                },
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Text(text = countryInfo.nationalCapital,
            fontSize = 15.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(2.dp)
                .constrainAs(capital) {

                })

    }
}