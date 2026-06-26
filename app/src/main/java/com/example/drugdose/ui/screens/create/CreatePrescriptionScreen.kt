package com.example.drugdose.ui.screens.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drugdose.R
import com.example.drugdose.di.ViewModelFactory
import com.example.drugdose.ui.components.ProfileDropdownMenu

@Composable
fun CreatePrescriptionScreen(
    farmacoId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onPrescrizioneCreata: () -> Unit = {},
    onLogoutClick: () -> Unit
) {
    val viewModel: CreatePrescriptionViewModel = viewModel(
        factory = ViewModelFactory(farmacoId = farmacoId)
    )

    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val salvataggioState by viewModel.salvataggioState.collectAsStateWithLifecycle()
    val caricamentoFarmacoState by viewModel.caricamentoFarmacoState.collectAsStateWithLifecycle()

    when (val stato = caricamentoFarmacoState) {
        is CaricamentoState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return
        }
        is CaricamentoState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stato.message,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
            return
        }
        is CaricamentoState.Success -> {
            // procede normalmente
        }
    }

    var profileMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            shape = RoundedCornerShape(0.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // HEADER FISSO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(50.dp)
                            .clip(CircleShape)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape
                            )
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Back",
                                tint = Color.Unspecified,
                                modifier = Modifier.requiredSize(40.dp)
                            )
                        }
                    }

                    Text(
                        text = "DrugDose",
                        style = TextStyle(
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // TITOLO + SOTTOTITOLO FARMACO -” fisso
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Compila Prescrizione:",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formState.farmaco?.let { "${it.nome} -“ ${it.nomeCommerciale}" }
                            ?: "Caricamento...",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                StepIndicator(
                    currentStep = currentStep,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        when (currentStep) {
                            PrescrizioneStep.PAZIENTE -> PazienteStep(
                                formState = formState,
                                onNomeChange = viewModel::onNomeChange,
                                onCognomeChange = viewModel::onCognomeChange,
                                onCodiceFiscaleChange = viewModel::onCodiceFiscaleChange,
                                onEtaChange = viewModel::onEtaChange,
                                onPesoChange = viewModel::onPesoChange,
                                onAltezzaChange = viewModel::onAltezzaChange
                            )

                            PrescrizioneStep.FARMACO -> FarmacoStep(
                                formState = formState,
                                onFrequenzaChange = viewModel::onFrequenzaChange,
                                onNumeroConfezioniChange = viewModel::onNumeroConfezioniChange,
                                onNoteChange = viewModel::onNoteChange
                            )

                            PrescrizioneStep.RIEPILOGO -> RiepilogoStep(formState = formState)
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }

                NavigationButtons(
                    currentStep = currentStep,
                    salvataggioState = salvataggioState,
                    onBack = { viewModel.goToPreviousStep() },
                    onNext = { viewModel.goToNextStep() },
                    onConferma = {
                        viewModel.confermaPrescrizione(onSuccess = onPrescrizioneCreata)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }

        ProfileDropdownMenu(
            expanded = profileMenuExpanded,
            onAvatarClick = {
                profileMenuExpanded = !profileMenuExpanded
            },
            onDismiss = {
                profileMenuExpanded = false
            },
            onLogoutClick = {
                profileMenuExpanded = false
                onLogoutClick()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 40.dp,
                    end = 16.dp
                )
        )
    }

    if (viewModel.mostraPopUpErrori) {
        PopUpErrori(
            errori = formState.erroriCalcolo ?: emptyList(),
            onDismiss = { viewModel.chiudiPopUpErrori() }
        )
    }
}

@Composable
private fun StepIndicator(
    currentStep: PrescrizioneStep,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrescrizioneStep.values().forEachIndexed { index, step ->
            val isActive = step == currentStep
            val isPast = step.ordinal < currentStep.ordinal

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        when {
                            isActive -> MaterialTheme.colorScheme.primary
                            isPast -> MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                            else -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.label,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isActive)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }

            if (index < PrescrizioneStep.values().lastIndex) {
                Text(
                    text = "-",
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
private fun NavigationButtons(
    currentStep: PrescrizioneStep,
    salvataggioState: SalvataggioState,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onConferma: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Bottone Indietro — nascosto/disabilitato solo se si vuole sul primo step.
        // Qui resta visibile ma cliccabile solo se non siamo già al primo step.
        if (currentStep != PrescrizioneStep.PAZIENTE) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Indietro",
                    tint = MaterialTheme.colorScheme.onPrimary ,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = "Indietro",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary )
                )
            }
        } else {
            Spacer(modifier = Modifier.size(width = 64.dp, height = 50.dp))
        }

        when (currentStep) {
            PrescrizioneStep.PAZIENTE, PrescrizioneStep.FARMACO -> {
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.height(50.dp)
                ) {
                    Text(
                        text = "Avanti",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary )
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary ,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            PrescrizioneStep.RIEPILOGO -> {
                Button(
                    onClick = onConferma,
                    enabled = salvataggioState !is SalvataggioState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)), // verde
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.height(50.dp)
                ) {
                    if (salvataggioState is SalvataggioState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Conferma",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}