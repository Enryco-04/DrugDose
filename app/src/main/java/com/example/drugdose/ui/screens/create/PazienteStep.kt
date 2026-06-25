package com.example.drugdose.ui.screens.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment

import androidx.compose.ui.window.Dialog

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
                isError = formState.nomeError,
                supportingText = if (formState.nomeError) { { Text("Campo obbligatorio") } } else null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = formState.cognome,
                onValueChange = onCognomeChange,
                placeholder = { Text("Cognome", style = fieldPlaceholderStyle()) },
                isError = formState.cognomeError,
                supportingText = if (formState.cognomeError) { { Text("Campo obbligatorio") } } else null,
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
                isError = formState.codiceFiscaleError,
                supportingText = if (formState.codiceFiscaleError) { { Text("Campo obbligatorio") } } else null,
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
                isError = formState.etaError,
                supportingText = if (formState.etaError) { { Text("Campo obbligatorio") } } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    isError = formState.pesoError,
                    supportingText = if (formState.pesoError) { { Text("Obbligatorio") } } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = formState.altezzaCm,
                    onValueChange = { if (it.all { c -> c.isDigit() }) onAltezzaChange(it) },
                    placeholder = { Text("Altezza", style = fieldPlaceholderStyle()) },
                    suffix = { Text("cm", style = fieldPlaceholderStyle()) },
                    isError = formState.altezzaError,
                    supportingText = if (formState.altezzaError) { { Text("Obbligatorio") } } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    color = MaterialTheme.colorScheme.onSurface                )
            )
            content()
        }
    }
}

@Composable
fun fieldPlaceholderStyle() = TextStyle(
    fontSize = 14.sp,
    color = MaterialTheme.colorScheme.outline)

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
)



@Composable
fun PopUpErrori(
    errori: List<String>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Impossibile procedere",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    errori.forEach { errore ->
                        Text(
                            text = "• $errore",
                            style = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ho capito", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}