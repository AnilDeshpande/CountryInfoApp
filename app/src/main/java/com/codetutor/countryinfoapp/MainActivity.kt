package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountryCard()
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
            .padding(2.dp),

    ) {
        Row(modifier = Modifier.fillMaxWidth(1.0f)) {
            Column(modifier = Modifier
                .wrapContentSize()
                .weight(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                Box(modifier = Modifier
                    .padding(2.dp)){
                    val imageResId = R.drawable.`in` // Replace with your PNG image resource ID
                    val imagePainter: Painter = painterResource(id = imageResId)
                    Image(painter = imagePainter, contentDescription = "Country Flag")
                }

                Text(text ="India",
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f),
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    fontSize = 50.sp)

                Text(text ="New Delhi",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f))
            }

            Column(modifier = Modifier
                .weight(0.6f)
                .wrapContentSize(),
                verticalArrangement = Arrangement.SpaceEvenly) {
                Text(text ="Republic of India",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f))

                Text(text ="Asia",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f))

                Text(text ="South Asia",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f))

                Row(modifier = Modifier
                    .fillMaxWidth(1.0f)
                    ,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {

                    CircularText(text = "₹")

                    Text(text ="Indian Rupee",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.4f))

                    Column(modifier = Modifier.fillMaxWidth(0.3f),
                        horizontalAlignment = Alignment.End
                    ) {

                        Text(text ="+91",
                            textAlign = TextAlign.Center,
                            modifier = Modifier)

                        Text(text =".in",
                            textAlign = TextAlign.Center,
                            modifier = Modifier)
                    }
                }
            }

            Column(modifier = Modifier.weight(0.1f)) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Arrow Icon",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}


@Composable
fun CircularText(text: String){
    Text(
        modifier = Modifier
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
    CountryCard()
}