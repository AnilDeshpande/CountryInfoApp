package com.codetutor.countryinfoapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codetutor.countryinfoapp.R
import com.codetutor.countryinfoapp.data.CountryInfo

@Composable
fun CountryCard(countryInfo: CountryInfo) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(10.dp).wrapContentHeight(align = Alignment.Top).border(1.dp, Color.LightGray),
        shadowElevation = 2.dp,

        ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(0.2f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(50.dp).padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val imageResId = countryInfo.flagId // Replace with your PNG image resource ID
                    val imagePainter: Painter = painterResource(id = imageResId)
                    Image(
                        painter = imagePainter,
                        contentDescription = "Country Flag",
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = countryInfo.commonName,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f),
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                Text(
                    text = countryInfo.nationalCapital,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f)
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.8f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = countryInfo.officialName,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f)
                )

                Text(
                    text = countryInfo.region,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f)
                )

                Text(
                    text = countryInfo.subRegion,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1.0f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CircularText(text = countryInfo.currencySymbol)

                    Text(
                        text = countryInfo.currencyName,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.4f)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(0.3f),
                        horizontalAlignment = Alignment.End
                    ) {

                        Text(
                            text = countryInfo.mobileCode,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )

                        Text(
                            text = countryInfo.tld,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}