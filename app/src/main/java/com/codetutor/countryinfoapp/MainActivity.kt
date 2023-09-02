package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}


@Composable
fun MainScreen() {
    CountryInfoAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            CountryCard()
        }
    }
}

@Composable
fun CountryCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(5.dp).border(1.dp, Color.LightGray).wrapContentHeight(align = Alignment.Top),
        shadowElevation = 2.dp

    ) {
        CountryCardWithConstraintLayout()
    }
}








@Composable
fun CountryCardWithConstraintLayout(){
    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()) {
        val (flag, commonName, capital, officialName, region, subregion, currencySymbol, currencyName, mobileCode, tld ) = createRefs()
        val imageResId = R.drawable.`in` // Replace with your PNG image resource ID
        val imagePainter: Painter = painterResource(id = imageResId)

        Image(painter = imagePainter,
            contentDescription = "Country Flag",
            contentScale = ContentScale.Crop,
            modifier  = Modifier
                .fillMaxWidth(0.2f)
                .height(50.dp)
                .padding(2.dp)
                .constrainAs(flag) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })


        Text(text ="India",
            modifier = Modifier
                .padding(2.dp)
                .constrainAs(commonName) {
                    top.linkTo(flag.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(flag.end)
                },
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            fontSize = 20.sp)

        Text(text ="New Delhi",
            fontSize = 15.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(2.dp)
                .constrainAs(capital) {
                    start.linkTo(parent.start)
                    top.linkTo(commonName.bottom)
                    end.linkTo(flag.end)
                })

        Text(text ="Republic of India",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(officialName) {
                    top.linkTo(parent.top)
                    start.linkTo(flag.end)
                    end.linkTo(parent.end)
                }
                .padding(2.dp)
                .fillMaxWidth(0.8f))

        Text(text ="Asia",
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

        Text(text ="South Asia",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(subregion) {
                    start.linkTo(flag.end)
                    top.linkTo(region.bottom)
                }
                .padding(2.dp)
                .fillMaxWidth(0.8f))

        CircularText(text = "₹",
            modifier = Modifier
                .constrainAs(currencySymbol) {
                    start.linkTo(flag.end, margin = 30.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                })
        Text(text = "Indian Rupee",
            modifier = Modifier
                .constrainAs(currencyName) {
                    top.linkTo(subregion.bottom)
                    start.linkTo(currencySymbol.end, margin = 12.dp)
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                    end.linkTo(mobileCode.start)
                }, textAlign = TextAlign.Center )

        Text(
            text = "+91",
            modifier = Modifier.constrainAs(mobileCode) {
                top.linkTo(subregion.bottom)
                end.linkTo(parent.end)
            }.width(50.dp))

        Text(
            text = ".in",
            modifier = Modifier.constrainAs(tld) {
                top.linkTo(mobileCode.bottom)
                end.linkTo(parent.end)
            }.width(50.dp))

    }
}

@Composable
fun CircularText(text: String, modifier: Modifier){
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}