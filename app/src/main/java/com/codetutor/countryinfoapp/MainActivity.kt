package com.codetutor.countryinfoapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codetutor.countryinfoapp.components.CountryCardWithConstraintLayout
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.CountryInfo
import com.codetutor.countryinfoapp.util.getCountryList
import com.codetutor.countryinfoapp.ui.theme.CountryInfoAppTheme
import com.codetutor.countryinfoapp.util.getCountryListFromJson

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

    private val countryList = getCountryList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(countryList = countryList)
        }
    }
}


@Composable
fun MainScreen(countryList: List<CountryInfo>) {

    val context: Context = LocalContext.current

    val countries  = getCountryListFromJson(context)
    Log.i("MainActivity", "Countries: $countries.size")

    CountryInfoAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn {
                items(countries) {
                    CountryCard(countryInfo = it)
                }
            }
        }
    }
}

@Composable
fun MainScreen(countryList: List<CountryInfo>) {

    val context: Context = LocalContext.current

    val countries  = getCountryListFromJson(context)
    Log.i("MainActivity", "Countries: $countries.size")

    CountryInfoAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn {
                items(countries) {
                    CountryCard(countryInfo = it)
                }
            }
        }
    }
}

@Composable
fun CountryCard(countryInfo: Country) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(5.dp)
            .border(1.dp, Color.LightGray)
            .wrapContentHeight(align = Alignment.Top),
        shadowElevation = 2.dp

    ) {
        CountryCardWithConstraintLayout(country = countryInfo)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen(getCountryList())
}