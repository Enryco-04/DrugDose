package com.example.drugdose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.drugdose.ui.screens.home.HomeAction
import com.example.drugdose.ui.screens.create.CreatePrescriptionScreen
import com.example.drugdose.ui.screens.loading.LoadingScreen
import com.example.drugdose.ui.screens.login.LoginScreen
import com.example.drugdose.ui.screens.register.RegisterScreen
import com.example.drugdose.ui.screens.search.DrugSearchScreen
import com.example.drugdose.ui.components.MainTab
import com.example.drugdose.ui.screens.main.MainScreen


// AppNavigation.kt

//Mini classe per gestire le route (stringhe)
sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object Login : Screen("login")
    object Register : Screen("registrazione")
    object DrugSearchList : Screen("drugdose")

    object Creazione : Screen("creazione/{farmacoId}") {

        //Utili per passare parametri tra le route
        fun createRoute(farmacoId: String) = "creazione/$farmacoId"
        fun farmacoId(backStackEntry: NavBackStackEntry) =
            backStackEntry.arguments?.getString("farmacoId")
    }

    object Main : Screen("main?tab={tab}") {
        fun createRoute(tab: String = "home") = "main?tab=$tab"
        fun initialTab(backStackEntry: NavBackStackEntry): MainTab {
            val tab = backStackEntry.arguments?.getString("tab")
            return if (tab == "prescrizioni") MainTab.PRESCRIZIONI else MainTab.HOME
        }
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavigation() {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Loading.route) {
            LoadingScreen(
                onIniziamoClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccesso = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                                  },
                onVaiRegistrazione = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistrazioneSuccesso = {
                    navController.navigate(Screen.Main.createRoute()) {
                        popUpTo(0) { inclusive = true }

                    }
                },
                onVaiLogin = { navController.popBackStack() }
            )
        }
        // composable Main legge il tab dall'argomento
        composable(
            route = Screen.Main.route,
            arguments = listOf(navArgument("tab") { defaultValue = "home" })
        ) { backStackEntry ->
            val tabArg = backStackEntry.arguments?.getString("tab")
            val initialTab = if (tabArg == "prescrizioni") MainTab.PRESCRIZIONI else MainTab.HOME
            MainScreen(
                initialTab = initialTab,
                onMenuItemClick = { menuItem ->
                    when (menuItem.action) {
                        HomeAction.DRUG_DOSE -> navController.navigate(Screen.DrugSearchList.route)
                        HomeAction.PLACEHOLDER -> { /* non fa nulla per ora */ }
                    }
                },
                onLogoutClick = {
                    navController.navigate(Screen.Loading.route) {
                        popUpTo(Screen.Loading.route) { inclusive = false }
                    }
                },
                onSessioneNonValida = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }

            )
        }


        composable(Screen.DrugSearchList.route) {
            DrugSearchScreen(
                onBack = { navController.popBackStack() },
                onCreaPrescrizione = { farmaco ->
                    navController.navigate(Screen.Creazione.createRoute(farmaco.id)) },
                onLogoutClick = {
                    navController.navigate(Screen.Loading.route) {
                        popUpTo(Screen.Loading.route) { inclusive = false }
                    }
                }
            )
        }



        composable(
            route = Screen.Creazione.route,
            arguments = listOf(navArgument("farmacoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val farmacoId = Screen.Creazione.farmacoId(backStackEntry) ?: return@composable
                CreatePrescriptionScreen(
                    farmacoId = farmacoId,
                    onBack = { navController.popBackStack() },
                    onLogoutClick = {
                        navController.navigate(Screen.Loading.route) {
                            popUpTo(Screen.Loading.route) { inclusive = false } }
                                    },
                    onPrescrizioneCreata = {
                        navController.navigate(Screen.Main.createRoute("prescrizioni")) {
                            popUpTo(Screen.DrugSearchList.route) { inclusive = true }
                        }
                    }
                )
            }


    }
}
