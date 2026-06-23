package com.example.drugdose.ui.screens.create

import com.example.drugdose.data.model.Farmaco

/**
 * Stato del form multi-step per la creazione di una prescrizione.
 * Vive nel ViewModel e viene letto/scritto da tutti e 3 gli step.
 */
data class PrescriptionFormState(
    // Step 1 — Paziente
    val nome: String = "",
    val cognome: String = "",
    val codiceFiscale: String = "",
    val etaAnni: String = "",
    val pesoKg: String = "",
    val altezzaCm: String = "",

    // Step 2 — Farmaco
    val farmaco: Farmaco? = null,
    val frequenza: String = "",           // opzionale
    val numeroConfezioni: String = "",    // obbligatorio
    val note: String = "",                // opzionale

    // Calcolo
    val doseEsattaMg: Double? = null,
    val doseArrotondataMg: Double? = null,
    val numeroUnitaTesto: String? = null,
    val erroriCalcolo: List<String>? = null,

    // Errori di validazione UI — true = campo vuoto e tentativo di avanzare
    val nomeError: Boolean = false,
    val cognomeError: Boolean = false,
    val codiceFiscaleError: Boolean = false,
    val etaError: Boolean = false,
    val pesoError: Boolean = false,
    val altezzaError: Boolean = false,
    val numeroConfezioniError: Boolean = false
)
enum class PrescrizioneStep(val label: String) {
    PAZIENTE("Paziente"),
    FARMACO("Farmaco"),
    RIEPILOGO("Riepilogo")
}

/** Stato del salvataggio finale (bottone Conferma) */
sealed class SalvataggioState {
    object Idle : SalvataggioState()
    object Loading : SalvataggioState()
    object Success : SalvataggioState()
    data class Error(val message: String) : SalvataggioState()
}