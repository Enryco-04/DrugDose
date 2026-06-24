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
                HomeMenuItem(2, "DermaCalc", MenuIcon.Resource(R.drawable.ic_dermacalc)),
                HomeMenuItem(3, "PatientKiosk", MenuIcon.Resource(R.drawable.ic_patientkiosk)),
                HomeMenuItem(4, "MelanomaTNM", MenuIcon.Resource(R.drawable.ic_melanomatnm)),
                HomeMenuItem(5, "DermaBSA", MenuIcon.Resource(R.drawable.ic_dermabsa)),
                HomeMenuItem(6, "SCC TNM", MenuIcon.Resource(R.drawable.ic_scctnm)),
                HomeMenuItem(7, "Vaccini", MenuIcon.Resource(R.drawable.ic_vaccini)),
                HomeMenuItem(8, "Linee di Langer", MenuIcon.Resource(R.drawable.ic_lineedilanger)),
                HomeMenuItem(9, "Temperatura", MenuIcon.Resource(R.drawable.ic_term)),
                HomeMenuItem(10, "Add", MenuIcon.Resource(R.drawable.ic_plus))
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