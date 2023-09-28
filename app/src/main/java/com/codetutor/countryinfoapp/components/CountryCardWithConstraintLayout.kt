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
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.CountryInfo

@Composable
fun CountryCardWithConstraintLayout(countryInfo: Country){
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth().padding(5.dp)
    ) {
        val (flag, commonName, capital, officialName, region, subregion, currencySymbol, currencyName, mobileCode, tld) = createRefs()
        val imageResId = R.drawable.`in` // Replace with your PNG image resource ID
        val imagePainter: Painter = painterResource(id = imageResId)

        Image(painter = imagePainter,
            contentDescription = "Country Flag",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .height(70.dp)
                .padding(2.dp)
                .constrainAs(flag) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })




        countryInfo.name?.common?.let {
            Text(
                text = it,
                modifier = Modifier
                    .padding(2.dp)
                    .constrainAs(commonName) {
                        top.linkTo(flag.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(flag.end)
                    },
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }

        countryInfo.capital?.get(0)?.let {
            Text(text = it,
                fontSize = 15.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(2.dp)
                    .constrainAs(capital) {
                        start.linkTo(parent.start)
                        top.linkTo(commonName.bottom)
                        end.linkTo(flag.end)
                    })
        }

        countryInfo.name?.official?.let {
            Text(text = it,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(officialName) {
                        top.linkTo(parent.top)
                        start.linkTo(flag.end)
                        end.linkTo(parent.end)
                    }
                    .padding(2.dp)
                    .fillMaxWidth(0.65f))
        }

        countryInfo?.region?.let {
            Text(text = it,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(region) {
                        start.linkTo(flag.end)
                        end.linkTo(parent.end)
                        top.linkTo(officialName.bottom)
                    }
                    .padding(2.dp)
                    .fillMaxWidth(0.8f))
        }

        countryInfo.subregion?.let {
            Text(text = it,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(subregion) {
                        start.linkTo(officialName.start)
                        top.linkTo(region.bottom)
                        end.linkTo(officialName.end)
                    }
                    .padding(2.dp)
                    .fillMaxWidth(0.8f))
        }

        countryInfo.currencies?.NOK?.symbol?.let {
            CircularText(text = it,
                modifier = Modifier
                    .constrainAs(currencySymbol) {
                        start.linkTo(flag.end, margin = 30.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    })
        }
        countryInfo.currencies?.NOK?.name?.let {
            Text(text = it,
                modifier = Modifier
                    .constrainAs(currencyName) {
                        top.linkTo(subregion.bottom)
                        start.linkTo(currencySymbol.end, margin = 12.dp)
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                        end.linkTo(mobileCode.start)
                    }, textAlign = TextAlign.Center
            )
        }

        countryInfo.idd?.let {
            Text(
                text = it.root+""+it.suffixes?.get(0),
                modifier = Modifier.constrainAs(mobileCode) {
                    top.linkTo(subregion.bottom)
                    end.linkTo(parent.end)
                }.width(50.dp)
            )
        }

        countryInfo.tld?.get(0)?.let {
            Text(
                text = it,
                modifier = Modifier.constrainAs(tld) {
                    top.linkTo(mobileCode.bottom)
                    end.linkTo(parent.end)
                }.width(50.dp)
            )
        }

    }
}