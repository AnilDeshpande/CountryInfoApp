package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codetutor.countryinfoapp.components.CountryCardWithConstraintLayout
import com.codetutor.countryinfoapp.data.CountryInfo

class MainActivity : ComponentActivity() {

    private val indiaInfo = CountryInfo(R.drawable.`in`,
        "India",
        "New Delhi",
        "Republic of India",
        "Asia","South Asia",
        "₹",
        "Indian Rupee",
        "+91",
        ".in")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(indiaInfo)
        }
    }
}

@Composable
fun MainScreen(countryInfo: CountryInfo){
    Surface (modifier = Modifier.fillMaxSize()) {
        CountryCard(countryInfo = countryInfo)
    }
}

@Composable
fun CountryCard(countryInfo: CountryInfo){
    Surface(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(5.dp)
            .border(1.dp, Color.LightGray)
            .wrapContentHeight(align = Alignment.Top),
        shadowElevation = 2.dp

    ) {
        CountryCardWithConstraintLayout(countryInfo)

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val indiaInfo = CountryInfo(R.drawable.`in`,
        "India",
        "New Delhi",
        "Republic of India",
        "Asia","South Asia",
        "₹",
        "Indian Rupee",
        "+91",
        ".in")

    MainScreen(indiaInfo)
}