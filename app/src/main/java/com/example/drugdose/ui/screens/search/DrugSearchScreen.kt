package com.example.drugdose.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drugdose.R
import com.example.drugdose.data.model.Farmaco
import com.example.drugdose.di.ViewModelFactory
import com.example.drugdose.ui.components.DrugCard
import com.example.drugdose.ui.components.DrugInfo
import com.example.drugdose.ui.components.ProfileDropdownMenu

@Composable
fun DrugSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: DrugSearchViewModel = viewModel(factory = ViewModelFactory()),
    onBack: () -> Unit = {},
    onCreaPrescrizione: (Farmaco) -> Unit = {},
    onLogoutClick: () -> Unit,
) {
    val farmaci by viewModel.farmacoFiltrati.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // Farmaco selezionato per il popup (null = chiuso)
    var farmacoSelezionato by remember { mutableStateOf<Farmaco?>(null) }

    var profileMenuExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Surface(
            shape = RoundedCornerShape(44.dp),
            color = Color(0xFFF5F5F5),
            modifier = modifier
                .fillMaxSize()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(44.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp)
            ) {

                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 40.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                ) {

                    // Pulsante Home
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(50.dp)
                            .clip(CircleShape)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape
                            )
                            .background(MaterialTheme.colorScheme.onPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_home),
                                contentDescription = "Home",
                                tint = Color.Unspecified,
                                modifier = Modifier.requiredSize(40.dp)
                            )
                        }
                    }

                    // Titolo
                    Text(
                        text = "DrugDose",
                        style = TextStyle(
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Titolo sezione
                Text(
                    text = "Seleziona un Farmaco:",
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 8.dp
                    )
                )

                // Lista farmaci
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        Spacer(Modifier.height(4.dp))
                    }

                    items(
                        items = farmaci,
                        key = { it.id }
                    ) { farmaco ->

                        DrugCard(
                            nomeFarmaco = farmaco.nome,
                            nomeCommerciale = farmaco.nomeCommerciale,
                            indicazione = farmaco.indicazione,
                            tipoCalcolo = farmaco.tipoFormula,
                            onClick = {
                                farmacoSelezionato = farmaco
                            }
                        )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // Search bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        )
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.White)
                ) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cerca",
                        tint = Color(0xFF625B71),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(22.dp)
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            viewModel.onSearchQueryChange(it)
                        },
                        placeholder = {
                            Text(
                                text = "Cerca un farmaco",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color(0xFFAAAAAA)
                                )
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Popup con i dettagli del farmaco selezionato
        farmacoSelezionato?.let { farmaco ->
            DrugInfo(
                farmaco = farmaco,
                onDismiss = { farmacoSelezionato = null },
                onCreaPrescrizione = { selezionato ->
                    onCreaPrescrizione(selezionato)
                    farmacoSelezionato = null
                }
            )
        }
        // MENU PROFILO FUORI DALLA SURFACE
        ProfileDropdownMenu(
            expanded = profileMenuExpanded,
            onAvatarClick = {
                profileMenuExpanded = !profileMenuExpanded
            },
            onDismiss = {
                profileMenuExpanded = false
            },
            onLogoutClick = {
                profileMenuExpanded = false
                onLogoutClick()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 40.dp,
                    end = 16.dp
                )
        )

    }
}