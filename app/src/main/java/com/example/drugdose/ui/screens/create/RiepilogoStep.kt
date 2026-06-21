package com.example.drugdose.ui.screens.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            )

            val nomeCompleto = listOf(formState.nome, formState.cognome)
                .filter { it.isNotBlank() }
                .joinToString(" ")
                .ifBlank { "—" }

            RiepilogoLine(label = "Paziente: ", value = nomeCompleto)
            RiepilogoLine(label = "CF: ", value = formState.codiceFiscale.ifBlank { "—" })
            RiepilogoLine(
                label = "",
                value = buildAnnotatedString {
                    appendBold("Età: ")
                    appendValue(formState.etaAnni.ifBlank { "—" } + " anni")
                    append(" | ")
                    appendBold("Peso: ")
                    appendValue(formState.pesoKg.ifBlank { "—" } + " kg")
                    append(" | ")
                    appendBold("Altezza: ")
                    appendValue(formState.altezzaCm.ifBlank { "—" } + " cm")
                }
            )
            RiepilogoLine(label = "Farmaco: ", value = formState.farmaco?.nome ?: "—")
            RiepilogoLine(
                label = "",
                value = buildAnnotatedString {
                    appendBold("Dose esatta: ")
                    appendValue(formState.doseEsattaMg?.let { "${"%.0f".format(it)} mg" } ?: "– mg")
                    append(" | ")
                    appendBold("Dose unitaria: ")
                    appendValue(formState.doseUnitaria ?: "– u")
                }
            )
            RiepilogoLine(label = "Frequenza: ", value = formState.frequenza.ifBlank { "—" })
            RiepilogoLine(label = "Note: ", value = formState.note.ifBlank { "—" })
        }
    }
}

@Composable
private fun RiepilogoLine(label: String, value: String) {
    Text(
        text = buildAnnotatedString {
            appendBold(label)
            appendValue(value)
        },
        style = TextStyle(fontSize = 14.sp)
    )
}

@Composable
private fun RiepilogoLine(label: String, value: AnnotatedString) {
    Text(text = value, style = TextStyle(fontSize = 14.sp))
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendBold(text: String) {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
        append(text)
    }
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendValue(text: String) {
    withStyle(SpanStyle(color = Color(0xFF6750A4))) {
        append(text)
    }
}