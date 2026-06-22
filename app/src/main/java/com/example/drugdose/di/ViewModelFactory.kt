package com.example.drugdose.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.drugdose.data.repository.AuthRepository
import com.example.drugdose.data.repository.AuthRepositoryImpl
import com.example.drugdose.data.repository.FarmaciRepository
import com.example.drugdose.data.repository.FarmaciRepositoryImpl
import com.example.drugdose.data.repository.PrescrizioniRepository
import com.example.drugdose.data.repository.PrescrizioniRepositoryImpl
import com.example.drugdose.ui.screens.create.CreatePrescriptionViewModel
import com.example.drugdose.ui.screens.search.DrugSearchViewModel
import com.example.drugdose.ui.screens.home.HomeViewModel
import com.example.drugdose.ui.screens.login.LoginViewModel
import com.example.drugdose.ui.screens.prescriptions.PrescriptionsViewModel
import com.example.drugdose.ui.screens.register.RegisterViewModel


class ViewModelFactory(
    private val authRepo: AuthRepository = AuthRepositoryImpl(),
    private val farmaciRepo: FarmaciRepository = FarmaciRepositoryImpl(),
    private val prescrizioniRepo: PrescrizioniRepository = PrescrizioniRepositoryImpl(),
    // ↓ parametro extra, usato SOLO quando si crea un CreatePrescriptionViewModel.
    // Per tutti gli altri ViewModel resta null e viene ignorato.
    private val farmacoId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java ->
                LoginViewModel(authRepo) as T

            RegisterViewModel::class.java ->
                RegisterViewModel(authRepo) as T

            HomeViewModel::class.java ->
                HomeViewModel(authRepo) as T

            DrugSearchViewModel::class.java ->
                DrugSearchViewModel(farmaciRepo, authRepo) as T

            CreatePrescriptionViewModel::class.java -> {
                requireNotNull(farmacoId) {
                    "farmacoId è obbligatorio per CreatePrescriptionViewModel — passalo al costruttore di ViewModelFactory"
                }
                CreatePrescriptionViewModel(farmacoId, farmaciRepo) as T
            }

            PrescriptionsViewModel::class.java ->
                PrescriptionsViewModel(prescrizioniRepo,authRepo) as T

            else -> throw IllegalArgumentException("ViewModel non riconosciuto: ${modelClass.name}")
        }
    }
}