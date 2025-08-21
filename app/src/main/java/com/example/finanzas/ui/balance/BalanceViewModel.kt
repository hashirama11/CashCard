package com.example.finanzas.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BalanceState())
    val state = _state.asStateFlow()

    init {
        repository.getAllTransacciones().onEach { transactions ->
            // Cálculos generales
            val totalIngresos = transactions.filter { it.tipo == TipoTransaccion.INGRESO.name }.sumOf { it.monto }
            val totalGastos = transactions.filter { it.tipo == TipoTransaccion.GASTO.name }.sumOf { it.monto }
            val balanceNeto = totalIngresos - totalGastos
            val tasaAhorro = if (totalIngresos > 0) (balanceNeto / totalIngresos).toFloat() else 0f

            // Agrupar transacciones por mes para el gráfico (últimos 6 meses)
            val calendar = Calendar.getInstance()
            val monthlyData = mutableMapOf<String, Pair<Double, Double>>()
            val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())

            // Inicializar los últimos 6 meses en el mapa para asegurar que aparezcan
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
                    totalIngresos = totalIngresos,
                    totalGastos = totalGastos,
                    balanceNeto = balanceNeto,
                    tasaAhorro = tasaAhorro.coerceIn(0f, 1f), // Asegura que esté entre 0 y 1
                    monthlyFlows = monthlyFlows,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}