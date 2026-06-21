package com.example.drugdose.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.repository.FarmaciRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatePrescriptionViewModel(
    private val farmacoId: String,
    private val farmaciRepo: FarmaciRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(PrescriptionFormState())
    val formState: StateFlow<PrescriptionFormState> = _formState.asStateFlow()

    private val _currentStep = MutableStateFlow(PrescrizioneStep.PAZIENTE)
    val currentStep: StateFlow<PrescrizioneStep> = _currentStep.asStateFlow()

    private val _salvataggioState = MutableStateFlow<SalvataggioState>(SalvataggioState.Idle)
    val salvataggioState: StateFlow<SalvataggioState> = _salvataggioState.asStateFlow()

    // Stato di caricamento del farmaco (separato dal salvataggio finale)
    private val _caricamentoFarmacoState = MutableStateFlow<CaricamentoState>(CaricamentoState.Loading)
    val caricamentoFarmacoState: StateFlow<CaricamentoState> = _caricamentoFarmacoState.asStateFlow()

    init {
        caricaFarmaco()
    }

    private fun caricaFarmaco() {
        viewModelScope.launch {
            _caricamentoFarmacoState.value = CaricamentoState.Loading

            farmaciRepo.getFarmacoById(farmacoId).fold(
                onSuccess = { farmaco ->
                    if (farmaco != null) {
                        _formState.value = _formState.value.copy(farmaco = farmaco)
                        _caricamentoFarmacoState.value = CaricamentoState.Success
                    } else {
                        _caricamentoFarmacoState.value = CaricamentoState.Error("Farmaco non trovato")
                    }
                },
                onFailure = { error ->
                    _caricamentoFarmacoState.value = CaricamentoState.Error(
                        error.message ?: "Errore nel caricamento del farmaco"
                    )
                }
            )
        }
    }

    // --- Aggiornamento campi Step 1: Paziente ---
    fun onNomeChange(value: String) = updateForm { it.copy(nome = value) }
    fun onCognomeChange(value: String) = updateForm { it.copy(cognome = value) }
    fun onCodiceFiscaleChange(value: String) = updateForm { it.copy(codiceFiscale = value) }
    fun onEtaChange(value: String) = updateForm { it.copy(etaAnni = value) }
    fun onPesoChange(value: String) = updateForm { it.copy(pesoKg = value) }
    fun onAltezzaChange(value: String) = updateForm { it.copy(altezzaCm = value) }

    // --- Aggiornamento campi Step 2: Farmaco ---
    fun onFrequenzaChange(value: String) = updateForm { it.copy(frequenza = value) }
    fun onNumeroConfezioniChange(value: String) = updateForm { it.copy(numeroConfezioni = value) }
    fun onNoteChange(value: String) = updateForm { it.copy(note = value) }

    private fun updateForm(transform: (PrescriptionFormState) -> PrescriptionFormState) {
        _formState.value = transform(_formState.value)
        ricalcolaDose()
    }

    /**
     * PLACEHOLDER — calcolo dose non reale.
     * TODO: collegare la vera logica basata su peso/età/farmaco.doseUnitariaMg/strategiaArrotondamento.
     */
    private fun ricalcolaDose() {
        val state = _formState.value
        val peso = state.pesoKg.toDoubleOrNull()
        val farmaco = state.farmaco

        if (peso == null || farmaco == null) {
            _formState.value = state.copy(
                doseEsattaMg = null,
                doseArrotondataMg = null,
                doseUnitaria = null
            )
            return
        }

        val doseEsatta = peso * 3.7 // valore fittizio
        val doseArrotondata = Math.round(doseEsatta / 50.0) * 50.0

        _formState.value = state.copy(
            doseEsattaMg = doseEsatta,
            doseArrotondataMg = doseArrotondata,
            doseUnitaria = "1 –"
        )
    }

    // --- Navigazione tra step ---
    fun goToNextStep() {
        _currentStep.value = when (_currentStep.value) {
            PrescrizioneStep.PAZIENTE -> PrescrizioneStep.FARMACO
            PrescrizioneStep.FARMACO -> PrescrizioneStep.RIEPILOGO
            PrescrizioneStep.RIEPILOGO -> PrescrizioneStep.RIEPILOGO
        }
    }

    fun goToPreviousStep() {
        _currentStep.value = when (_currentStep.value) {
            PrescrizioneStep.PAZIENTE -> PrescrizioneStep.PAZIENTE
            PrescrizioneStep.FARMACO -> PrescrizioneStep.PAZIENTE
            PrescrizioneStep.RIEPILOGO -> PrescrizioneStep.FARMACO
        }
    }

    // --- Salvataggio finale (Conferma) ---
    fun confermaPrescrizione(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _salvataggioState.value = SalvataggioState.Loading
            try {
                // TODO: sostituire con vera chiamata a PrescrizioniRepository.creaPrescrizione(...)
                delay(1500) // simula latenza di rete
                _salvataggioState.value = SalvataggioState.Success
                onSuccess()
            } catch (e: Exception) {
                _salvataggioState.value = SalvataggioState.Error(
                    e.message ?: "Errore durante il salvataggio"
                )
            }
        }
    }
}

sealed class CaricamentoState {
    object Loading : CaricamentoState()
    object Success : CaricamentoState()
    data class Error(val message: String) : CaricamentoState()
}