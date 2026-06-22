package com.example.drugdose.ui.screens.prescriptions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drugdose.R
import com.example.drugdose.data.model.Prescrizione
import com.example.drugdose.ui.components.PrescriptionCard
import com.example.drugdose.ui.components.PrescriptionInfo

// Eccezione: PrescrizioniViewModel non è iniettato dalla factory ma deve essere gestito in AppNavigation.kt per il backstack
// Così si può avere un backstack con dentro Home e Prescrizioni
@Composable
fun PrescrizioniScreen(
    modifier: Modifier = Modifier,
    viewModel: PrescriptionsViewModel,
    onHomeClick: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.refreshPrescrizioni()
    }

    val prescrizioni by viewModel.prescrizioniFiltrate.collectAsStateWithLifecycle()
    val filtroPaziente by viewModel.filtroPaziente.collectAsStateWithLifecycle()
    val filtroFarmaco by viewModel.filtroFarmaco.collectAsStateWithLifecycle()
    val filtroStatus by viewModel.filtroStatus.collectAsStateWithLifecycle()

    // Prescrizione selezionata per il popup (null = chiuso)
    var prescrizioneSelezionata by remember { mutableStateOf<Prescrizione?>(null) }

    // Box filtri aperto/chiuso — puramente visivo, non serve al ViewModel
    var filtriAperti by remember { mutableStateOf(false) }

    val filtriAttivi = filtroPaziente.isNotBlank() || filtroFarmaco.isNotBlank() || filtroStatus != FiltroStatus.TUTTI

    Surface(
        shape = RoundedCornerShape(44.dp),
        color = Color(0xFFF5F5F5),
        modifier = modifier
            .fillMaxSize()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(44.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Top bar — hamburger a sx, notifiche + avatar a dx (come mockup)
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.Black,
                modifier = Modifier
                    .padding(start = 23.dp, top = 42.dp)
                    .requiredSize(27.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 33.dp, end = 26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifiche",
                    tint = Color.Black,
                    modifier = Modifier.requiredSize(27.dp)
                )
                Box(
                    modifier = Modifier.requiredSize(45.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(BorderStroke(2.dp, Color.White), CircleShape)
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.requiredSize(30.dp)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .padding(top = 107.dp, bottom = 97.dp)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Titolo + icona filtro con badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Elenco prescrizioni",
                        color = Color.Black,
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    )

                    Box {
                        IconButton(onClick = { filtriAperti = !filtriAperti }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filtri",
                                tint = if (filtriAperti) MaterialTheme.colorScheme.primary else Color.Black,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        // pallino badge se almeno un filtro è attivo
                        if (filtriAttivi) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 6.dp, end = 6.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }

                // Riga risultati — sempre visibile, sopra ai filtri collassabili
                Text(
                    text = "Trovate ${prescrizioni.size} prescrizioni",
                    style = TextStyle(fontSize = 13.sp),
                    color = Color(0xFF5F6368),
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Box filtri — collassabile
                AnimatedVisibility(
                    visible = filtriAperti,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "Dati Paziente",
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                                color = Color.Black
                            )
                            OutlinedTextField(
                                value = filtroPaziente,
                                onValueChange = { viewModel.onFiltroPazienteChange(it) },
                                placeholder = {
                                    Text("Cerca per dati paziente ...", color = Color(0xFFAAAAAA), fontSize = 14.sp)
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Nome Farmaco",
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                                color = Color.Black
                            )
                            OutlinedTextField(
                                value = filtroFarmaco,
                                onValueChange = { viewModel.onFiltroFarmacoChange(it) },
                                placeholder = {
                                    Text("Cerca per farmaco...", color = Color(0xFFAAAAAA), fontSize = 14.sp)
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Status",
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                                color = Color.Black
                            )
                            StatusDropdown(
                                selected = filtroStatus,
                                onSelected = { viewModel.onFiltroStatusChange(it) }
                            )
                        }
                    }
                }

                // Lista prescrizioni — occupa tutto lo spazio rimanente
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = prescrizioni, key = { it.id }) { prescrizione ->
                        PrescriptionCard(
                            prescrizione = prescrizione,
                            onClick = { prescrizioneSelezionata = prescrizione }
                        )
                    }

                    item { Spacer(Modifier.height(8.dp)) }
                }
            }

            // Bottom Navigation Bar — identica a HomeScreen, "Prescriptions" evidenziato
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
                    .background(Color.White)
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.clickable { onHomeClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color(0xFF5F6368),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Home",
                        color = Color(0xFF5F6368),
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
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Prescriptions",
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

    // Popup dettaglio
    prescrizioneSelezionata?.let { prescrizione ->
        PrescriptionInfo(
            prescrizione = prescrizione,
            onDismiss = { prescrizioneSelezionata = null },
            onAnnulla = { id ->
                viewModel.annullaPrescrizione(id)
                prescrizioneSelezionata = null
            }
        )
    }
}

@Composable
private fun StatusDropdown(
    selected: FiltroStatus,
    onSelected: (FiltroStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledTrailingIconColor = Color.Black
            ),
            enabled = false
        )

        // overlay clickable — il campo è disabled, serve per intercettare il tap
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            FiltroStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.label) },
                    onClick = {
                        onSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun PrescrizioniScreenPreview() {
    PrescrizioniScreen(
        viewModel = viewModel()
    )
}