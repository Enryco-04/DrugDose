package com.example.drugdose.ui.components
import com.google.firebase.Timestamp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drugdose.data.model.PazienteEmbedded
import com.example.drugdose.data.model.Prescrizione
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PrescriptionCard(
    prescrizione: Prescrizione,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}        // ← aggiungi questo parametro


) {
    //ToDO capire come passare il change nel ViewModel forse
    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    fun formatDate(timestamp: Timestamp?): String {
        return timestamp?.toDate()?.let {
            dateFormatter.format(it)
        } ?: "-"
    }

    //FIne TODO
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .clickable { onClick() }   // ← aggiungi questa riga, dopo .background()

    ) {

        // Nome farmaco + stato
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = prescrizione.nomeFarmaco,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "${prescrizione.dosaggioMg.toInt()}mg - ${prescrizione.frequenza ?: ""}",
                    fontSize = 14.sp,
                    color = Color(0xff4a5565)
                )
            }


            StatusBadge(
                stato = prescrizione.status
            )
        }


        // Paziente
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {

            Text(
                text = "Paziente: ${prescrizione.paziente.nome} ${prescrizione.paziente.cognome}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "CF: ${prescrizione.paziente.codiceFiscale}",
                fontSize = 14.sp,
                color = Color(0xff4a5565)
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {Text(
                text = "Prescrizione: ${formatDate(prescrizione.dataCreazione)}",
                fontSize = 12.sp,
                color = Color(0xff6a7282)
            )

                Text(
                    text = "Scadenza: ${formatDate(prescrizione.dataScadenza)}",
                    fontSize = 12.sp,
                    color = Color(0xff6a7282)
                )
            }
        }
    }
}


@Composable
private fun StatusBadge(
    stato: String
) {

    val background = when(stato) {
        "ATTIVA" -> Color(0xffdcfce7)
        "SCADUTA" -> Color(0xfffff3e0)
        else -> Color(0xffffebee)
    }


    val textColor = when(stato) {
        "ATTIVA" -> Color(0xff016630)
        "SCADUTA" -> Color(0xffe65100)
        else -> Color(0xffc62828)
    }


    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .border(
                width = 1.dp,
                color = textColor.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
    ) {

        Text(
            text = stato.lowercase()
                .replaceFirstChar { it.uppercase() },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
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