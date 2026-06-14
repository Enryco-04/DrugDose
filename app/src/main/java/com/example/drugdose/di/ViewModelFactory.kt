package com.example.drugdose.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.drugdose.data.repository.AuthRepository
import com.example.drugdose.data.repository.AuthRepositoryImpl
import com.example.drugdose.data.repository.FarmaciRepository
import com.example.drugdose.data.repository.FarmaciRepositoryImpl
import com.example.drugdose.data.repository.PrescrizioniRepository
import com.example.drugdose.data.repository.PrescrizioniRepositoryImpl
import com.example.drugdose.ui.screens.login.LoginViewModel
import com.example.drugdose.ui.screens.register.RegisterViewModel

class ViewModelFactory(
    private val authRepo: AuthRepository = AuthRepositoryImpl(),
    private val farmaciRepo: FarmaciRepository = FarmaciRepositoryImpl(),
    private val prescrizioniRepo: PrescrizioniRepository = PrescrizioniRepositoryImpl()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java ->
                LoginViewModel(authRepo) as T

            RegisterViewModel::class.java ->
                RegisterViewModel(authRepo) as T

            // futuri ViewModel — aggiungi qui
            // CalcoloViewModel::class.java ->
            //     CalcoloViewModel(farmaciRepo) as T

            else -> throw IllegalArgumentException("ViewModel non riconosciuto: ${modelClass.name}")
        }
    }
}