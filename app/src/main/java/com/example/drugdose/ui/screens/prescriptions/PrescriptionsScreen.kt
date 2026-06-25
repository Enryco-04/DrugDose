package com.example.drugdose.ui.screens.prescriptions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.drugdose.data.model.Prescrizione
import com.example.drugdose.ui.components.PrescriptionCard
import com.example.drugdose.ui.components.PrescriptionInfo

@Composable
fun PrescrizioniScreen(
    modifier: Modifier = Modifier,
    viewModel: PrescriptionsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.refreshPrescrizioni()
    }

    val prescrizioni by viewModel.prescrizioniFiltrate.collectAsStateWithLifecycle()
    val filtroPaziente by viewModel.filtroPaziente.collectAsStateWithLifecycle()
    val filtroFarmaco by viewModel.filtroFarmaco.collectAsStateWithLifecycle()
    val filtroStatus by viewModel.filtroStatus.collectAsStateWithLifecycle()

    var prescrizioneSelezionata by remember { mutableStateOf<Prescrizione?>(null) }
    var filtriAperti by remember { mutableStateOf(false) }

    val filtriAttivi = filtroPaziente.isNotBlank() || filtroFarmaco.isNotBlank() || filtroStatus != FiltroStatus.TUTTI

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 107.dp, bottom = 97.dp)
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Elenco prescrizioni",
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
            )

            Box {
                IconButton(onClick = { filtriAperti = !filtriAperti }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtri",
                        tint = if (filtriAperti) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(26.dp)
                    )
                }
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

        Text(
            text = "Trovate ${prescrizioni.size} prescrizioni",
            style = TextStyle(fontSize = 13.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

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
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column {
                    Text(
                        text = "Dati Paziente",
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    OutlinedTextField(
                        value = filtroPaziente,
                        onValueChange = { viewModel.onFiltroPazienteChange(it) },
                        placeholder = {
                            Text("Cerca per dati paziente ...", color = MaterialTheme.colorScheme.outline, fontSize = 14.sp)
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    OutlinedTextField(
                        value = filtroFarmaco,
                        onValueChange = { viewModel.onFiltroFarmacoChange(it) },
                        placeholder = {
                            Text("Cerca per farmaco...", color = MaterialTheme.colorScheme.outline, fontSize = 14.sp)
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    StatusDropdown(
                        selected = filtroStatus,
                        onSelected = { viewModel.onFiltroStatusChange(it) }
                    )
                }
            }
        }

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
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onBackground
            ),
            enabled = false
        )

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