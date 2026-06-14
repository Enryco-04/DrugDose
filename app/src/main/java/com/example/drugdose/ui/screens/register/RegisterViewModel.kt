package com.example.drugdose.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
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

    fun onNameChange(newValue: String) { name = newValue }
    fun onSurnameChange(newValue: String) { surname = newValue }
    fun onEmailChange(newValue: String) { email = newValue }
    fun onAlbumNumberChange(newValue: String) { albumNumber = newValue }
    fun onProvinceChange(newValue: String) { province = newValue }
    fun onPasswordChange(newValue: String) { password = newValue }
    fun onConfirmPasswordChange(newValue: String) { confirmPassword = newValue }
    fun togglePasswordVisibility() { passwordVisible = !passwordVisible }
    fun toggleConfirmPasswordVisibility() { confirmPasswordVisible = !confirmPasswordVisible }
    fun onPrivacyAcceptedChange(newValue: Boolean) { privacyAccepted = newValue }

    //TODO integra con backend
    fun register() {
        println("Nome: $name")
        println("Cognome: $surname")
        println("Email: $email")
        println("Numero d'albo: $albumNumber")
        println("Provincia: $province")
        println("Password: $password")
        println("Conferma Password: $confirmPassword")
    }
}
