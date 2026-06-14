package com.example.drugdose.data.model

import com.google.firebase.firestore.Exclude

data class Medico(
    @get:Exclude
    val id: String = "",

    val nome: String = "",
    val cognome: String = "",
    val email: String = "",
    val numeroIscrizioneOrdine: String = "",
    val provinciaOrdine: String = "",

)
