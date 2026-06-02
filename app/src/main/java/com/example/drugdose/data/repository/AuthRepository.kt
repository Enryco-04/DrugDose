package com.example.drugdose.data.repository

import com.example.drugdose.data.model.Medico

interface AuthRepository {

    suspend fun registraMedico(
        email: String,
        password: String,
        nome: String,
        cognome: String,
        numeroOrdine: String,
        provinciaOrdine: String
    ): Result<Medico>

    suspend fun login(
        email: String,
        password: String
    ): Result<Medico?>

    fun logout()

    fun getMedicoIdCorrente(): String?

    fun isLoggato(): Boolean
}
