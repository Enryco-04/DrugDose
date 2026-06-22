package com.example.drugdose.ui.screens.prescriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.model.Prescrizione
import com.example.drugdose.data.repository.PrescrizioniRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import com.example.drugdose.data.repository.AuthRepository
import kotlinx.coroutines.launch
enum class FiltroStatus(val label: String) {
    TUTTI("Tutti"),
    ATTIVA("Attiva"),
    SCADUTA("Scaduta"),
    ANNULLATA("Annullata")
}
class PrescriptionsViewModel(
    private val prescrizioniRepo: PrescrizioniRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _prescrizioni = MutableStateFlow<List<Prescrizione>>(emptyList())

    private val _filtroPaziente = MutableStateFlow("")
    val filtroPaziente: StateFlow<String> = _filtroPaziente.asStateFlow()

    private val _filtroFarmaco = MutableStateFlow("")
    val filtroFarmaco: StateFlow<String> = _filtroFarmaco.asStateFlow()

    private val _filtroStatus = MutableStateFlow(FiltroStatus.TUTTI)
    val filtroStatus: StateFlow<FiltroStatus> = _filtroStatus.asStateFlow()

    val prescrizioniFiltrate: StateFlow<List<Prescrizione>> = combine(
        _prescrizioni,
        _filtroPaziente,
        _filtroFarmaco,
        _filtroStatus
    ) { lista, filtroPaziente, filtroFarmaco, status ->

        lista.filter { p ->

            val nomeCompleto =
                "${p.paziente.nome} ${p.paziente.cognome}"

            val matchPaziente =
                filtroPaziente.isBlank() ||
                        p.paziente.codiceFiscale.contains(
                            filtroPaziente,
                            ignoreCase = true
                        ) ||
                        p.paziente.nome.contains(
                            filtroPaziente,
                            ignoreCase = true
                        ) ||
                        p.paziente.cognome.contains(
                            filtroPaziente,
                            ignoreCase = true
                        ) ||
                        nomeCompleto.contains(
                            filtroPaziente,
                            ignoreCase = true
                        )

            val matchFarmaco =
                filtroFarmaco.isBlank() ||
                        p.nomeFarmaco.contains(
                            filtroFarmaco,
                            ignoreCase = true
                        ) ||
                        p.nomeCommercialeFarmaco.contains(
                            filtroFarmaco,
                            ignoreCase = true
                        )

            val matchStatus =
                status == FiltroStatus.TUTTI ||
                        p.statoVisualizzato() == status.name

            matchPaziente && matchFarmaco && matchStatus
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadPrescrizioni()
    }

    private fun loadPrescrizioni() {
        viewModelScope.launch {
            prescrizioniRepo.getPrescrizioni().fold(
                onSuccess = { lista ->
                    _prescrizioni.value = lista
                },
                onFailure = {
                    // TODO gestione errore
                }
            )
        }
    }

    fun onFiltroPazienteChange(value: String) {
        _filtroPaziente.value = value
    }

    fun onFiltroFarmacoChange(value: String) {
        _filtroFarmaco.value = value
    }

    fun onFiltroStatusChange(value: FiltroStatus) {
        _filtroStatus.value = value
    }

    fun annullaPrescrizione(id: String) {
        viewModelScope.launch {
            prescrizioniRepo.annullaPrescrizione(id).fold(
                onSuccess = {
                    _prescrizioni.value = _prescrizioni.value.map { p ->
                        if (p.id == id) {
                            p.copy(status = "ANNULLATA")
                        } else {
                            p
                        }
                    }
                },
                onFailure = {
                    // TODO gestione errore
                }
            )
        }
    }

    fun refreshPrescrizioni() {
        loadPrescrizioni()
    }
}