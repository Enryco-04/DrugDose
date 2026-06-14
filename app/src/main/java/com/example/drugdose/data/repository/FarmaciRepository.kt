package com.example.drugdose.data.repository

import com.example.drugdose.data.model.Farmaco
//Contratto in stile REST-API, alcune operazioni per ora sono proibite
//I metodi restituiscono tutti un oggetto di tipo Result<T>

interface FarmaciRepository {

    suspend fun getFarmaci(): Result<List<Farmaco>>

    suspend fun getFarmacoById(
        idFarmaco: String
    ): Result<Farmaco?>

    suspend fun cercaFarmaci(
        query: String
    ): Result<List<Farmaco>>


    //Al momento è proibito dalle security rules, è solo un placeholder
    suspend fun creaFarmaco(
        farmaco: Farmaco
    ): Result<Farmaco>

    //al momento è proibito, è solo un placeholder
    suspend fun modificaFarmaco(id: String, nuovoFarmaco: Farmaco): Result<Farmaco>

    //al momento è proibito, è solo un placeholder
    suspend fun eliminaFarmaco(id: String): Result<Unit>



}
