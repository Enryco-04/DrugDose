package com.example.drugdose.ui.screens.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FarmacoStep(
    formState: PrescriptionFormState,
    onFrequenzaChange: (String) -> Unit,
    onNumeroConfezioniChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val farmaco = formState.farmaco

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card: Farmaco selezionato
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Farmaco Selezionato",
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = farmaco?.tipoFormula ?: "Tipo_calcolo",
                            style = TextStyle(fontSize = 12.sp, color = Color(0xFF6750A4), fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                LabelValueRow(label = "Principio Attivo:", value = farmaco?.nome ?: "—")
                LabelValueRow(label = "Nome Commerciale:", value = farmaco?.nomeCommerciale ?: "—")
            }
        }

        // Card: Dosaggio (placeholder calcolato dal ViewModel)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Dosaggio",
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                )
                LabelValueRow(
                    label = "Dose esatta:",
                    value = formState.doseEsattaMg?.let { "${"%.1f".format(it)} mg" } ?: "– mg"
                )
                LabelValueRow(
                    label = "Dose arrotondata:",
                    value = formState.doseArrotondataMg?.let { "${it.toInt()} mg" } ?: "– mg"
                )
                LabelValueRow(
                    label = "Conversione:",
                    value = formState.numeroUnitaTesto ?: "– u"
                )
                formState.erroriCalcolo?.forEach { errore ->
                    Text(
                        text = errore,
                        style = TextStyle(fontSize = 13.sp, color = Color(0xFFC62828)),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        FormCard(title = "Frequenza") {
            OutlinedTextField(
                value = formState.frequenza,
                onValueChange = onFrequenzaChange,
                placeholder = { Text("es. due volte al giorno", style = fieldPlaceholderStyle()) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        FormCard(title = "N° confezioni") {
            OutlinedTextField(
                value = formState.numeroConfezioni,
                onValueChange = { if (it.all { c -> c.isDigit() }) onNumeroConfezioniChange(it) },
                placeholder = { Text("es. 2, 3, …", style = fieldPlaceholderStyle()) },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        FormCard(title = "Note") {
            OutlinedTextField(
                value = formState.note,
                onValueChange = onNoteChange,
                placeholder = { Text("es. assumere a stomaco pieno", style = fieldPlaceholderStyle()) },
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                minLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            )
        }
    }
}

@Composable
private fun LabelValueRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 14.sp, color = Color(0xFF625B71))
        )
        Text(
            text = value,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        )
    }
}