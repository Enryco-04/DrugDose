package com.example.drugdose.data.repository

import com.example.drugdose.data.model.Farmaco
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

private const val FARMACI_COLLECTION = "drugs"
// Alcuni metodi sono proibiti dalle security rules, non ancora supportati

//I metodi usano runCachting cosi la gestione degli errori è piu semplice:
//Simili alle promise di js

// Il viewModel farà result.fold(onSuccess {..}, onFailure {...}
class FarmaciRepositoryImpl(
    private val db: FirebaseFirestore = Firebase.firestore
) : FarmaciRepository {

    private val farmaciCollection = db.collection(FARMACI_COLLECTION)

    override suspend fun getFarmaci(): Result<List<Farmaco>> = runCatching {
        farmaciCollection
            .orderBy("nome", Query.Direction.ASCENDING) //Default
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(Farmaco::class.java)?.copy(id = document.id) //copia l'ogetto,aggiungi l'id copiandolo dal documento
            }
    }

    override suspend fun getFarmacoById(idFarmaco: String): Result<Farmaco?> = runCatching {
        farmaciCollection
            .document(idFarmaco)
            .get()
            .await()
            .toObject(Farmaco::class.java)
            ?.copy(id = idFarmaco)
    }
    // Metodo Filler (compito spetta al frontend?)
    //Usa getFarmaci() e filtra, non fa una query apposta
    override suspend fun cercaFarmaci(query: String): Result<List<Farmaco>> = runCatching {
        val normalizedQuery = query.trim()

        if (normalizedQuery.isEmpty()) {
            return@runCatching getFarmaci().getOrThrow()
        }

        getFarmaci()
            .getOrThrow()
            .filter { farmaco ->
                farmaco.nome.contains(normalizedQuery, ignoreCase = true) ||
                    farmaco.nomeCommerciale.contains(normalizedQuery, ignoreCase = true) ||
                    farmaco.indicazione.contains(normalizedQuery, ignoreCase = true)
            }
    }
    // Proibito dalle security rules,inutilizzato, solo per il futuro
    override suspend fun creaFarmaco(farmaco: Farmaco): Result<Farmaco> = runCatching {
        if (farmaco.id.isBlank()) {
            val document = farmaciCollection.add(farmaco).await()
            farmaco.copy(id = document.id)
        } else {
            farmaciCollection.document(farmaco.id).set(farmaco).await()
            farmaco
        }
    }
    //Proibito dalle security rules, inutilizzato, solo per il futuro
    override suspend fun modificaFarmaco(
        id: String,
        nuovoFarmaco: Farmaco
    ): Result<Farmaco> = runCatching {
        val farmacoAggiornato = nuovoFarmaco.copy(id = id)
        farmaciCollection.document(id).set(farmacoAggiornato).await()
        farmacoAggiornato
    }
    //Proibito dalle security rules, inutilizzato, solo per il futuro
    override suspend fun eliminaFarmaco(id: String): Result<Unit> = runCatching {
        farmaciCollection.document(id).delete().await()
    }
}
