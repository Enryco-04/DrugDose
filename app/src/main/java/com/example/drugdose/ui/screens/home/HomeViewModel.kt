package com.example.drugdose.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.lifecycle.ViewModel
import com.example.drugdose.R
import com.example.drugdose.ui.model.HomeMenuItem
import com.example.drugdose.ui.model.MenuIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _menuItems = MutableStateFlow<List<HomeMenuItem>>(emptyList())
    val menuItems: StateFlow<List<HomeMenuItem>> = _menuItems.asStateFlow()

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {
        _menuItems.value = listOf(
            HomeMenuItem(
                id = 1,
                title = "Drug Dose",
                icon = MenuIcon.Resource(R.drawable.ic_pill),
                route = "drug_dose"
            ),
            HomeMenuItem(
                id = 2,
                title = "Fun2",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun2"
            ),
            HomeMenuItem(
                id = 3,
                title = "Fun3",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun3"
            ),
            HomeMenuItem(
                id = 4,
                title = "Fun4",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun4"
            ),
            HomeMenuItem(
                id = 5,
                title = "Fun5",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun5"
            ),
            HomeMenuItem(
                id = 6,
                title = "Fun6",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun6"
            ),
            HomeMenuItem(
                id = 7,
                title = "Fun7",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun7"
            ),
            HomeMenuItem(
                id = 8,
                title = "Fun8",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun8"
            ),
            HomeMenuItem(
                id = 9,
                title = "Fun9",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun9"
            ),
            HomeMenuItem(
                id = 10,
                title = "Fun10",
                icon = MenuIcon.Vector(Icons.Default.ReceiptLong),
                route = "Fun10"
            )
        )
    }

    // Aggiungi un nuovo item a runtime (es. da API)
    fun addMenuItem(item: HomeMenuItem) {
        _menuItems.update { currentList -> currentList + item }
    }

    // Rimuovi un item per id
    fun removeMenuItem(id: Int) {
        _menuItems.update { currentList -> currentList.filter { it.id != id } }
    }
}