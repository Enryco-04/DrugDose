package com.example.drugdose.data.repository

import com.example.drugdose.data.model.Medico
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

private const val MEDICI_COLLECTION = "medici"
// Interazioni con Autenticazione
class AuthRepositoryImpl(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore
) : AuthRepository {

    override suspend fun registraMedico(
        email: String,
        password: String,
        nome: String,
        cognome: String,
        numeroOrdine: String,
        provinciaOrdine: String
    ): Result<Medico> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = requireNotNull(result.user?.uid) {
            "Registrazione completata, ma utente Firebase non disponibile."
        }

        val medico = Medico(
            id = uid,
            nome = nome,
            cognome = cognome,
            email = email,
            numeroIscrizioneOrdine = numeroOrdine,
            provinciaOrdine = provinciaOrdine,
        )

        db.collection(MEDICI_COLLECTION)
            .document(uid)
            .set(medico.copy(id = ""))
            .await()

        medico
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Medico?> = runCatching {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: return@runCatching null

        db.collection(MEDICI_COLLECTION)
            .document(uid)
            .get()
            .await()
            .toObject(Medico::class.java)
            ?.copy(id = uid)
    }

    override fun logout() {
        auth.signOut()
    }

    override suspend fun getMedicoCorrente(): Result<Medico?> = runCatching {
        val uid = auth.currentUser?.uid ?: return@runCatching null

        db.collection(MEDICI_COLLECTION)
            .document(uid)
            .get()
            .await()
            .toObject(Medico::class.java)
            ?.copy(id = uid)
    }



    override fun isLoggato(): Boolean = auth.currentUser != null
}
