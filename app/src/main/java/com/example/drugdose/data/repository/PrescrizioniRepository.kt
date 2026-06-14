package com.example.drugdose.data.repository

import com.example.drugdose.data.model.Prescrizione

interface PrescrizioniRepository {


    suspend fun creaPrescrizione(
        prescrizione: Prescrizione
    ): Result<Prescrizione>

    suspend fun getPrescrizioni(): Result<List<Prescrizione>>


    suspend fun getPrescrizioniByIdFarmaco(
        idFarmaco: String
    ): Result<List<Prescrizione>>

    suspend fun getPrescrizioneById(
        prescrizioneId: String
    ): Result<Prescrizione?>

    suspend fun annullaPrescrizione(
        prescrizioneId: String
    ): Result<Unit>




}
