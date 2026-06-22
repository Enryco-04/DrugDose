package com.example.drugdose.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.model.Farmaco
import com.example.drugdose.data.repository.AuthRepository
import com.example.drugdose.data.repository.FarmaciRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class DrugSearchViewModel(
    private val drugRepo: FarmaciRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _farmaci = MutableStateFlow<List<Farmaco>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set
    var errore by mutableStateOf<String?>(null)
        private set

    var URLRcp:String by mutableStateOf("")


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
        viewModelScope.launch {
            isLoading = true
            errore = null

            drugRepo.getFarmaci().fold(
                onSuccess = { lista ->
                    isLoading = false
                    _farmaci.value = lista
                },
                onFailure = { e ->
                    isLoading = false
                    errore = e.message ?: "Errore nel caricamento dei farmaci"
                }
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun retry() {
        loadFarmaci()
    }
}