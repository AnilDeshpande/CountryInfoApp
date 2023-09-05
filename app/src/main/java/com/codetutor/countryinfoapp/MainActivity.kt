package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codetutor.countryinfoapp.components.CountryCard
import com.codetutor.countryinfoapp.data.CountryInfo
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme

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
    CountryInfoAppTheme {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface) {
            CountryCard(countryInfo)
        }
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