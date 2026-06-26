package com.example.drugdose.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.drugdose.ui.components.AppBottomBar
import com.example.drugdose.ui.components.MainTab
import com.example.drugdose.ui.components.ProfileDropdownMenu
import com.example.drugdose.ui.screens.home.HomeScreen
import com.example.drugdose.ui.screens.home.HomeMenuItem
import com.example.drugdose.ui.screens.prescriptions.PrescriptionsScreen

@Composable
fun MainScreen(
    initialTab: MainTab = MainTab.HOME,
    onMenuItemClick: (HomeMenuItem) -> Unit = {}, //Queste funzioni vengono da AppNav e si passano ai rispettivi HomeScreen e PrescriptionsScreen
    onLogoutClick: () -> Unit = {},
    onSessioneNonValida: () -> Unit = {}
) {
    //Questa variabile gestisce la navigazione interna, AnimatedContent quando cambia questa variabile cambia il contenuto
    var currentTab by remember { mutableStateOf(initialTab) }
    var profileMenuExpanded by remember { mutableStateOf(false) }

    BackHandler { /*Intercetta il backHandler, (non fa niente) */ }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentTab,
            transitionSpec = {
                val isForward = targetState.ordinal > initialState.ordinal
                if (isForward) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(300)
                            )
                } else {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(300)
                            )
                }
            },
            modifier = Modifier.fillMaxSize(),
            label = "tab_transition"
        ) { tab ->
            when (tab) {
                MainTab.HOME -> HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onMenuItemClick = onMenuItemClick,
                    onSessioneNonValida = onSessioneNonValida
                )
                MainTab.PRESCRIZIONI -> PrescriptionsScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        // Hamburger — posizione fissa, non viene influenzato dal dropdown
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 23.dp, top = 9.dp)
                .requiredSize(27.dp)
                .zIndex(1f)
        )

        // ProfileDropdownMenu — libero di espandersi verso il basso
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 9.dp, end = 26.dp)
                .zIndex(1f)
        ) {
            ProfileDropdownMenu(
                expanded = profileMenuExpanded,
                onAvatarClick = { profileMenuExpanded = !profileMenuExpanded },
                onDismiss = { profileMenuExpanded = false },
                onLogoutClick = {
                    profileMenuExpanded = false
                    onLogoutClick()
                }
            )
        }
        //La navigazione interna è gestita da qui, AppBottomBar definisce le funzioni
        // cambiando currentTab, che fa cambiare il contenuto animato

        AppBottomBar(
            currentTab = currentTab,
            onHomeClick = { currentTab = MainTab.HOME },
            onPrescrizioniClick = { currentTab = MainTab.PRESCRIZIONI },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}