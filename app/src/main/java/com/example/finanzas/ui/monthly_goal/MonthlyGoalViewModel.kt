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

        combine(transactionsFlow, userFlow) { allTransactions, user ->
            val ahorroAcumulado = user?.ahorroAcumulado ?: 0.0

            val currentMonthTransactions = allTransactions.filter { isTransactionInCurrentMonth(it) }

            val ingresosDelMes = currentMonthTransactions
                .filter { it.tipo == TipoTransaccion.INGRESO.name }
                .sumOf { it.monto }

            val gastosDelMes = currentMonthTransactions
                .filter { it.tipo == TipoTransaccion.GASTO.name }
                .sumOf { it.monto }

            val balanceDelMes = ingresosDelMes - gastosDelMes
            val saldoActual = ahorroAcumulado + balanceDelMes

            _state.update {
                it.copy(
                    ahorroAcumulado = ahorroAcumulado,
                    ingresosDelMes = ingresosDelMes,
                    gastosDelMes = gastosDelMes,
                    balanceDelMes = balanceDelMes,
                    saldoActual = saldoActual,
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