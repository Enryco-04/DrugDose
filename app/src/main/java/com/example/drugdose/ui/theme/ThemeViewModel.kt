package com.example.drugdose.ui.theme


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {

    private val _darkTheme = MutableStateFlow(false)

    val darkTheme = _darkTheme.asStateFlow()

    fun toggleTheme() {
        _darkTheme.value = !_darkTheme.value
    }
}