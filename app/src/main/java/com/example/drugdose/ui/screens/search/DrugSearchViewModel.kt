package com.example.drugdose.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.model.Farmaco
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class DrugSearchViewModel : ViewModel() {

    // Lista completa caricata (da repository/Firestore in futuro)
    private val _farmaci = MutableStateFlow<List<Farmaco>>(emptyList())

    // Testo della barra di ricerca
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Lista filtrata in base alla ricerca — si aggiorna automaticamente
    val farmacoFiltrati: StateFlow<List<Farmaco>> = combine(
        _farmaci,
        _searchQuery
    ) { farmaci, query ->
        if (query.isBlank()) {
            farmaci
        } else {
            farmaci.filter { farmaco ->
                farmaco.nome.contains(query, ignoreCase = true) ||
                        farmaco.nomeCommerciale.contains(query, ignoreCase = true) ||
                        farmaco.indicazione.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadFarmaci()
    }

    private fun loadFarmaci() {
        // TODO: sostituire con chiamata a Repository/Firestore
        _farmaci.value = listOf(
            Farmaco(
                id = "1",
                nome = "Paracetamolo",
                nomeCommerciale = "Tachipirina",
                indicazione = "Analgesico e antipiretico per il trattamento del dolore lieve e della febbre.",
                tipoFormula = "Peso"
            ),
            Farmaco(
                id = "2",
                nome = "Ibuprofene",
                nomeCommerciale = "Moment",
                indicazione = "Antinfiammatorio non steroideo per dolore e febbre.",
                tipoFormula = "Peso"
            ),
            Farmaco(
                id = "3",
                nome = "Amoxicillina",
                nomeCommerciale = "Augmentin",
                indicazione = "Antibiotico per infezioni batteriche delle vie respiratorie.",
                tipoFormula = "Fascia"
            )
        )
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}