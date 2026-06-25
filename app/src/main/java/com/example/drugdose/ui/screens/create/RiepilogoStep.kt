package com.example.drugdose.ui.screens.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun RiepilogoStep(
    formState: PrescriptionFormState,
    modifier: Modifier = Modifier
) {
    // leggi i colori qui, dove MaterialTheme è accessibile
    val colorOnSurface = MaterialTheme.colorScheme.onSurface
    val colorPrimary = MaterialTheme.colorScheme.primary

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface   // ← era Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Riepilogo Prescrizione",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorOnSurface   // ← era Color.Black
                )
            )

            val nomeCompleto = listOf(formState.nome, formState.cognome)
                .filter { it.isNotBlank() }
                .joinToString(" ")
                .ifBlank { "—" }

            RiepilogoLine(label = "Paziente: ", value = nomeCompleto, colorLabel = colorOnSurface, colorValue = colorPrimary)
            RiepilogoLine(label = "CF: ", value = formState.codiceFiscale.ifBlank { "—" }, colorLabel = colorOnSurface, colorValue = colorPrimary)
            RiepilogoLine(
                label = "",
                value = buildAnnotatedString {
                    appendBold("Età: ", colorOnSurface)
                    appendValue(formState.etaAnni.ifBlank { "—" } + " anni", colorPrimary)
                    append(" | ")
                    appendBold("Peso: ", colorOnSurface)
                    appendValue(formState.pesoKg.ifBlank { "—" } + " kg", colorPrimary)
                    append(" | ")
                    appendBold("Altezza: ", colorOnSurface)
                    appendValue(formState.altezzaCm.ifBlank { "—" } + " cm", colorPrimary)
                }
            )
            RiepilogoLine(label = "Farmaco: ", value = formState.farmaco?.nome ?: "—", colorLabel = colorOnSurface, colorValue = colorPrimary)
            RiepilogoLine(
                label = "",
                value = buildAnnotatedString {
                    appendBold("Dose esatta: ", colorOnSurface)
                    appendValue(formState.doseEsattaMg?.let { "${"%.0f".format(it)} mg" } ?: "– mg", colorPrimary)
                    append(" | ")
                    appendBold("Dose unitaria: ", colorOnSurface)
                    appendValue(formState.numeroUnitaTesto ?: "– u", colorPrimary)
                }
            )
            RiepilogoLine(label = "Frequenza: ", value = formState.frequenza.ifBlank { "—" }, colorLabel = colorOnSurface, colorValue = colorPrimary)
            RiepilogoLine(label = "Note: ", value = formState.note.ifBlank { "—" }, colorLabel = colorOnSurface, colorValue = colorPrimary)
        }
    }
}

@Composable
private fun RiepilogoLine(
    label: String,
    value: String,
    colorLabel: Color,
    colorValue: Color
) {
    Text(
        text = buildAnnotatedString {
            appendBold(label, colorLabel)
            appendValue(value, colorValue)
        },
        style = TextStyle(fontSize = 14.sp)
    )
}

@Composable
private fun RiepilogoLine(label: String, value: AnnotatedString) {
    Text(text = value, style = TextStyle(fontSize = 14.sp))
}

private fun AnnotatedString.Builder.appendBold(text: String, color: Color) {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = color)) {
        append(text)
    }
}

private fun AnnotatedString.Builder.appendValue(text: String, color: Color) {
    withStyle(SpanStyle(color = color)) {
        append(text)
    }
}