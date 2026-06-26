package com.example.drugdose

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.drugdose.di.ViewModelFactory
import com.example.drugdose.ui.components.ProfileDropdownMenu
import com.example.drugdose.ui.screens.home.HomeAction
import com.example.drugdose.ui.screens.create.CreatePrescriptionScreen
import com.example.drugdose.ui.screens.home.HomeScreen
import com.example.drugdose.ui.screens.loading.LoadingScreen
import com.example.drugdose.ui.screens.login.LoginScreen
import com.example.drugdose.ui.screens.prescriptions.PrescriptionsViewModel
import com.example.drugdose.ui.screens.register.RegisterScreen
import com.example.drugdose.ui.screens.search.DrugSearchScreen
import com.example.drugdose.ui.components.MainTab
import com.example.drugdose.ui.screens.main.MainScreen

// AppNavigation.kt
sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object Login : Screen("login")
    object Register : Screen("registrazione")
    object DrugSearchList : Screen("drugdose")

    object Creazione : Screen("creazione/{farmacoId}") {
        fun createRoute(farmacoId: String) = "creazione/$farmacoId"
    }

    object Main : Screen("main?tab={tab}") {
        fun createRoute(tab: String = "home") = "main?tab=$tab"
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
                        popUpTo(Screen.Login.route) { inclusive = true }
                    } },
                onVaiRegistrazione = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistrazioneSuccesso = {
                    navController.navigate(Screen.Main.createRoute()) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onVaiLogin = { navController.popBackStack() }
            )
        }
        // composable Main — legge il tab dall'argomento
        composable(
            route = Screen.Main.route,
            arguments = listOf(
                navArgument("tab") {
                    type = NavType.StringType
                    defaultValue = "home"
                }
            )
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
            val farmacoId = backStackEntry.arguments?.getString("farmacoId")
            if (farmacoId != null) {
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
}