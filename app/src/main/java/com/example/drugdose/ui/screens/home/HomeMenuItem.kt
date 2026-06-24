package com.example.drugdose.ui.screens.home

enum class HomeAction {
    DRUG_DOSE,
    PLACEHOLDER
}

data class HomeMenuItem(
    val id: Int,
    val title: String,
    val icon: MenuIcon,
    val action: HomeAction = HomeAction.PLACEHOLDER  // default
)