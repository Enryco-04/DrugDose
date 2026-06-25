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


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Header e bottom bar fissi: solo su Home/Prescrizioni
    val showChrome = currentRoute == Screen.Home.route || currentRoute == Screen.Prescrizioni.route
    var profileMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(navController = navController, startDestination = Screen.Home.route) {

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
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
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
                    onVaiLogin = { navController.popBackStack() }
                )
            }

            // Scorrimento tra Home e Prescrizioni
            composable(
                route = Screen.Home.route,
                exitTransition = {
                    if (targetState.destination.route == Screen.Prescrizioni.route) {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                        )
                    } else null
                },
                popEnterTransition = {
                    if (initialState.destination.route == Screen.Prescrizioni.route) {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                        )
                    } else null
                }
            ) {
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
                    }
                )
            }

            composable(
                route = Screen.Prescrizioni.route,
                enterTransition = {
                    if (initialState.destination.route == Screen.Home.route) {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        )
                    } else null
                },
                popExitTransition = {
                    if (targetState.destination.route == Screen.Home.route) {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        )
                    } else null
                }
            ) {
                val viewModel: PrescriptionsViewModel = viewModel(
                    viewModelStoreOwner = navController.getBackStackEntry(Screen.Home.route),
                    factory = ViewModelFactory()
                )
                PrescrizioniScreen(viewModel = viewModel)
            }

            composable(Screen.DrugSearchList.route) {
                DrugSearchScreen(
                    onBack = { navController.popBackStack() },
                    onCreaPrescrizione = { farmaco ->
                        navController.navigate(Screen.Creazione.createRoute(farmaco.id))
                    },
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
                                popUpTo(Screen.Loading.route) { inclusive = false }
                            }
                        },
                        onPrescrizioneCreata = {
                            navController.navigate(Screen.Prescrizioni.route) {
                                popUpTo(Screen.DrugSearchList.route) { inclusive = false }
                            }
                        }
                    )
                }
            }
        }

        // CHROME FISSO: menu, profilo, bottom bar — solo su Home/Prescrizioni
        if (showChrome) {

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 23.dp, top = 42.dp)
                    .requiredSize(27.dp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 33.dp, end = 26.dp)
            ) {
                ProfileDropdownMenu(
                    expanded = profileMenuExpanded,
                    onAvatarClick = { profileMenuExpanded = !profileMenuExpanded },
                    onDismiss = { profileMenuExpanded = false },
                    onLogoutClick = {
                        profileMenuExpanded = false
                        navController.navigate(Screen.Loading.route) {
                            popUpTo(Screen.Loading.route) { inclusive = false }
                        }
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(0.9f)
                    .height(77.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(40.dp))
                    .clip(RoundedCornerShape(40.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (currentRoute == Screen.Home.route)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Home",
                        color = if (currentRoute == Screen.Home.route)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Alignment.Center.let { Arrangement.Center },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Prescrizioni.route) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_stethoscope),
                        contentDescription = "Prescriptions",
                        tint = if (currentRoute == Screen.Prescrizioni.route)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Prescriptions",
                        color = if (currentRoute == Screen.Prescrizioni.route)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}