package com.example.drugdose.ui.screens.prescriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.model.PazienteEmbedded
import com.example.drugdose.data.model.Prescrizione
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Date

enum class FiltroStatus(val label: String) {
    TUTTI("Tutti"),
    ATTIVA("Attiva"),
    SCADUTA("Scaduta"),
    ANNULLATA("Annullata")
}

class PrescriptionsViewModel : ViewModel() {

    private val _prescrizioni = MutableStateFlow<List<Prescrizione>>(emptyList())

    // — tre filtri separati, come nel mockup —
    private val _filtroCF = MutableStateFlow("")
    val filtroCF: StateFlow<String> = _filtroCF.asStateFlow()

    private val _filtroFarmaco = MutableStateFlow("")
    val filtroFarmaco: StateFlow<String> = _filtroFarmaco.asStateFlow()

    private val _filtroStatus = MutableStateFlow(FiltroStatus.TUTTI)
    val filtroStatus: StateFlow<FiltroStatus> = _filtroStatus.asStateFlow()

    // Lista filtrata — combina tutti e tre i filtri, ricerca client-side
    val prescrizioniFiltrate: StateFlow<List<Prescrizione>> = combine(
        _prescrizioni, _filtroCF, _filtroFarmaco, _filtroStatus
    ) { lista, cf, farmaco, status ->
        lista.filter { p ->
            //TODO anche per nome congome contains
            val matchCF = cf.isBlank() ||
                    p.paziente.codiceFiscale.contains(cf, ignoreCase = true)

            val matchFarmaco = farmaco.isBlank() ||
                    p.nomeFarmaco.contains(farmaco, ignoreCase = true) ||
                    p.nomeCommercialeFarmaco.contains(farmaco, ignoreCase = true)

            val matchStatus = status == FiltroStatus.TUTTI ||
                    p.status == status.name

            matchCF && matchFarmaco && matchStatus
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadPrescrizioniMock()
    }

    private fun loadPrescrizioniMock() {
        val ora = Timestamp(Date())
        val scadenza = Timestamp(Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))

        _prescrizioni.value = listOf(
            Prescrizione(
                id = "1",
                idMedico = "mock",
                status = "ATTIVA",
                dataCreazione = ora,
                dataScadenza = scadenza,
                paziente = PazienteEmbedded(
                    nome = "Mario",
                    cognome = "Rossi",
                    codiceFiscale = "RSSMRA80A01H501U"
                ),
                idFarmaco = "AMOXICILLINA",
                nomeFarmaco = "Amoxicillina",
                nomeCommercialeFarmaco = "Augmentin",
                dosaggioMg = 500.0,
                formaFarmaceuticaSomministrazione = "compressa",
                numeroUnitaSomministrazione = 1,
                quantita = 30,
                note = "3 volte al giorno, a stomaco pieno",
                frequenza = "3 volte al giorno"
            ),
            Prescrizione(
                id = "2",
                idMedico = "mock",
                status = "ATTIVA",
                dataCreazione = ora,
                dataScadenza = scadenza,
                paziente = PazienteEmbedded(
                    nome = "Luigi",
                    cognome = "Verdi",
                    codiceFiscale = "VRDLGI75C15F205X"
                ),
                idFarmaco = "IBUPROFENE",
                nomeFarmaco = "Ibuprofene",
                nomeCommercialeFarmaco = "Moment",
                dosaggioMg = 400.0,
                formaFarmaceuticaSomministrazione = "compressa",
                numeroUnitaSomministrazione = 1,
                quantita = 20,
                note = "Al bisogno",
                frequenza = "2 volte al giorno"

            )
        )
    }

    fun onFiltroCFChange(value: String) {
        _filtroCF.value = value
    }

    fun onFiltroFarmacoChange(value: String) {
        _filtroFarmaco.value = value
    }

    fun onFiltroStatusChange(value: FiltroStatus) {
        _filtroStatus.value = value
    }

    fun annullaPrescrizione(id: String) {
        // TODO: integrazione con PrescrizioniRepository
        _prescrizioni.value = _prescrizioni.value.map { p ->
            if (p.id == id) p.copy(status = "ANNULLATA") else p
        }
    }
}