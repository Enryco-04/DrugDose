package com.example.drugdose.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.res.painterResource
import com.example.drugdose.R
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.drugdose.ui.model.HomeMenuItem
import com.example.drugdose.ui.screens.login.LoginViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory()),
    onMenuItemClick: (HomeMenuItem) -> Unit = {},
    onSessioneNonValida: () -> Unit = {}

) {
    LaunchedEffect(viewModel.sessioneNonValida) {
        if (viewModel.sessioneNonValida) onSessioneNonValida()
    }
    val menuItems by viewModel.menuItems.collectAsStateWithLifecycle()
    val pages = menuItems.chunked(9)

    val pagerState = rememberPagerState(
        pageCount = { pages.size.coerceAtLeast(1) }
    )

    Surface(
        shape = RoundedCornerShape(44.dp),
        color = Color(0xFFF5F5F5),
        modifier = modifier
            .fillMaxSize()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(44.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(top = 33.dp, end = 26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifiche",
                    tint = Color.Black,
                    modifier = Modifier.requiredSize(size = 27.dp)
                )
                Box(
                    modifier = Modifier.requiredSize(size = 45.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.primary)
                            .border(border = BorderStroke(2.dp, Color.White), shape = CircleShape)
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.requiredSize(size = 30.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.Black,
                modifier = Modifier
                    .padding(start = 23.dp, top = 42.dp)
                    .requiredSize(size = 27.dp)
            )

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
                        color = Color.Black,
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "Cosa vuole fare oggi?",
                        color = Color.Black,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }

                // Cards Section — dinamica tramite LazyRow
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
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
                                        Color(0xFFD9D9D9)
                                )
                        )
                    }
                }
            }

            // Bottom Navigation Bar
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(0.9f)
                    .height(77.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(40.dp))
                    .clip(shape = RoundedCornerShape(40.dp))
                    .background(color = Color.White)
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Home",
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_stethoscope),
                        contentDescription = "Prescriptions",
                        tint = Color(0xFF5F6368),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Prescriptions",
                        color = Color(0xFF5F6368),
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    )
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