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

    var nameError by mutableStateOf<String?>(null)
        private set
    var surnameError by mutableStateOf<String?>(null)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var albumError by mutableStateOf<String?>(null)
        private set
    var provinceError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set
    var errore by mutableStateOf<String?>(null)
        private set
    var successo by mutableStateOf(false)
        private set

    fun onNameChange(v: String) { name = v; nameError = null }
    fun onSurnameChange(v: String) { surname = v; surnameError = null }
    fun onEmailChange(v: String) { email = v; emailError = null }
    fun onAlbumNumberChange(v: String) {
        albumNumber = v.filter { it.isDigit() }
        albumError = null
    }
    fun onProvinceChange(v: String) {
        province = v.uppercase().filter { it.isLetter() }.take(2)
        provinceError = null
    }
    fun onPasswordChange(v: String) {
        password = v
        passwordError = null
        confirmPasswordError = null
    }
    fun onConfirmPasswordChange(v: String) {
        confirmPassword = v
        confirmPasswordError = null
    }
    fun togglePasswordVisibility() { passwordVisible = !passwordVisible }
    fun toggleConfirmPasswordVisibility() { confirmPasswordVisible = !confirmPasswordVisible }
    fun onPrivacyAcceptedChange(v: Boolean) { privacyAccepted = v }

    fun register() {
        errore = null

        nameError = if (name.isBlank()) "Campo obbligatorio" else null
        surnameError = if (surname.isBlank()) "Campo obbligatorio" else null
        albumError = if (albumNumber.isBlank()) "Campo obbligatorio" else null

        emailError = when {
            email.isBlank() -> "Campo obbligatorio"
            !email.contains("@") || !email.contains(".") -> "Formato email non valido"
            else -> null
        }

        provinceError = when {
            province.isBlank() -> "Campo obbligatorio"
            province.length != 2 -> "Provincia non valida"
            else -> null
        }

        passwordError = when {
            password.isBlank() -> "Campo obbligatorio"
            password.length < 6 -> "La password deve avere almeno 6 caratteri"
            else -> null
        }

        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Campo obbligatorio"
            password != confirmPassword -> "Le password non coincidono"
            else -> null
        }

        val hasErrors = listOf(
            nameError, surnameError, emailError, albumError,
            provinceError, passwordError, confirmPasswordError
        ).any { it != null }

        if (hasErrors) return

        viewModelScope.launch {
            isLoading = true
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
                    when (e) {
                        is FirebaseAuthUserCollisionException -> emailError = "Email già registrata"
                        is FirebaseAuthWeakPasswordException -> passwordError = "Password troppo debole"
                        is FirebaseAuthInvalidCredentialsException -> emailError = "Email non valida"
                        else -> errore = e.message ?: "Errore durante la registrazione"
                    }
                }
            )
        }
    }
}
