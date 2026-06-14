package com.example.drugdose.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        //private set serve a rendere impossibile per i @Composable modificare i valori
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errore by mutableStateOf<String?>(null)
        private set
    var successo by mutableStateOf(false)
        private set

    fun onEmailChange(v: String) { email = v; errore = null }
    fun onPasswordChange(v: String) { password = v; errore = null }

    fun login() {
        //Pattern bouncer
        if (email.isBlank() || password.isBlank()) {
            errore = "Compila tutti i campi"
            return
        }

        if (!email.contains("@") || !email.contains(".")){
            errore = "Formato email non valida"
            return
        }

        viewModelScope.launch {
            isLoading = true
            authRepo.login(email, password).fold(
                onSuccess = {
                    isLoading = false
                    successo = true
                },
                onFailure = { e ->
                    isLoading = false
                    errore = handleErrorMessage(e)
                }
            )
        }
    }

    private fun handleErrorMessage(e: Throwable): String {
        return when (e) {
            is IOException,
            is FirebaseNetworkException -> "Connessione non riuscita"
            is FirebaseAuthInvalidUserException,
            is FirebaseAuthInvalidCredentialsException -> "Email o Password errate"
            else -> "Email o Password errate"
        }
    }

    fun forgotPassword() {
        // TODO
    }
}
