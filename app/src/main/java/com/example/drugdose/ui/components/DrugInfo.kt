package com.example.drugdose.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drugdose.data.model.Farmaco
import com.example.drugdose.data.model.Rcp
import com.example.drugdose.ui.theme.DrugDoseTheme

@Composable
fun DrugInfo(
    farmaco: Farmaco,
    onDismiss: () -> Unit,
    onCreaPrescrizione: (Farmaco) -> Unit,
    onApriLink: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false) // ← card a tutta larghezza, non il default ristretto
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp),
            modifier = modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.78f) // ← card fissa al centro, non a tutto schermo
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // HEADER FISSO — non scrolla
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = farmaco.nome,
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Chiudi",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = farmaco.nomeCommerciale,
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = farmaco.tipoFormula,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // CONTENUTO SCROLLABILE — weight(1f) occupa lo spazio rimanente tra header e bottone
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item {
                        DetailSection(
                            title = "Indicazioni:",
                            content = farmaco.indicazione
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

                    item {
                        DetailSection(
                            title = "Età minima:",
                            content = farmaco.etaMinimaAnni?.let { "$it anni" } ?: "Non specificata"
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

                    item {
                        DetailSection(
                            title = "Dosaggio:",
                            content = buildString {
                                farmaco.doseMinimaMg?.let { append("Min: dose minima $it mg") }
                                if (farmaco.doseMinimaMg != null && farmaco.doseMassimaMg != null) append("   ")
                                farmaco.doseMassimaMg?.let { append("Max: dose massima $it mg") }
                                if (farmaco.doseMinimaMg == null && farmaco.doseMassimaMg == null) append("Non specificato")
                            }
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


                    item {
                        DetailSection(
                            title = "Controindicazioni:",
                            content = farmaco.rcp?.controindicazioni?.takeIf { it.isNotEmpty() }
                                ?.joinToString("\n\n") { "• $it" } ?: "Non specificate."
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


                    item {
                        DetailSection(
                            title = "Interazioni",
                            content = farmaco.rcp?.interazioni?.takeIf { it.isNotEmpty() }
                                ?.joinToString("\n\n") { "• $it" } ?: "Non specificate."
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

                    item {
                        DetailSection(
                            title = "Avvertenze speciali:",
                            content = farmaco.rcp?.avvertenzeSpeciali?.takeIf { it.isNotEmpty() }
                                ?.joinToString("\n\n") { "• $it" } ?: "Non specificate."
                        )
                    }

                    if (farmaco.fonteRcp.isNotBlank()) {

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
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = "Fonte:",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = farmaco.fonteRcp,
                                    style = TextStyle(
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    modifier = Modifier.clickable { onApriLink(farmaco.fonteRcp) }
                                )
                            }
                        }
                    }

                    item { Spacer(Modifier.height(4.dp)) }
                }

                // BOTTONE FISSO — fuori dalla LazyColumn, sempre visibile
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = { onCreaPrescrizione(farmaco) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Crea Prescrizione",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color =  MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = content,
            style = TextStyle(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrugInfoPreview() {
    val sampleFarmaco = Farmaco(
        id = "1",
        nome = "Paracetamolo",
        nomeCommerciale = "Tachipirina",
        indicazione = "Trattamento sintomatico dell'influenza, delle malattie esantematiche, degli stati febbrili di qualunque tipo, delle affezioni algiche di lieve e media entità di varia origine.",
        tipoFormula = "Gocce",
        etaMinimaAnni = 0,
        doseMinimaMg = 10.0,
        doseMassimaMg = 15.0,
        alerts = listOf("Non superare le dosi consigliate", "L'uso prolungato può causare danni al fegato"),
        fonteRcp = "https://farmaci.agenziafarmaco.gov.it/bancadatifarmaci/",
        rcp = Rcp(
            controindicazioni = listOf("Ipersensibilità al principio attivo", "Grave anemia emolitica"),
            interazioni = listOf("Anticoagulanti", "Zidovudina"),
            avvertenzeSpeciali = listOf("Ciao", "ciaodnj")
        )
    )

    DrugDoseTheme {
        DrugInfo(
            farmaco = sampleFarmaco,
            onDismiss = { },
            onCreaPrescrizione = { }
        )
    }
}