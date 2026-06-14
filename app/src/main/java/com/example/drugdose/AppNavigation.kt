package com.example.drugdose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drugdose.ui.screens.loading.LoadingScreen
import com.example.drugdose.ui.screens.login.LoginScreen
import com.example.drugdose.ui.screens.register.RegisterScreen

// AppNavigation.kt
sealed class Screen(val route: String) {
    object Loading       : Screen("loading")
    object Login         : Screen("login")
    object Register : Screen("registrazione")
    object Home          : Screen("home")
    object DrugDoseList  : Screen("drugdose")
    object Creazione     : Screen("creazione/{farmacoId}")
    object Prescrizioni  : Screen("prescrizioni")
}
//TODO PER IL FUTURO, SERVE ViewModelFacotry che fa dependecy injection di AuthRepository, FarmaciRepository, PrescrizioniRepostiory
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Loading.route) {

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
                        popUpTo(Screen.Loading.route) { inclusive = true } //Rimuove tutto cio che c'era prima
                    }
                },
                onVaiRegistrazione = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistrazioneSuccesso = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                },
                onVaiLogin = { navController.popBackStack() } //semplice indietro
            )
        }
    }
}

