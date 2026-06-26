package com.example.drugdose.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.drugdose.data.model.PazienteEmbedded
import com.example.drugdose.data.model.Prescrizione
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PrescriptionInfo(
    prescrizione: Prescrizione,
    onDismiss: () -> Unit,
    onAnnulla: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Formatter per le date (da TimeStamp firestore a String)
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    fun formatDate(timestamp: Timestamp?) =
        timestamp?.toDate()?.let { formatter.format(it) } ?: "-"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp),
            modifier = modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.78f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header fisso
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dettagli Prescrizione",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Text(text = "✕", fontSize = 20.sp)
                        }
                    }
                }

                // Contenuto scrollabile (Tutte le prescrizioni, anche filtrate)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp) // gestiamo gli spazi manualmente
                ) {
                    // Farmaco e stato
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = prescrizione.nomeFarmaco,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            StatusBadge(stato = prescrizione.statoVisualizzato())
                        }
                    }

                    item {
                        Text(
                            text = prescrizione.nomeCommercialeFarmaco,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item {
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    //  Dosaggio
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoColumn("N° Confezioni", "${prescrizione.quantita} compresse")
                            InfoColumn("Frequenza", prescrizione.frequenza ?: "-")
                        }
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoColumn("Dose esatta", "${prescrizione.dosaggioMg} mg")
                            InfoColumn(
                                "Dose Unitaria",
                                "${prescrizione.numeroUnitaSomministrazione} pastiglia"
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item {
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    //  Dati paziente
                    item {
                        Text(
                            text = "Dati Paziente",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoColumn("Nome", prescrizione.paziente.nome)
                            InfoColumn("Cognome", prescrizione.paziente.cognome)
                        }
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    item {
                        InfoColumn("Codice Fiscale", prescrizione.paziente.codiceFiscale)
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item {
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    //  Date
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoColumn("Data Prescrizione", formatDate(prescrizione.dataCreazione))
                            InfoColumn("Data Scadenza", formatDate(prescrizione.dataScadenza))
                        }
                    }

                    // ---- Note (se presenti) ----
                    prescrizione.note?.let { note ->
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                        item {
                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        item {
                            InfoColumn("Note", note)
                        }
                    }

                    // Spazio finale per non far toccare il contenuto al bordo inferiore
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }

                //Button annulla solo se attiva
                if (prescrizione.statoVisualizzato() == "ATTIVA") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onAnnulla(prescrizione.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error,
                            )
                        ) {
                            Text("Annulla prescrizione")
                        }
                    }
                }
                // Se non è attiva, non mostriamo il bottone e la LazyColumn
                // occupa tutto lo spazio rimanente (per non avere nessuno spazio vuoto in basso)
            }
        }
    }
}

// Helper

@Composable
private fun InfoColumn(label: String, value: String) {
    Column(modifier = Modifier.width(150.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PrescriptionInfoPreview() {
    PrescriptionInfo(
        prescrizione = Prescrizione(
            id = "001",
            status = "ATTIVA",
            nomeFarmaco = "Amoxicillina",
            nomeCommercialeFarmaco = "Zimox",
            dosaggioMg = 500.0,
            formaFarmaceuticaSomministrazione = "Compresse",
            numeroUnitaSomministrazione = 1,
            quantita = 30,
            frequenza = "3 volte al giorno",
            note = "Assumere a stomaco pieno",
            dataCreazione = Timestamp.now(),
            dataScadenza = Timestamp.now(),
            paziente = PazienteEmbedded(
                nome = "Mario",
                cognome = "Rossi",
                codiceFiscale = "RSSMRA80A01H501U"
            )
        ),
        onDismiss = {},
        onAnnulla = {}
    )
}