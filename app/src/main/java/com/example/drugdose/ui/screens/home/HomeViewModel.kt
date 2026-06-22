package com.example.drugdose.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.R
import com.example.drugdose.data.model.Medico
import com.example.drugdose.data.repository.AuthRepository
import com.example.drugdose.ui.model.HomeAction
import com.example.drugdose.ui.model.HomeMenuItem
import com.example.drugdose.ui.model.MenuIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _menuItems = MutableStateFlow<List<HomeMenuItem>>(emptyList())
    val menuItems: StateFlow<List<HomeMenuItem>> = _menuItems.asStateFlow()

    var medico by mutableStateOf<Medico?>(null)
        private set

    var sessioneNonValida by mutableStateOf(false)
        private set


    init {
        caricaMedico()
        loadMenuItems()
    }

    private fun caricaMedico() {
        viewModelScope.launch {
            authRepo.getMedicoCorrente().fold(
                onSuccess = {
                    if (it != null) {
                        medico = it
                    }
                    else{
                        medico = null
                        authRepo.logout()
                        sessioneNonValida = true //AppNavigation rimanda immediatamente a Login Page
                    }
                },
                onFailure = {
                    authRepo.logout()
                    sessioneNonValida = true //AppNavigation rimanda immediatamente a Login Page
                }
            )
        }
    }

    private fun loadMenuItems() {
            _menuItems.value = listOf(
                HomeMenuItem(1, "Drug Dose", MenuIcon.Resource(R.drawable.ic_pill), HomeAction.DRUG_DOSE),
                HomeMenuItem(2, "DermaCalc", MenuIcon.Vector(Icons.Default.ReceiptLong)),  // PLACEHOLDER di default
                HomeMenuItem(3, "PatientKiosk", MenuIcon.Vector(Icons.Default.ReceiptLong)),
                HomeMenuItem(4, "MelanomaTNM", MenuIcon.Vector(Icons.Default.ReceiptLong)),
                HomeMenuItem(5, "DermaBSA", MenuIcon.Vector(Icons.Default.ReceiptLong)),
                HomeMenuItem(6, "SCC TNM", MenuIcon.Vector(Icons.Default.ReceiptLong)),
                HomeMenuItem(7, "Vaccini", MenuIcon.Vector(Icons.Default.ReceiptLong)),
                HomeMenuItem(8, "Linee di Langer", MenuIcon.Vector(Icons.Default.ReceiptLong)),
                HomeMenuItem(9, "Altro", MenuIcon.Vector(Icons.Default.ReceiptLong)),
                HomeMenuItem(10, "Altro", MenuIcon.Vector(Icons.Default.ReceiptLong))
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