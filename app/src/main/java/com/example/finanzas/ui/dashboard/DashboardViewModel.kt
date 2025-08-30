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
        val calendar = Calendar.getInstance()
        val summaries = mutableMapOf<String, MonthlySummary>()
        val threeMonthsAgo = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, -2) }

        for (i in 0..2) {
            val monthCalendar = (Calendar.getInstance() as Calendar).apply { add(Calendar.MONTH, -i) }
            val monthName = SimpleDateFormat("MMM", Locale.getDefault()).format(monthCalendar.time)
            summaries[monthName] = MonthlySummary(monthName, 0.0, 0.0)
        }

        val recentTransactions = transactions.filter { it.fecha.after(threeMonthsAgo.time) }

        recentTransactions.forEach { transaction ->
            val transactionCalendar = Calendar.getInstance().apply { time = transaction.fecha }
            val monthName = SimpleDateFormat("MMM", Locale.getDefault()).format(transactionCalendar.time)

            if (summaries.containsKey(monthName)) {
                val moneda = monedas.find { it.simbolo == transaction.moneda }
                val amountInPrimaryCurrency = if (moneda != null && moneda.tasa_conversion > 0) transaction.monto / moneda.tasa_conversion else transaction.monto

                val currentSummary = summaries[monthName]!!
                if (transaction.tipo == TipoTransaccion.INGRESO.name) {
                    summaries[monthName] = currentSummary.copy(income = currentSummary.income + amountInPrimaryCurrency)
                } else if (transaction.tipo == TipoTransaccion.GASTO.name) {
                    summaries[monthName] = currentSummary.copy(expense = currentSummary.expense + amountInPrimaryCurrency)
                }
            }
        }
        return summaries.values.toList().sortedBy { SimpleDateFormat("MMM", Locale.getDefault()).parse(it.month) }
    }

    private suspend fun checkAndPerformMonthEndClosure() {
        val user = repository.getUsuario().first() ?: return
        val monedas = repository.getAllMonedas().first()
        if (monedas.isEmpty()) return

        val lastClosureDate = Calendar.getInstance().apply { timeInMillis = user.fechaUltimoCierre }
        val today = Calendar.getInstance()

        if (lastClosureDate.get(Calendar.MONTH) != today.get(Calendar.MONTH) ||
            lastClosureDate.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {

            val allTransactions = repository.getAllTransacciones().first()
            val monedasMapByNombre = monedas.associateBy { it.nombre }

            val primaryCurrency = monedasMapByNombre[user.monedaPrincipal]
            val secondaryCurrency = user.monedaSecundaria?.let { monedasMapByNombre[it] }

            val lastMonthTransactions = allTransactions.filter {
                val txCal = Calendar.getInstance().apply { time = it.fecha }
                txCal.get(Calendar.MONTH) == lastClosureDate.get(Calendar.MONTH) &&
                        txCal.get(Calendar.YEAR) == lastClosureDate.get(Calendar.YEAR)
            }

            val ingresos = lastMonthTransactions.filter { it.tipo == TipoTransaccion.INGRESO.name }
            val gastos = lastMonthTransactions.filter { it.tipo == TipoTransaccion.GASTO.name }

            var totalBalanceInPrimary = 0.0

            if (primaryCurrency != null) {
                val primaryIngresos = ingresos.filter { it.moneda == primaryCurrency.simbolo }.sumOf { it.monto }
                val primaryGastos = gastos.filter { it.moneda == primaryCurrency.simbolo }.sumOf { it.monto }
                totalBalanceInPrimary += primaryIngresos - primaryGastos
            }

            if (secondaryCurrency != null) {
                val secondaryIngresos = ingresos.filter { it.moneda == secondaryCurrency.simbolo }.sumOf { it.monto }
                val secondaryGastos = gastos.filter { it.moneda == secondaryCurrency.simbolo }.sumOf { it.monto }
                val secondaryBalance = secondaryIngresos - secondaryGastos

                val conversionRate = secondaryCurrency.tasa_conversion
                if (conversionRate > 0) {
                    totalBalanceInPrimary += secondaryBalance / conversionRate
                }
            }

            var newAhorroAcumulado = user.ahorroAcumulado
            if (totalBalanceInPrimary > 0) {
                newAhorroAcumulado += totalBalanceInPrimary
            }

            repository.upsertUsuario(user.copy(
                ahorroAcumulado = newAhorroAcumulado,
                fechaUltimoCierre = today.timeInMillis
            ))
        }
    }

    private fun isTransactionInCurrentMonth(transaction: Transaccion): Boolean {
        val txCalendar = Calendar.getInstance().apply { time = transaction.fecha }
        val currentCalendar = Calendar.getInstance()
        return txCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                txCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
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