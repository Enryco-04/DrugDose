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

    var passwordVisible by mutableStateOf(false)
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

    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(v: String) { email = v; errore = null; emailError = null }
    fun onPasswordChange(v: String) { password = v; errore = null; passwordError = null }

    fun login() {
        //Pattern bouncer
        emailError = null
        passwordError = null
        errore = null

        var hasError = false

        if (email.isBlank()) {
            emailError = "Email obbligatoria"
            hasError = true
        }

        if (password.isBlank()) {
            passwordError = "Password obbligatoria"
            hasError = true
        }

        if (!email.isBlank() &&
            (!email.contains("@") || !email.contains("."))) {
            emailError = "Formato email non valido"
            hasError = true
        }

        if (hasError) return


        viewModelScope.launch {
            isLoading = true
            authRepo.login(email, password).fold(
                onSuccess = {
                    isLoading = false
                    successo = true
                },
                onFailure = { e ->
                    isLoading = false
                    println(e)
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

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }
}
