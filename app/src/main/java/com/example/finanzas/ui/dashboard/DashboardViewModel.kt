package com.example.finanzas.ui.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.MonthlySummary
import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.SavingsChartData
import com.example.finanzas.model.SavingsDataPoint
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails
import com.example.finanzas.ui.theme.PieChartColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000L)
            try {
                checkAndPerformMonthEndClosure()
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error during month-end closure", e)
            }
        }

        val transactionsFlow = repository.getAllTransacciones()
        val categoriesFlow = repository.getAllCategorias()
        val userFlow = repository.getUsuario()
        val monedasFlow = repository.getAllMonedas()

        combine(transactionsFlow, categoriesFlow, userFlow, monedasFlow) { allTransactions, categories, user, monedas ->
            if (user == null || monedas.isEmpty()) {
                return@combine
            }

            val categoriesMap = categories.associateBy { it.id }
            val monedasMap = monedas.associateBy { it.nombre }
            val primaryCurrencySymbol = monedasMap[user.monedaPrincipal]?.simbolo ?: ""
            val secondaryCurrencySymbol = user.monedaSecundaria?.let { monedasMap[it]?.simbolo } ?: ""

            if (_state.value.selectedSavingsCurrency == null && primaryCurrencySymbol.isNotBlank()) {
                _state.update { it.copy(selectedSavingsCurrency = primaryCurrencySymbol) }
            }
            val selectedCurrencyForSavings = _state.value.selectedSavingsCurrency

            val currentMonthTransactions = allTransactions.filter { isTransactionInCurrentMonth(it) }

            // --- LÓGICA DEL DASHBOARD DINÁMICO ---
            val usedCurrencies = currentMonthTransactions.map { it.moneda }.distinct()
            val dashboardPanels = usedCurrencies.map { currencySymbol ->
                val currencyTransactions = currentMonthTransactions.filter { it.moneda == currencySymbol }
                val currencyTransactionsWithDetails = currencyTransactions.map { TransactionWithDetails(it, categoriesMap[it.categoriaId]) }

                val incomeTransactions = currencyTransactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.INGRESO.name }
                val expenseTransactions = currencyTransactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.GASTO.name }

                val totalIncome = incomeTransactions.sumOf { it.transaccion.monto }
                val totalExpenses = expenseTransactions.sumOf { it.transaccion.monto }

                val incomeChartData = createChartData(incomeTransactions, totalIncome)
                val expenseChartData = createChartData(expenseTransactions, totalExpenses)

                DashboardPanelState(
                    currencySymbol = currencySymbol,
                    totalIncome = totalIncome,
                    totalExpenses = totalExpenses,
                    incomeTransactions = incomeTransactions,
                    expenseTransactions = expenseTransactions,
                    incomeChartData = incomeChartData,
                    expenseChartData = expenseChartData
                )
            }

            // --- Lógica de Ahorros (se mantiene separada por ahora) ---
            val savingsTransactions = currentMonthTransactions.filter { it.tipo == TipoTransaccion.AHORRO.name }
            val totalAhorrosVes = savingsTransactions.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }
            val totalAhorrosUsd = savingsTransactions.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }

            val allSavingsTransactions = allTransactions
                .filter { it.tipo == TipoTransaccion.AHORRO.name && it.moneda == selectedCurrencyForSavings }
                .sortedBy { it.fecha }
            var cumulativeSavings = 0.0
            val savingsDataPoints = allSavingsTransactions.map {
                cumulativeSavings += it.monto
                SavingsDataPoint(it.fecha, cumulativeSavings.toFloat())
            }
            val savingsChartData = SavingsChartData(savingsDataPoints)

            val monthlySummary = calculateMonthlySummary(allTransactions, monedas)

            _state.update {
                it.copy(
                    dashboardPanels = dashboardPanels,
                    totalAhorrosVes = totalAhorrosVes,
                    totalAhorrosUsd = totalAhorrosUsd,
                    userName = user.nombre,
                    ahorroAcumulado = user.ahorroAcumulado,
                    primaryCurrencySymbol = primaryCurrencySymbol,
                    secondaryCurrencySymbol = secondaryCurrencySymbol,
                    savingsChartData = savingsChartData,
                    monthlySummary = monthlySummary,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onSavingsCurrencySelected(currencySymbol: String) {
        _state.update { it.copy(selectedSavingsCurrency = currencySymbol) }
    }

    private fun calculateMonthlySummary(transactions: List<Transaccion>, monedas: List<Moneda>): List<MonthlySummary> {
        // ... (lógica existente)
    }

    private suspend fun checkAndPerformMonthEndClosure() {
        // ... (lógica existente)
    }

    private fun isTransactionInCurrentMonth(transaction: Transaccion): Boolean {
        // ... (lógica existente)
    }

    private fun createChartData(
        transactions: List<TransactionWithDetails>,
        total: Double
    ): List<PieChartData> {
        if (total <= 0) return emptyList()
        return transactions
            .filter { it.categoria != null }
            .groupBy { it.categoria!! }
            .mapValues { (_, txs) -> txs.sumOf { it.transaccion.monto } }
            .map { (category, sum) ->
                val percentage = (sum / total).toFloat()
                val color = PieChartColors[category.id % PieChartColors.size]
                PieChartData(percentage, color, category.nombre)
            }
            .sortedByDescending { it.value }
    }
}