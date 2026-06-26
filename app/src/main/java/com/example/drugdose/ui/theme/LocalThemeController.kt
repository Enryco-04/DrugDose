package com.example.drugdose.ui.theme

import androidx.compose.runtime.compositionLocalOf

val LocalThemeController = compositionLocalOf<ThemeViewModel> {
    error("ThemeViewModel non trovato")
}