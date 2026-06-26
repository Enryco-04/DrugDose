package com.example.drugdose.ui.screens.home

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.res.painterResource
import com.example.drugdose.R
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drugdose.di.ViewModelFactory
import com.example.drugdose.ui.components.HomeCard  // ← importato da components
import com.example.drugdose.ui.components.ProfileDropdownMenu

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory()),
    onMenuItemClick: (HomeMenuItem) -> Unit = {},
    onSessioneNonValida: () -> Unit = {},
    onVaiAPrescrizioniClick: () -> Unit ={},
    onLogoutClick: () -> Unit = {}

) {
    LaunchedEffect(viewModel.sessioneNonValida) {
        if (viewModel.sessioneNonValida) onSessioneNonValida()
    }
    val menuItems by viewModel.menuItems.collectAsStateWithLifecycle()
    val pages = menuItems.chunked(9)

    val pagerState = rememberPagerState(
        pageCount = { pages.size.coerceAtLeast(1) }
    )
    // Stato del menu a discesa del profilo
    var profileMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {


            // Content
            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
                modifier = Modifier
                    .padding(top = 107.dp, bottom = 97.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                // Greeting
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
                ) {
                    Text(
                        text = "Benvenuto Dr. ${viewModel.medico?.cognome ?: ""}",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "Cosa vuole fare oggi?",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }

                // Cards Section ” dinamica tramite LazyRow
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                ) { page ->

                    val pageItems = pages.getOrElse(page) { emptyList() }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(Alignment.Top)
                    ) {
                        pageItems.chunked(3).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { menuItem ->
                                    HomeCard(
                                        title = menuItem.title,
                                        icon = menuItem.icon,
                                        onClick = { onMenuItemClick(menuItem) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                repeat(3 - rowItems.size) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // Page Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = index == pagerState.currentPage
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outlineVariant
                                )
                        )
                    }
                }
            }
        }
    }
}
@Preview(widthDp = 431, heightDp = 934)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}