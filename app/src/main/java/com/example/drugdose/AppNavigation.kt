package com.example.drugdose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.drugdose.di.ViewModelFactory
import com.example.drugdose.ui.model.HomeAction
import com.example.drugdose.ui.screens.create.CreatePrescriptionScreen
import com.example.drugdose.ui.screens.home.HomeScreen
import com.example.drugdose.ui.screens.loading.LoadingScreen
import com.example.drugdose.ui.screens.login.LoginScreen
import com.example.drugdose.ui.screens.prescriptions.PrescriptionsViewModel
import com.example.drugdose.ui.screens.prescriptions.PrescrizioniScreen
import com.example.drugdose.ui.screens.register.RegisterScreen
import com.example.drugdose.ui.screens.search.DrugSearchScreen

// AppNavigation.kt
sealed class Screen(val route: String) {
    object Loading       : Screen("loading")
    object Login         : Screen("login")
    object Register : Screen("registrazione")
    object Home          : Screen("home")
    object DrugSearchList  : Screen("drugdose")
    object Creazione     : Screen("creazione/{farmacoId}") {
        fun createRoute(farmacoId: String) = "creazione/$farmacoId"
    }
    object Prescrizioni  : Screen("prescrizioni")
}

//TODO come gestire 3 viewModel in uno screen
@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Loading.route) {
            LoadingScreen(
                onIniziamoClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true } // Rimuove LoadingScreen dalla backstack, non si torna indietro
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccesso = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } //Rimuove tutto cio che c'era prima
                    }
                },
                onVaiRegistrazione = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistrazioneSuccesso = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onVaiLogin = { navController.popBackStack() } //semplice indietro
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onMenuItemClick = { menuItem ->
                    when (menuItem.action) {
                        HomeAction.DRUG_DOSE -> navController.navigate(Screen.DrugSearchList.route)
                        HomeAction.PLACEHOLDER -> { /* non fa nulla per ora */ }
                    }
                },
                onSessioneNonValida = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onVaiAPrescrizioniClick = {
                    navController.navigate(Screen.Prescrizioni.route)
                } ,

                onLogoutClick = {
                    navController.navigate(Screen.Loading.route) {
                        popUpTo(Screen.Loading.route) { inclusive = false }
                    }
                }
            )
        }

        // ECCEZIONE: Il ViewModel viene legato al ciclo di vita della Home (Screen.Home.route)
        // invece che a quello di questa destinazione. Questo permette di navigare avanti e indietro
        // tra Home e Prescrizioni senza perdere i dati caricati e lo stato della UI.
        composable(Screen.Prescrizioni.route){
            val viewModel: PrescriptionsViewModel = viewModel(
                //Assegno l'owner del BackStack di PrescrizioniViewModel a Home, per non uccidere il vecchio ViewModel (Home)
                viewModelStoreOwner = navController.getBackStackEntry(Screen.Home.route),
                factory = ViewModelFactory()
            )
            PrescrizioniScreen( //Inietto il viewModel creato da qui,
                viewModel = viewModel,
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.DrugSearchList.route) {
            DrugSearchScreen(
                onBack = { navController.popBackStack() },
                onCreaPrescrizione = { farmaco ->
                    // Passiamo solo l'id (String) — niente IllegalArgumentException
                    // su SavedStateHandle, perché Farmaco non è Parcelable.
                    navController.navigate(Screen.Creazione.createRoute(farmaco.id))
                }
            )
        }

        composable(
            route = Screen.Creazione.route,
            arguments = listOf(navArgument("farmacoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val farmacoId = backStackEntry.arguments?.getString("farmacoId")

            if (farmacoId != null) {
                CreatePrescriptionScreen(
                    farmacoId = farmacoId,
                    onBack = { navController.popBackStack() },
                    onPrescrizioneCreata = {
                        // Naviga a Prescrizioni e pulisce lo stack fino a DrugSearchList
                        navController.navigate(Screen.Prescrizioni.route) {
                            popUpTo(Screen.DrugSearchList.route) { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}