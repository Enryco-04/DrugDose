package com.example.drugdose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.drugdose.data.model.Farmaco
import com.example.drugdose.data.model.PazienteEmbedded
import com.example.drugdose.data.model.Prescrizione
import com.example.drugdose.data.repository.AuthRepository
import com.example.drugdose.data.repository.AuthRepositoryImpl
import com.example.drugdose.data.repository.FarmaciRepository
import com.example.drugdose.data.repository.FarmaciRepositoryImpl
import com.example.drugdose.data.repository.PrescrizioniRepository
import com.example.drugdose.data.repository.PrescrizioniRepositoryImpl
import com.example.drugdose.ui.theme.DrugDoseTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.Date

private const val BACKEND_TEST_TAG = "BackendTestConsole"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DrugDoseTheme {
                BackendTestScreen()
            }
        }
    }
}

@Composable
private fun BackendTestScreen() {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val authRepository = remember { lazy<AuthRepository> { AuthRepositoryImpl() } }
    val farmaciRepository = remember { lazy<FarmaciRepository> { FarmaciRepositoryImpl() } }
    val prescrizioniRepository = remember { lazy<PrescrizioniRepository> { PrescrizioniRepositoryImpl() } }

    var email by remember { mutableStateOf("prova@gmail.com") }
    var password by remember { mutableStateOf("123456") }
    var nome by remember { mutableStateOf("Mario") }
    var cognome by remember { mutableStateOf("Rossi") }
    var numeroOrdine by remember { mutableStateOf("12345") }
    var provinciaOrdine by remember { mutableStateOf("MI") }

    var idFarmaco by remember { mutableStateOf("") }
    var queryFarmaco by remember { mutableStateOf("") }
    var prescrizioneId by remember { mutableStateOf("") }

    var latestOutput by remember { mutableStateOf("Nessun test eseguito.") }
    var output by remember { mutableStateOf("Backend test console pronta.") }

    fun appendLog(title: String, message: String) {
        val logEntry = "[$title]\n$message"
        latestOutput = logEntry
        output = "$logEntry\n\n$output"
        Log.d(BACKEND_TEST_TAG, logEntry)
    }

    fun <T> appendResult(title: String, result: Result<T>, formatter: (T) -> String = { it.toString() }) {
        result.fold(
            onSuccess = { value -> appendLog(title, "SUCCESS\n${formatter(value)}") },
            onFailure = { error -> appendLog(title, "FAILURE\n${error.javaClass.simpleName}: ${error.message}") }
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Backend Test Console",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = latestOutput,
                style = MaterialTheme.typography.bodyMedium
            )

            TestSection(title = "AuthRepository") {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = cognome,
                    onValueChange = { cognome = it },
                    label = { Text("Cognome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = numeroOrdine,
                        onValueChange = { numeroOrdine = it },
                        label = { Text("Numero ordine") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = provinciaOrdine,
                        onValueChange = { provinciaOrdine = it },
                        label = { Text("Provincia") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                ButtonRow {
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "registraMedico",
                                result = authRepository.value.registraMedico(
                                    email = email,
                                    password = password,
                                    nome = nome,
                                    cognome = cognome,
                                    numeroOrdine = numeroOrdine,
                                    provinciaOrdine = provinciaOrdine
                                )
                            )
                        }
                    }) {
                        Text("registraMedico")
                    }
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "login",
                                result = authRepository.value.login(email, password)
                            )
                        }
                    }) {
                        Text("login")
                    }
                }
                ButtonRow {
                    Button(onClick = {
                        authRepository.value.logout()
                        appendLog("logout", "SUCCESS")
                    }) {
                        Text("logout")
                    }
                    Button(onClick = {
                        appendLog("isLoggato", authRepository.value.isLoggato().toString())
                    }) {
                        Text("isLoggato")
                    }
                    Button(onClick = {
                        appendLog(
                            title = "getMedicoIdCorrente",
                            message = authRepository.value.getMedicoIdCorrente() ?: "null"
                        )
                    }) {
                        Text("getMedicoIdCorrente")
                    }
                }
            }

            TestSection(title = "FarmaciRepository") {
                OutlinedTextField(
                    value = idFarmaco,
                    onValueChange = { idFarmaco = it },
                    label = { Text("ID Farmaco") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = queryFarmaco,
                    onValueChange = { queryFarmaco = it },
                    label = { Text("Query farmaco") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                ButtonRow {
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "getFarmaci",
                                result = farmaciRepository.value.getFarmaci(),
                                formatter = { farmaci -> summarizeList(farmaci) }
                            )
                        }
                    }) {
                        Text("getFarmaci")
                    }
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "getFarmacoById",
                                result = farmaciRepository.value.getFarmacoById(idFarmaco)
                            )
                        }
                    }) {
                        Text("getFarmacoById")
                    }
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "cercaFarmaci",
                                result = farmaciRepository.value.cercaFarmaci(queryFarmaco),
                                formatter = { farmaci -> summarizeList(farmaci) }
                            )
                        }
                    }) {
                        Text("cercaFarmaci")
                    }
                }

            }

            TestSection(title = "PrescrizioniRepository") {
                OutlinedTextField(
                    value = prescrizioneId,
                    onValueChange = { prescrizioneId = it },
                    label = { Text("Prescrizione ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                ButtonRow {
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "creaPrescrizione",
                                result = prescrizioniRepository.value.creaPrescrizione(
                                    createTestPrescrizione(idFarmaco = idFarmaco)
                                )
                            )
                        }
                    }) {
                        Text("creaPrescrizione")
                    }
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "getPrescrizioni",
                                result = prescrizioniRepository.value.getPrescrizioni(),
                                formatter = { prescrizioni -> summarizeList(prescrizioni) }
                            )
                        }
                    }) {
                        Text("getPrescrizioni")
                    }
                }
                ButtonRow {
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "getPrescrizioniByIdFarmaco",
                                result = prescrizioniRepository.value.getPrescrizioniByIdFarmaco(idFarmaco),
                                formatter = { prescrizioni -> summarizeList(prescrizioni) }
                            )
                        }
                    }) {
                        Text("getByIdFarmaco")
                    }
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "getPrescrizioneById",
                                result = prescrizioniRepository.value.getPrescrizioneById(prescrizioneId)
                            )
                        }
                    }) {
                        Text("getById")
                    }
                    Button(onClick = {
                        scope.launch {
                            appendResult(
                                title = "annullaPrescrizione",
                                result = prescrizioniRepository.value.annullaPrescrizione(prescrizioneId)
                            )
                        }
                    }) {
                        Text("annulla")
                    }
                }
            }

            TestSection(title = "Output") {
                Button(onClick = { output = "Backend test console pulita." }) {
                    Text("Clear output")
                }
                Text(
                    text = output,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun TestSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HorizontalDivider()
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        content()
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun ButtonRow(content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        content()
    }
}

private fun createTestPrescrizione(idFarmaco: String): Prescrizione {
    val now = System.currentTimeMillis()
    val effectiveIdFarmaco = idFarmaco.ifBlank { "test-id-farmaco" }

    return Prescrizione(
        status = "ATTIVA",
        dataCreazione = Timestamp(Date(now)),
        dataScadenza = Timestamp(Date(now + 30L * 24L * 60L * 60L * 1000L)),
        paziente = PazienteEmbedded(
            nome = "Rossi",
            cognome = "Mario",
            codiceFiscale = "TSTPZN00A01F205X"
        ),
        idFarmaco = effectiveIdFarmaco,
        nomeFarmaco = "Farmaco Test",
        nomeCommercialeFarmaco = "DrugDose Test",
        dosaggioMg = 100.0,
        formaFarmaceuticaSomministrazione = "compressa",
        numeroUnitaSomministrazione = 1,
        quantita = 1,
        note = "Prescrizione creata dalla Backend Test Console",
        frequenza = "1 volta al giorno"
    )
}

private fun <T> summarizeList(items: List<T>): String {
    val preview = items
        .take(5)
        .joinToString(separator = "\n") { item -> "- $item" }

    return if (items.size > 5) {
        "Totale: ${items.size}\n$preview\n..."
    } else {
        "Totale: ${items.size}\n$preview"
    }
}
