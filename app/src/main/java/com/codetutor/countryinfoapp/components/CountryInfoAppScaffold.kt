package com.codetutor.countryinfoapp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.codetutor.countryinfoapp.screens.MainScreen
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codetutor.countryinfoapp.CountryInfoApplication
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import com.codetutor.countryinfoapp.repository.CountryRepository
import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import com.codetutor.countryinfoapp.repository.service.CountryListProviderViaNetwork
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProvider
import com.codetutor.countryinfoapp.repository.service.CountryListServiceProviderImpl
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel
import kotlinx.coroutines.Dispatchers


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryInfoAppScaffold(operationViewModel: CountryOperationViewModel,
                           uiViewModel: CountryUIViewModel){

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current



    val selectedFilter = uiViewModel.selectedFilter
    val filterByKey = uiViewModel.filterByKey

    ObserveFilterKeyChanges(filterByKey, selectedFilter, operationViewModel)

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
//                navigationIcon = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "ArrowBack")
//                    }
//                },

                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "MoreVert")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar {
                FilterCountryChips("Continent", selectedFilter)
                FilterCountryChips("Drive Side", selectedFilter)

                if (selectedFilter.value != null) {
                    TextField(
                        value = filterByKey.value,
                        onValueChange = { newValue ->
                            filterByKey.value = newValue
                        },
                        modifier = Modifier.padding(3.dp),
                        label = { Text("") },
                        singleLine = true,
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation( )) {
                    Icon(imageVector = Icons.Filled.Filter, contentDescription = "Filter")
            }
        }

    ) { innerPaddingValues ->
        MainScreen(innerPaddingValues, operationViewModel, uiViewModel)
    }
}