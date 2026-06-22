package com.example.drugdose.data.calculator

import com.example.drugdose.data.model.Farmaco
import com.example.drugdose.data.model.RisultatoCalcolo
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

object Calculator {

    fun calcola(
        farmaco: Farmaco,
        pesoKg: Double,
        altezzaCm: Double,
        etaAnni: Int
    ): RisultatoCalcolo {

        // — validazione —
        val erroriCalcolo = mutableListOf<String>()

        val minPeso = farmaco.pesoMinimoKg ?: 0.0
        if (pesoKg < minPeso) {
            erroriCalcolo.add("Peso $pesoKg kg inferiore al minimo richiesto ($minPeso kg).")
        }

        val etaMinima = farmaco.etaMinimaAnni ?: 0
        if (etaAnni < etaMinima) {
            erroriCalcolo.add("Età $etaAnni anni inferiore al minimo richiesto ($etaMinima anni).")
        }

        // se ci sono errori di validazione, blocca il calcolo, restituisce 0 ed errori
        if (erroriCalcolo.isNotEmpty()) {
            return RisultatoCalcolo(
                doseRealeMg = 0.0,
                doseArrotondataMg = 0.0,
                numeroUnitaSomministrare = 0.0,
                erroriCalcolo = erroriCalcolo
            )
        }

        // — calcolo dose reale —
        val doseRealeMg = when (farmaco.tipoFormula) {

            "PER_KG" -> {
                // doseUnitaria è in mg/kg
                // es. 0.2 mg/kg * 70 kg = 14 mg
                val dose = (farmaco.doseUnitaria ?: 0.0) * pesoKg

                // applica limiti min/max
                applicaLimiti(dose, farmaco)
            }

            "PER_M2" -> {
                // BSA con formula Mosteller
                // BSA = sqrt((altezzaCm * pesoKg) / 3600)
                val bsa = sqrt((altezzaCm * pesoKg) / 3600.0)
                val dose = (farmaco.doseUnitaria ?: 0.0) * bsa

                applicaLimiti(dose, farmaco)
            }

            "FISSA" -> {
                // dose fissa, non dipende dal paziente
                farmaco.doseUnitaria ?: 0.0
            }

            "FASCE" -> {
                // trova la fascia di peso corretta
                val fascia = farmaco.fascePeso?.firstOrNull { f ->
                    pesoKg >= f.minKg && (f.maxKg == null || pesoKg <= f.maxKg)
                }

                if (fascia == null) {
                    erroriCalcolo.add("Nessuna fascia di peso trovata per $pesoKg kg.")
                    0.0
                } else {
                    fascia.doseMg
                }
            }

            else -> {
                erroriCalcolo.add("Tipo formula non riconosciuto: ${farmaco.tipoFormula}")
                0.0
            }
        }

        // — arrotondamento —
        val base = farmaco.unitaSomministrazioneMg

        val doseArrotondataMg = when (farmaco.strategiaArrotondamento) {
            "MULTIPLO_PIU_VICINO" -> (doseRealeMg / base).roundToInt() * base
            "ECCESSO"             -> ceil(doseRealeMg / base) * base
            "DIFETTO"             -> floor(doseRealeMg / base) * base
            else                  -> doseRealeMg  // NESSUNO
        }

        // — numero unità da somministrare —
        // SEMPRE arrotondato per eccesso indipendentemente dalla strategiaArrotondamento
        // perché il numero di confezioni fisiche da aprire non può essere frazionario
        val numeroUnita = if (base > 0) ceil(doseArrotondataMg / base) else 0.0

        return RisultatoCalcolo(
            doseRealeMg = doseRealeMg,
            doseArrotondataMg = doseArrotondataMg,
            numeroUnitaSomministrare = numeroUnita,
            erroriCalcolo = if (erroriCalcolo.isEmpty()) null else erroriCalcolo
        )
    }

    private fun applicaLimiti(dose: Double, farmaco: Farmaco): Double {
        var d = dose
        farmaco.doseMassimaMg?.let { d = minOf(d, it) }
        farmaco.doseMinimaMg?.let  { d = maxOf(d, it) }
        return d
    }
}