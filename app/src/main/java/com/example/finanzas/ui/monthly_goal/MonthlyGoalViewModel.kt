package com.example.finanzas.ui.monthly_goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MonthlyGoalViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _selectedCurrency = MutableStateFlow<Moneda?>(null)
    val selectedCurrency: StateFlow<Moneda?> = _selectedCurrency

    val usedCurrencies: StateFlow<List<Moneda>> = repository.getAllTransacciones()
        .combine(repository.getAllMonedas()) { transactions, allMonedas ->
            val currencyNames = transactions.map { it.moneda }.distinct()
            allMonedas.filter { it.nombre in currencyNames }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state: StateFlow<MonthlyGoalState> = combine(
        _selectedCurrency,
        repository.getAllTransacciones(),
        repository.getUsuario(),
        usedCurrencies
    ) { selectedCurrency, allTransactions, user, usedCurrenciesList ->
        if (selectedCurrency == null || user == null) {
            MonthlyGoalState(isLoading = true, usedCurrencies = usedCurrenciesList)
        } else {
            val currentMonthTransactions = allTransactions.filter {
                isTransactionInCurrentMonth(it) && it.moneda == selectedCurrency.nombre
            }

            val ingresosDelMes = currentMonthTransactions
                .filter { it.tipo == TipoTransaccion.INGRESO.name }
                .sumOf { it.monto }

            val gastosDelMes = currentMonthTransactions
                .filter { it.tipo == TipoTransaccion.GASTO.name }
                .sumOf { it.monto }

            val balanceDelMes = ingresosDelMes - gastosDelMes

            // Assuming ahorroAcumulado and monthlyGoal are in the primary currency.
            // This is a simplification. A more robust solution would involve currency conversion.
            val ahorroAcumulado = if (selectedCurrency.nombre == user.monedaPrincipal) {
                user.ahorroAcumulado
            } else {
                0.0 // Not showing accumulated savings for other currencies to avoid confusion
            }

            val monthlyGoal = if (selectedCurrency.nombre == user.monedaPrincipal) {
                user.objetivoAhorroMensual
            } else {
                0.0 // Not showing goal for other currencies
            }

            val saldoActual = ahorroAcumulado + balanceDelMes
            val savingsRate = if (ingresosDelMes > 0) (balanceDelMes / ingresosDelMes).toFloat() else 0f

            MonthlyGoalState(
                ahorroAcumulado = ahorroAcumulado,
                ingresosDelMes = ingresosDelMes,
                gastosDelMes = gastosDelMes,
                balanceDelMes = balanceDelMes,
                saldoActual = saldoActual,
                savingsRate = savingsRate.coerceIn(0f, 1f),
                isLoading = false,
                usedCurrencies = usedCurrenciesList,
                selectedCurrency = selectedCurrency,
                monthlyGoal = monthlyGoal
            )
        }
    }
    .flowOn(Dispatchers.Default)
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MonthlyGoalState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            repository.getUsuario()
                .combine(repository.getAllMonedas()) { user, allMonedas ->
                    val primaryCurrency = user?.monedaPrincipal?.let { p -> allMonedas.find { it.nombre == p } }
                    if (primaryCurrency != null) {
                        primaryCurrency
                    } else {
                        // Fallback using the usedCurrencies flow
                        usedCurrencies.value.firstOrNull()
                    }
                }
                .collect { initialCurrency ->
                    if (_selectedCurrency.value == null && initialCurrency != null) {
                        _selectedCurrency.value = initialCurrency
                    }
                }
        }
    }

    fun onCurrencySelected(moneda: Moneda) {
        _selectedCurrency.value = moneda
    }

    private fun isTransactionInCurrentMonth(transaction: Transaccion): Boolean {
        val txCalendar = Calendar.getInstance().apply { time = transaction.fecha }
        val currentCalendar = Calendar.getInstance()
        return txCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                txCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
    }
}