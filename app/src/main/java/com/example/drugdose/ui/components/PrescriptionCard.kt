package com.example.drugdose.ui.components
import com.google.firebase.Timestamp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drugdose.data.model.PazienteEmbedded
import com.example.drugdose.data.model.Prescrizione
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import java.text.SimpleDateFormat
import java.util.Locale
@Composable
fun PrescriptionCard(
    prescrizione: Prescrizione,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // Convertitore
    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    fun formatDate(timestamp: Timestamp?): String {
        return timestamp?.toDate()?.let {
            dateFormatter.format(it)
        } ?: "-"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = prescrizione.nomeFarmaco,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${prescrizione.dosaggioMg.toInt()}mg - ${prescrizione.frequenza ?: ""}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            StatusBadge(
                stato = prescrizione.statoVisualizzato()
            )
        }

        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "Paziente: ${prescrizione.paziente.nome} ${prescrizione.paziente.cognome}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "CF: ${prescrizione.paziente.codiceFiscale}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Prescrizione: ${formatDate(prescrizione.dataCreazione)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Scadenza: ${formatDate(prescrizione.dataScadenza)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrescriptionCardPreview() {

    PrescriptionCard(
        prescrizione = Prescrizione(

            id = "prescrizione_001",

            idMedico = "medico_123",

            status = "ATTIVA",

            dataCreazione = Timestamp.now(),

            dataScadenza = Timestamp.now(),

            annullatoIl = null,


            paziente = PazienteEmbedded(
                nome = "Mario",
                cognome = "Rossi",
                codiceFiscale = "RSSMRA80A01H501U"
            ),


            idFarmaco = "AMOX_500",

            nomeFarmaco = "Amoxicillina",

            nomeCommercialeFarmaco = "Zimox",


            dosaggioMg = 500.0,

            formaFarmaceuticaSomministrazione = "Compresse",

            numeroUnitaSomministrazione = 1,

            quantita = 30,


            note = "Assumere dopo i pasti",

            frequenza = "3 volte al giorno"
        )
    )
}