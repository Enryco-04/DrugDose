package com.example.drugdose.ui.screens.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PazienteStep(
    formState: PrescriptionFormState,
    onNomeChange: (String) -> Unit,
    onCognomeChange: (String) -> Unit,
    onCodiceFiscaleChange: (String) -> Unit,
    onEtaChange: (String) -> Unit,
    onPesoChange: (String) -> Unit,
    onAltezzaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormCard(title = "Nome e Cognome") {
            OutlinedTextField(
                value = formState.nome,
                onValueChange = onNomeChange,
                placeholder = { Text("Nome", style = fieldPlaceholderStyle()) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = formState.cognome,
                onValueChange = onCognomeChange,
                placeholder = { Text("Cognome", style = fieldPlaceholderStyle()) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        FormCard(title = "Codice Fiscale") {
            OutlinedTextField(
                value = formState.codiceFiscale,
                onValueChange = { onCodiceFiscaleChange(it.uppercase()) },
                placeholder = { Text("RSSMRA80A01H501U", style = fieldPlaceholderStyle()) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        FormCard(title = "Età") {
            OutlinedTextField(
                value = formState.etaAnni,
                onValueChange = { if (it.all { c -> c.isDigit() }) onEtaChange(it) },
                placeholder = { Text("Età (anni)", style = fieldPlaceholderStyle()) },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        FormCard(title = "Peso e Altezza") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = formState.pesoKg,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) onPesoChange(it) },
                    placeholder = { Text("Peso", style = fieldPlaceholderStyle()) },
                    suffix = { Text("kg", style = fieldPlaceholderStyle()) },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = formState.altezzaCm,
                    onValueChange = { if (it.all { c -> c.isDigit() }) onAltezzaChange(it) },
                    placeholder = { Text("Altezza", style = fieldPlaceholderStyle()) },
                    suffix = { Text("cm", style = fieldPlaceholderStyle()) },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/** Card bianca arrotondata con titolo — riutilizzata in tutti gli step */
@Composable
fun FormCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            content()
        }
    }
}

@Composable
fun fieldPlaceholderStyle() = TextStyle(
    fontSize = 14.sp,
    color = Color(0xFFAFAFAF)
)

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = Color(0xFFE0E0E0),
    focusedContainerColor = Color(0xFFFAFAFA),
    unfocusedContainerColor = Color(0xFFFAFAFA)
)