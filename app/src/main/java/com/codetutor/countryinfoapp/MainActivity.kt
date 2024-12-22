package com.codetutor.countryinfoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.codetutor.countryinfoapp.components.CountryInfoAppScaffold
import com.codetutor.countryinfoapp.ui.theme.MyCustomAppTheme
import com.codetutor.countryinfoapp.util.Logger
import com.codetutor.countryinfoapp.viewmodel.CountryOperationViewModel
import com.codetutor.countryinfoapp.viewmodel.CountryUIViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val operationViewModel: CountryOperationViewModel by viewModels()
    private val uiViewModel: CountryUIViewModel by viewModels()

    @Inject
    @Named("MainActivity")
    lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppContainer is accessible any where in the app
        logger.info("Activity loaded")

        setContent {
            MyCustomAppTheme {
                CountryInfoAppScaffold(operationViewModel, uiViewModel, logger)
            }
        }
    }
}

