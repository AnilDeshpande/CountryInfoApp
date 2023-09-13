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

        val startingBarrier = createStartBarrier(flag)
        val topBarrier = createTopBarrier(flag)
        val flagBottomBarrier = createBottomBarrier(flag)
        val bottomBarrier = createBottomBarrier(capital)


        val imageResId = countryInfo.flagId // Replace with your PNG image resource ID
        val imagePainter: Painter = painterResource(id = imageResId)

        Image(painter = imagePainter,
            contentDescription = "Country Flag",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(50.dp)
                .padding(2.dp)
                .constrainAs(flag) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })


        Text(
            text = countryInfo.officialName,
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(0.8f)
                .constrainAs(officialName) {
                    start.linkTo(flag.end)
                    //top.linkTo(topBarrier)
                    bottom.linkTo(flagBottomBarrier)
                },
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            fontSize = 20.sp)

        Text(
            text = countryInfo.commonName,
            modifier = Modifier
                .padding(2.dp)
                .constrainAs(commonName) {
                    top.linkTo(flag.bottom)
                    start.linkTo(startingBarrier)
                    end.linkTo(flag.end)
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
                    start.linkTo(startingBarrier)
                    top.linkTo(commonName.bottom)
                    end.linkTo(flag.end)
                })

        CircularText(text = countryInfo.currencySymbol,
            modifier = Modifier
                .constrainAs(currencySymbol) {
                    start.linkTo(flag.end, margin = 30.dp)
                    bottom.linkTo(bottomBarrier, margin = 8.dp)
                })


    }
}