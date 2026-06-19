package com.example.drugdose.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class RegisterViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var email by mutableStateOf("")
    var albumNumber by mutableStateOf("")
    var province by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var confirmPasswordVisible by mutableStateOf(false)
    var privacyAccepted by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
        private set
    var errore by mutableStateOf<String?>(null)
        private set
    var successo by mutableStateOf(false)
        private set

    fun onNameChange(v: String) { name = v; errore = null }
    fun onSurnameChange(v: String) { surname = v; errore = null }
    fun onEmailChange(v: String) { email = v; errore = null }
    fun onAlbumNumberChange(v: String) {
        albumNumber = v.filter { it.isDigit() }
        errore = null
    }
    fun onProvinceChange(v: String) {
        province = v.uppercase().filter { it.isLetter() }.take(2)
        errore = null
    }
    fun onPasswordChange(v: String) { password = v; errore = null }
    fun onConfirmPasswordChange(v: String) { confirmPassword = v; errore = null }
    fun togglePasswordVisibility() { passwordVisible = !passwordVisible }
    fun toggleConfirmPasswordVisibility() { confirmPasswordVisible = !confirmPasswordVisible }
    fun onPrivacyAcceptedChange(v: Boolean) { privacyAccepted = v }

    fun register() {
        if (name.isBlank() || surname.isBlank() || email.isBlank() ||
            albumNumber.isBlank() || province.isBlank() ||
            password.isBlank() || confirmPassword.isBlank()
        ) {
            errore = "Compila tutti i campi"
            return
        }

        if (password != confirmPassword) {
            errore = "Le password non coincidono"

            return
        }

        if (!email.contains("@") || !email.contains(".")) {
            errore = "Formato email non valido"
            return
        }

        if (password.length < 6) {
            errore = "La password deve avere almeno 6 caratteri"
            return
        }

        if (province.length != 2){
            errore = "Provincia non valida" //Add list province valide (110)
            return
        }


        viewModelScope.launch {
            isLoading = true
            email = email.trim()
            authRepo.registraMedico(
                email = email,
                password = password,
                nome = name,
                cognome = surname,
                numeroOrdine = albumNumber,
                provinciaOrdine = province
            ).fold(
                onSuccess = {
                    isLoading = false
                    successo = true
                },
                onFailure = { e ->
                    isLoading = false
                    errore = when (e) {
                        is FirebaseAuthUserCollisionException -> "Email già registrata"
                        is FirebaseAuthWeakPasswordException -> "Password troppo debole"
                        is FirebaseAuthInvalidCredentialsException -> "Email non valida"
                        else -> e.message ?: "Errore durante la registrazione"
                    }
                })
        }
    }
}
