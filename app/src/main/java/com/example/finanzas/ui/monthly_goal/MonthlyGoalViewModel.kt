package com.example.finanzas.ui.monthly_goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MonthlyGoalViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MonthlyGoalState())
    val state = _state.asStateFlow()

    init {
        val transactionsFlow = repository.getAllTransacciones()
        val userFlow = repository.getUsuario()
        val monedasFlow = repository.getAllMonedas()

        combine(transactionsFlow, userFlow, monedasFlow) { allTransactions, user, monedas ->
            val monedasMap = monedas.associateBy { it.nombre }
            val primaryCurrencySymbol = monedasMap[user?.monedaPrincipal]?.simbolo ?: ""
            val secondaryCurrencySymbol = monedasMap[user?.monedaSecundaria]?.simbolo ?: ""

            val ahorroAcumulado = user?.ahorroAcumulado ?: 0.0
            val currentMonthTransactions = allTransactions.filter { isTransactionInCurrentMonth(it) }

            val ingresosDelMes = currentMonthTransactions.filter { it.tipo == TipoTransaccion.INGRESO.name }
            val gastosDelMes = currentMonthTransactions.filter { it.tipo == TipoTransaccion.GASTO.name }

            val ingresosDelMesUsd = ingresosDelMes.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }
            val ingresosDelMesVes = ingresosDelMes.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }

            val gastosDelMesUsd = gastosDelMes.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }
            val gastosDelMesVes = gastosDelMes.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }

            val balanceDelMesUsd = ingresosDelMesUsd - gastosDelMesUsd
            val balanceDelMesVes = ingresosDelMesVes - gastosDelMesVes

            val saldoActualUsd = ahorroAcumulado + balanceDelMesUsd
            val saldoActualVes = balanceDelMesVes

            val savingsRateUsd = if (ingresosDelMesUsd > 0) {
                (balanceDelMesUsd / ingresosDelMesUsd).toFloat()
            } else { 0f }.coerceIn(0f, 1f)

            // --- NUEVO CÁLCULO PARA TASA DE AHORRO EN VES ---
            val savingsRateVes = if (ingresosDelMesVes > 0) {
                (balanceDelMesVes / ingresosDelMesVes).toFloat()
            } else { 0f }.coerceIn(0f, 1f)

            _state.update {
                it.copy(
                    ahorroAcumulado = ahorroAcumulado,
                    ingresosDelMesUsd = ingresosDelMesUsd,
                    ingresosDelMesVes = ingresosDelMesVes,
                    gastosDelMesUsd = gastosDelMesUsd,
                    gastosDelMesVes = gastosDelMesVes,
                    balanceDelMesUsd = balanceDelMesUsd,
                    balanceDelMesVes = balanceDelMesVes,
                    saldoActualUsd = saldoActualUsd,
                    saldoActualVes = saldoActualVes,
                    savingsRateUsd = savingsRateUsd,
                    savingsRateVes = savingsRateVes, // <-- Actualizamos el estado
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun isTransactionInCurrentMonth(transaction: Transaccion): Boolean {
        val txCalendar = Calendar.getInstance().apply { time = transaction.fecha }
        val currentCalendar = Calendar.getInstance()
        return txCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                txCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
    }
}