package com.example.drugdose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.drugdose.ui.theme.DrugDoseTheme
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import com.example.drugdose.di.ViewModelFactory
import com.example.drugdose.ui.theme.*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeViewModel: ThemeViewModel by viewModels {
            ViewModelFactory()
        }
        setContent {
            val darkTheme =
                themeViewModel.darkTheme.collectAsState().value
            DrugDoseTheme(
                darkTheme = darkTheme
            ) {
                // Passa themeViewModel al composable
                CompositionLocalProvider(
                    LocalThemeController provides themeViewModel
                ) {
                    AppNavigation()
                }
            }
        }
    }
}


