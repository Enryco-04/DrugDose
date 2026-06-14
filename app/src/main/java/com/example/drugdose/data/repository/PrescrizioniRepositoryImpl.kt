package com.example.drugdose.data.repository

import android.util.Log
import com.example.drugdose.data.model.Prescrizione
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

private const val PRESCRIZIONI_COLLECTION = "prescrizioni"
//Temp logging
private const val TAG = "PrescrizioniRepo"



// Implementazione del contratto, non tutti i metodi verrano usati, i filtri sono compito del frontend per rendere smooth i cambi di
// animazione
class PrescrizioniRepositoryImpl(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore
) : PrescrizioniRepository {

    private val prescrizioniCollection = db.collection(PRESCRIZIONI_COLLECTION)

    override suspend fun creaPrescrizione(
        prescrizione: Prescrizione
    ): Result<Prescrizione> {
        Log.d(TAG, "creaPrescrizione - Step 1: Input prescrizione: $prescrizione")
        val medicoId = getMedicoIdCorrenteOrFailure().getOrElse {
            Log.e(TAG, "creaPrescrizione - Errore medicoId: ${it.message}")
            return Result.failure(it)
        }

        return runCatching {
            //in locale
            val prescrizioneDaSalvare = prescrizione.copy(
                id = "",
                idMedico = medicoId
            )
            Log.d(TAG, "creaPrescrizione - Step 2: Prescrizione preparata per il DB: $prescrizioneDaSalvare")

            val document = prescrizioniCollection.add(prescrizioneDaSalvare).await()
            val snapshot = document.get().await()

            Log.d(TAG, "FIRESTORE SAVED DOCUMENT: ${snapshot.data}")

            val prescrizioneSalvata = prescrizioneDaSalvare.copy(id = document.id)

            Log.d(TAG, "creaPrescrizione - Step 3: Successo. Prescrizione salvata: $prescrizioneSalvata")
            prescrizioneSalvata

        }.onFailure {
            Log.e(TAG, "creaPrescrizione - Fallimento: ${it.message}", it)
        }
    }

    override suspend fun getPrescrizioni(): Result<List<Prescrizione>> {
        //Per ora non c'è la possibilita di ottenere tutte le prescrizioni ma solo quelle dell medico loggato
        val medicoId = getMedicoIdCorrenteOrFailure().getOrElse {
            return Result.failure(it)
        }

        return runCatching {
            val snapshot = prescrizioniCollection
                .whereEqualTo("idMedico", medicoId)
                .orderBy("dataCreazione", Query.Direction.DESCENDING)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { document ->
                Log.d("CHECK_SCADUTA", document.getBoolean("scaduta").toString())
                document.toObject(Prescrizione::class.java)?.copy(id = document.id)
            }
        }
    }

    override suspend fun getPrescrizioniByIdFarmaco(
        idFarmaco: String
    ): Result<List<Prescrizione>> {
        val medicoId = getMedicoIdCorrenteOrFailure().getOrElse {
            return Result.failure(it)
        }

        return runCatching {
            val snapshot = prescrizioniCollection
                .whereEqualTo("idMedico", medicoId)
                .whereEqualTo("idFarmaco", idFarmaco)
                .orderBy("dataCreazione", Query.Direction.DESCENDING)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { document ->
                document.toObject(Prescrizione::class.java)?.copy(id = document.id)
            }
        }
    }

    override suspend fun getPrescrizioneById(
        prescrizioneId: String
    ): Result<Prescrizione?> {
        val medicoId = getMedicoIdCorrenteOrFailure().getOrElse {
            return Result.failure(it)
        }

        return runCatching {
            val document = prescrizioniCollection
                .document(prescrizioneId)
                .get()
                .await()
            
            document.toObject(Prescrizione::class.java)
                ?.copy(id = prescrizioneId)
                ?.takeIf { prescrizione -> prescrizione.idMedico == medicoId }
        }
    }

    //NOTA: SOFT DELETE
    override suspend fun annullaPrescrizione(
        prescrizioneId: String
    ): Result<Unit> {
        getMedicoIdCorrenteOrFailure().getOrElse {
            return Result.failure(it)
        }

        return runCatching {
            prescrizioniCollection
                .document(prescrizioneId)
                .update("status", "ANNULLATA")
                .await()
            Unit
        }
    }

    //Helper per sapere l'Id del medico senza passare dal frontend
    private fun getMedicoIdCorrenteOrFailure(): Result<String> {
        val medicoId = auth.currentUser?.uid
            ?: return Result.failure(Exception("Utente non loggato"))

        return Result.success(medicoId)
    }
}
