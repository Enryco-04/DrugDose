package com.example.drugdose.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun login() {
        // TODO: integrazione con AuthRepository
        println("Login con: $email / $password")
    }
    //TODO DELETE probably, too long to setup the pages?
    fun forgotPassword() {
        // TODO
    }
}