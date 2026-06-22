package com.example.drugdose.ui.screens.create

import com.example.drugdose.data.model.Farmaco

/**
 * Stato del form multi-step per la creazione di una prescrizione.
 * Vive nel ViewModel e viene letto/scritto da tutti e 3 gli step.
 */
data class PrescriptionFormState(
    // --- Step 1: Paziente ---
    val nome: String = "",
    val cognome: String = "",
    val codiceFiscale: String = "",
    val etaAnni: String = "",
    val pesoKg: String = "",
    val altezzaCm: String = "",

    // --- Step 2: Farmaco ---
    val farmaco: Farmaco? = null,
    val frequenza: String = "",
    val numeroConfezioni: String = "",
    val note: String = "",

    // --- Calcolo dose (placeholder — da collegare alla logica reale) ---
    val doseEsattaMg: Double? = null,
    val doseArrotondataMg: Double? = null,
    val numeroUnitaTesto: String? = null ,
    val erroriCalcolo: List<String>? = null

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