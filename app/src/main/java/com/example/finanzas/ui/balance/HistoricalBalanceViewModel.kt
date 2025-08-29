package com.example.finanzas.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoricalBalanceViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BalanceState())
    val state = _state.asStateFlow()

    init {
        val transactionsFlow = repository.getAllTransacciones()
        val userFlow = repository.getUsuario()
        val monedasFlow = repository.getAllMonedas()

        // Esta lógica ahora calcula sobre TODAS las transacciones para dar un total histórico
        combine(transactionsFlow, userFlow, monedasFlow) { transactions, user, monedas ->
            val monedasMap = monedas.associateBy { it.nombre }
            val primaryCurrencySymbol = monedasMap[user?.monedaPrincipal]?.simbolo ?: ""
            val secondaryCurrencySymbol = monedasMap[user?.monedaSecundaria]?.simbolo ?: ""

            val ingresos = transactions.filter { it.tipo == TipoTransaccion.INGRESO.name }
            val gastos = transactions.filter { it.tipo == TipoTransaccion.GASTO.name }

            val totalIngresosVes = ingresos.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }
            val totalIngresosUsd = ingresos.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }
            val totalGastosVes = gastos.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }
            val totalGastosUsd = gastos.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }

            val balanceNetoVes = totalIngresosVes - totalGastosVes
            val balanceNetoUsd = totalIngresosUsd - totalGastosUsd

            val totalIngresosConsolidado = ingresos.sumOf { it.monto }
            val balanceNetoConsolidado = transactions.sumOf { if (it.tipo == TipoTransaccion.INGRESO.name) it.monto else -it.monto }
            val tasaAhorro = if (totalIngresosConsolidado > 0) (balanceNetoConsolidado / totalIngresosConsolidado).toFloat() else 0f


            val calendar = Calendar.getInstance()
            val monthlyData = mutableMapOf<String, Pair<Double, Double>>()
            val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())

            for (i in 5 downTo 0) {
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.MONTH, -i)
                val monthKey = monthFormatter.format(calendar.time)
                monthlyData[monthKey] = 0.0 to 0.0
            }

            transactions.forEach { tx ->
                calendar.time = tx.fecha
                val monthKey = monthFormatter.format(calendar.time)
                if (monthlyData.containsKey(monthKey)) {
                    val current = monthlyData[monthKey]!!
                    if (tx.tipo == TipoTransaccion.INGRESO.name) {
                        monthlyData[monthKey] = current.first + tx.monto to current.second
                    } else {
                        monthlyData[monthKey] = current.first to current.second + tx.monto
                    }
                }
            }

            val monthlyFlows = monthlyData.map { (month, data) ->
                MonthlyFlow(month, data.first.toFloat(), data.second.toFloat())
            }

            _state.update {
                it.copy(
                    totalIngresosVes = totalIngresosVes,
                    totalIngresosUsd = totalIngresosUsd,
                    totalGastosVes = totalGastosVes,
                    totalGastosUsd = totalGastosUsd,
                    balanceNetoVes = balanceNetoVes,
                    balanceNetoUsd = balanceNetoUsd,
                    tasaAhorro = tasaAhorro.coerceIn(0f, 1f),
                    monthlyFlows = monthlyFlows,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}