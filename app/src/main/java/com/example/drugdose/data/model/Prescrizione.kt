package com.example.drugdose.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude


data class Prescrizione(
    @get:Exclude //per non mettere id dentro il DB ma solo come identificatore di documento
    val id: String = "",

    val idMedico: String? = "",
    val status: String = "ATTIVA",
    val dataCreazione: Timestamp? = null,
    val dataScadenza: Timestamp? = null,
    val annullatoIl: Timestamp? = null,
    val paziente: PazienteEmbedded = PazienteEmbedded(),
    val idFarmaco: String = "",
    val nomeFarmaco: String = "",
    val nomeCommercialeFarmaco: String = "",
    val dosaggioMg: Double = 0.0,
    val formaFarmaceuticaSomministrazione: String = "",
    val numeroUnitaSomministrazione: Int = 0,
    val quantita: Int = 0,
    val note: String? = null,
    val frequenza: String? = null
) {
    @Exclude
    fun isScaduta(): Boolean =
        status == "ATTIVA" &&
                dataScadenza?.toDate()?.before(java.util.Date()) == true
    @Exclude
    fun statoVisualizzato(): String = when {
        status == "ANNULLATA" -> "ANNULLATA"
        isScaduta() -> "SCADUTA"
        else -> "ATTIVA"
    }
}

data class PazienteEmbedded(
    val nome: String = "",
    val cognome: String = "",
    val codiceFiscale: String = ""
)