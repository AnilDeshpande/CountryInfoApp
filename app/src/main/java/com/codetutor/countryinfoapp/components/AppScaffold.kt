package com.codetutor.countryinfoapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codetutor.countryinfoapp.CountryApplication
import com.codetutor.countryinfoapp.screens.CountryListScreen
import com.codetutor.countryinfoapp.util.getCountryListFromJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryInfoAppScaffold() {

    var countryList = getCountryListFromJson(LocalContext.current)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {

                    Text(text = "CountryInfoApp", style = MaterialTheme.typography.labelLarge)
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription ="Search")
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription ="MoreOptions")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },

        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ) {

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription ="Edit")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Sort, contentDescription ="Sort")
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation (
                    defaultElevation = 5.dp,
                    pressedElevation = 10.dp,
                    focusedElevation = 10.dp,
                    hoveredElevation = 8.dp
                        )) {
                    Icon(imageVector = Icons.Filled.Filter, contentDescription ="Filter")
            }
        },
    ){ innerPadding ->
        CountryApplication(countryList = countryList, innerPadding)
    }
}

@Preview
@Composable
fun SimpleComposablePreview() {
    CountryInfoAppScaffold()
}