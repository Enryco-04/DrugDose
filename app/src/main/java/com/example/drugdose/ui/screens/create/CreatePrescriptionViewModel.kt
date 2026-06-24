package com.example.drugdose.ui.screens.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.domain.Calculator
import com.example.drugdose.data.model.Farmaco
import com.example.drugdose.data.model.PazienteEmbedded
import com.example.drugdose.data.model.Prescrizione
import com.example.drugdose.data.repository.AuthRepository
import com.example.drugdose.data.repository.FarmaciRepository
import com.example.drugdose.data.repository.PrescrizioniRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatePrescriptionViewModel(
    private val farmacoId: String,
    private val farmaciRepo: FarmaciRepository,
    private val prescrizioniRepo: PrescrizioniRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(PrescriptionFormState())
    val formState: StateFlow<PrescriptionFormState> = _formState.asStateFlow()

    private val _currentStep = MutableStateFlow(PrescrizioneStep.PAZIENTE)
    val currentStep: StateFlow<PrescrizioneStep> = _currentStep.asStateFlow()

    private val _salvataggioState = MutableStateFlow<SalvataggioState>(SalvataggioState.Idle)
    val salvataggioState: StateFlow<SalvataggioState> = _salvataggioState.asStateFlow()

    // Stato di caricamento del farmaco (separato dal salvataggio finale)
    private val _caricamentoFarmacoState =
        MutableStateFlow<CaricamentoState>(CaricamentoState.Loading)
    val caricamentoFarmacoState: StateFlow<CaricamentoState> =
        _caricamentoFarmacoState.asStateFlow()

    var mostraPopUpErrori by mutableStateOf(false)
        private set

    fun chiudiPopUpErrori() {
        mostraPopUpErrori = false
    }

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
                        _caricamentoFarmacoState.value =
                            CaricamentoState.Error("Farmaco non trovato")
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

    fun onNomeChange(value: String) = updateForm { it.copy(nome = value, nomeError = false) }
    fun onCognomeChange(value: String) =
        updateForm { it.copy(cognome = value, cognomeError = false) }

    fun onCodiceFiscaleChange(value: String) =
        updateForm { it.copy(codiceFiscale = value, codiceFiscaleError = false) }

    fun onEtaChange(value: String) = updateForm { it.copy(etaAnni = value, etaError = false) }
    fun onPesoChange(value: String) = updateForm { it.copy(pesoKg = value, pesoError = false) }
    fun onAltezzaChange(value: String) =
        updateForm { it.copy(altezzaCm = value, altezzaError = false) }

    fun onNumeroConfezioniChange(value: String) =
        updateForm { it.copy(numeroConfezioni = value, numeroConfezioniError = false) }

    fun onFrequenzaChange(value: String) = updateForm { it.copy(frequenza = value) }
    fun onNoteChange(value: String) = updateForm { it.copy(note = value) }

    private fun updateForm(transform: (PrescriptionFormState) -> PrescriptionFormState) {
        _formState.value = transform(_formState.value)
        ricalcolaDose()
    }

    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
        }
    }

    /**
     * PLACEHOLDER — calcolo dose non reale.
     * TODO: collegare la vera logica basata su peso/età/farmaco.doseUnitariaMg/strategiaArrotondamento.
     */
    private fun ricalcolaDose() {
        val state = _formState.value
        val farmaco = state.farmaco

        val peso = state.pesoKg.toDoubleOrNull()
        val altezza = state.altezzaCm.toDoubleOrNull()
        val eta = state.etaAnni.toIntOrNull()

        // Se manca anche un solo dato necessario, azzera il risultato — non c'è errore da mostrare,
        // semplicemente l'utente non ha ancora finito di compilare
        if (farmaco == null || peso == null || altezza == null || eta == null) {
            _formState.value = state.copy(
                doseEsattaMg = null,
                doseArrotondataMg = null,
                numeroUnitaTesto = null,
                erroriCalcolo = null
            )
            return
        }

        val risultato = Calculator.calcola(farmaco, peso, altezza, eta)

        _formState.value = state.copy(
            doseEsattaMg = risultato.doseRealeMg,
            doseArrotondataMg = risultato.doseArrotondataMg,
            numeroUnitaTesto = formatNumeroUnita(risultato.numeroUnitaSomministrare, farmaco),
            erroriCalcolo = risultato.erroriCalcolo
        )
    }

    private fun formatNumeroUnita(numeroUnita: Double, farmaco: Farmaco): String {
        val count = numeroUnita.toInt()
        val forma = pluralizza(farmaco.formaFarmaceuticaSomministrazione, count)
        return "$count $forma"
    }

    private fun pluralizza(forma: String, count: Int): String {
        if (count == 1) return forma
        return when (forma.lowercase()) {
            "fiala" -> "fiale"
            "capsula" -> "capsule"
            "compressa" -> "compresse"
            "bustina" -> "bustine"
            "flaconcino" -> "flaconcini"
            else -> forma
        }
    }

    fun goToNextStep() {
        when (_currentStep.value) {

            PrescrizioneStep.PAZIENTE -> {
                val s = _formState.value
                val hasErrors = s.nome.isBlank() || s.cognome.isBlank() ||
                        s.codiceFiscale.isBlank() || s.etaAnni.isBlank() ||
                        s.pesoKg.isBlank() || s.altezzaCm.isBlank()

                // aggiorna i flag di errore — rosso solo sui campi vuoti
                _formState.value = s.copy(
                    nomeError = s.nome.isBlank(),
                    cognomeError = s.cognome.isBlank(),
                    codiceFiscaleError = s.codiceFiscale.isBlank(),
                    etaError = s.etaAnni.isBlank(),
                    pesoError = s.pesoKg.isBlank(),
                    altezzaError = s.altezzaCm.isBlank()
                )

                if (hasErrors) return

                if (!s.erroriCalcolo.isNullOrEmpty()) {
                    mostraPopUpErrori = true
                    return
                }

                _currentStep.value = PrescrizioneStep.FARMACO
            }

            PrescrizioneStep.FARMACO -> {
                val s = _formState.value
                val hasErrors = s.numeroConfezioni.isBlank()

                _formState.value = s.copy(
                    numeroConfezioniError = s.numeroConfezioni.isBlank()
                )

                if (hasErrors) return

                _currentStep.value = PrescrizioneStep.RIEPILOGO
            }

            PrescrizioneStep.RIEPILOGO -> { /* niente — usa il bottone Conferma */
            }
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
                // salvo numero untia e forma farmaceutica

                val state = _formState.value


                val numeroUnita = state.numeroUnitaTesto
                    ?.substringBefore(" ")
                    ?.toIntOrNull()
                    ?: 0

                val idMedico = authRepo.getMedicoCorrente().fold(
                    onSuccess = { it?.id },
                    onFailure = { "" }
                )

                val farmaco = _formState.value.farmaco ?: return@launch

                //Chiama PrescrizioniRepo
                //Raccoglie tutti i dati necessari al salvataggio su DB
                val prescrizione = Prescrizione(
                    idMedico = idMedico,
                    dataCreazione = Timestamp.now(),
                    dataScadenza =Timestamp(Timestamp.now().seconds + (30L * 24L * 60L * 60L), 0),
                    paziente = PazienteEmbedded(
                        nome = state.nome,
                        cognome = state.cognome,
                        codiceFiscale = state.codiceFiscale
                    ),

                    idFarmaco = farmaco.id,
                    nomeFarmaco = farmaco.nome,
                    nomeCommercialeFarmaco = farmaco.nomeCommerciale,

                    dosaggioMg = state.doseArrotondataMg ?: 0.0,

                    formaFarmaceuticaSomministrazione =
                        farmaco.formaFarmaceuticaSomministrazione,

                    numeroUnitaSomministrazione = numeroUnita,

                    quantita = state.numeroConfezioni.toIntOrNull() ?: 1,

                    frequenza = state.frequenza,
                    note = state.note.takeIf { it.isNotEmpty() } ?: "Nessuna nota")



                prescrizioniRepo.creaPrescrizione(prescrizione)
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