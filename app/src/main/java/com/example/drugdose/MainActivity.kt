package com.example.drugdose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.drugdose.ui.theme.DrugDoseTheme
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
                AppNavigation()
            }
        }
    }
}


