package com.example.drugdose.domain

//Class solo per formattare i dati in output, no DB
data class RisultatoCalcolo(
    val doseRealeMg: Double,
    val doseArrotondataMg: Double?,
    val numeroUnitaSomministrare: Double,
    val erroriCalcolo: List<String>? = null

)