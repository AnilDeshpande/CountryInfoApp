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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(){
    CountryInfoAppTheme {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface) {
            CountryCard()
        }
    }
}


@Composable
fun CountryCard(){
    Surface(modifier = Modifier
        .fillMaxWidth(1.0f)
        .padding(10.dp)
        .wrapContentHeight(align = Alignment.Top)
        .border(1.dp, Color.LightGray),
        shadowElevation = 2.dp) {

        Row (modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier
                .fillMaxWidth(0.2f)
                .weight(0.2f)) {

                Box(modifier = Modifier
                    .width(70.dp)
                    .height(50.dp)
                    .padding(2.dp),
                    contentAlignment = Alignment.Center) {

                    val imageResId = R.drawable.`in`
                    val imagePainter: Painter = painterResource(id = imageResId)

                    Image(painter = imagePainter,
                        contentDescription = "Country Flag",
                        contentScale = ContentScale.Crop)
                }

                Text(text = "India", modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .padding(2.dp),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center)

                Text(text = "New Delhi", modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .padding(2.dp),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center)
            }

            Column(modifier = Modifier
                .fillMaxWidth(0.8f)
                .weight(0.8f)) {

                Text(text ="Republic of India",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f))

                Text(text ="Asia",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f))

                Text(text ="South Asia",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(1.0f))

                Row(modifier = Modifier.fillMaxWidth(1.0f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {

                    CirculerText(text = "₹")

                    Text(text ="Indian Rupee",
                        fontSize = 15.sp,
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
        }
    }
}

@Composable
fun CirculerText(text: String){
    Text(text = text,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(0.1f)
            .drawBehind {
                drawCircle(color = Color.LightGray, radius = this.size.maxDimension)
            })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}