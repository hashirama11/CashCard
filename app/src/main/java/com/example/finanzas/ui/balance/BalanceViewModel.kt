package com.example.finanzas.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
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

    val state: StateFlow<BalanceState> = combine(
        _selectedCurrency,
        repository.getAllTransacciones(),
        usedCurrencies
    ) { selectedCurrency, allTransactions, usedCurrenciesList ->
        if (selectedCurrency == null) {
            BalanceState(isLoading = true, usedCurrencies = usedCurrenciesList)
        } else {
            val filteredTransactions = allTransactions.filter { it.moneda == selectedCurrency.nombre }

            val ingresos = filteredTransactions.filter { it.tipo == TipoTransaccion.INGRESO.name }
            val gastos = filteredTransactions.filter { it.tipo == TipoTransaccion.GASTO.name }

            val totalIngresos = ingresos.sumOf { it.monto }
            val totalGastos = gastos.sumOf { it.monto }
            val balanceNeto = totalIngresos - totalGastos

            val tasaAhorro = if (totalIngresos > 0) (balanceNeto / totalIngresos).toFloat() else 0f

            // Monthly data for the chart (last 6 months)
            val monthlyData = mutableMapOf<String, Pair<Double, Double>>()
            val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())
            val sixMonthsAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -5) }

            for (i in 0..5) {
                val monthCalendar = (sixMonthsAgo.clone() as Calendar).apply { add(Calendar.MONTH, i) }
                val monthKey = monthFormatter.format(monthCalendar.time)
                monthlyData[monthKey] = 0.0 to 0.0
            }

            filteredTransactions.forEach { tx ->
                val calendar = Calendar.getInstance().apply { time = tx.fecha }
                val monthKey = monthFormatter.format(calendar.time)
                if (monthlyData.containsKey(monthKey)) {
                    val current = monthlyData.getValue(monthKey)
                    when (tx.tipo) {
                        TipoTransaccion.INGRESO.name -> monthlyData[monthKey] = current.first + tx.monto to current.second
                        TipoTransaccion.GASTO.name -> monthlyData[monthKey] = current.first to current.second + tx.monto
                    }
                }
            }

            val monthlyFlows = monthlyData.map { (month, data) ->
                MonthlyFlow(month, data.first.toFloat(), data.second.toFloat())
            }

            BalanceState(
                totalIngresos = totalIngresos,
                totalGastos = totalGastos,
                balanceNeto = balanceNeto,
                tasaAhorro = tasaAhorro.coerceIn(0f, 1f),
                monthlyFlows = monthlyFlows,
                isLoading = false,
                usedCurrencies = usedCurrenciesList,
                selectedCurrency = selectedCurrency
            )
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BalanceState(isLoading = true)
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
}