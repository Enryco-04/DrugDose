package com.example.drugdose.data.model
//Identica al DB in firestore
data class Farmaco(
    val id: String = "",
    val nome: String = "",
    val nomeCommerciale: String = "",
    val indicazione: String = "",
    val tipoFormula: String = "",
    val doseUnitaria: Double? = null,
    val unitaMisura: String = "",
    val doseMassimaMg: Double? = null,
    val doseMinimaMg: Double? = null,
    val pesoMinimoKg: Double? = null,
    val etaMinimaAnni: Int? = null,
    val unitaSomministrazioneMg: Double = 0.0,
    val formaFarmaceuticaSomministrazione: String = "",
    val strategiaArrotondamento: String = "",
    val fascePeso: List<FasciaPeso>? = null,
    // puo essere eliminaoìto forse (tenuto per retrocompatibilità)
    val alerts: List<String> = emptyList(),
    val rcp: Rcp? = null,
    val fonteRcp: String = ""
)

data class FasciaPeso(
    val id: String = "",
    val minKg: Double = 0.0,
    val maxKg: Double? = null,
    val doseMg: Double = 0.0
)

data class Rcp(
    val controindicazioni: List<String> = emptyList(),
    val avvertenzeSpeciali: List<String> = emptyList(),
    val interazioni: List<String> = emptyList()
)