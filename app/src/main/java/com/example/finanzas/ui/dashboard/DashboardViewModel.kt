package com.example.finanzas.ui.dashboard

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
        viewModelScope.launch {
            checkAndPerformMonthEndClosure()
        }

        val transactionsFlow = repository.getAllTransacciones()
        val categoriesFlow = repository.getAllCategorias()
        val userFlow = repository.getUsuario()
        val monedasFlow = repository.getAllMonedas()

        combine(transactionsFlow, categoriesFlow, userFlow, monedasFlow) { allTransactions, categories, user, monedas ->
            val categoriesMap = categories.associateBy { it.id }
            val monedasMap = monedas.associateBy { it.nombre }
            val primaryCurrencySymbol = monedasMap[user?.monedaPrincipal]?.simbolo ?: ""
            val secondaryCurrencySymbol = monedasMap[user?.monedaSecundaria]?.simbolo ?: ""

            val currentMonthTransactions = allTransactions.filter { isTransactionInCurrentMonth(it) }
            val transactionsWithDetails = currentMonthTransactions.map { transaccion ->
                TransactionWithDetails(
                    transaccion = transaccion,
                    categoria = categoriesMap[transaccion.categoriaId]
                )
            }
            val ingresos = currentMonthTransactions.filter { it.tipo == TipoTransaccion.INGRESO.name }
            val gastos = currentMonthTransactions.filter { it.tipo == TipoTransaccion.GASTO.name }
            val totalIngresosVes = ingresos.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }
            val totalIngresosUsd = ingresos.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }
            val totalGastosVes = gastos.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }
            val totalGastosUsd = gastos.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }
            val totalGastosConsolidados = gastos.sumOf { it.monto }
            val totalIngresosConsolidados = ingresos.sumOf { it.monto }
            val expenseChartData = createChartData(transactionsWithDetails, TipoTransaccion.GASTO, totalGastosConsolidados)
            val incomeChartData = createChartData(transactionsWithDetails, TipoTransaccion.INGRESO, totalIngresosConsolidados)

            val savingsTransactions = currentMonthTransactions.filter { it.tipo == TipoTransaccion.AHORRO.name }
            val totalAhorrosVes = savingsTransactions.filter { it.moneda == primaryCurrencySymbol }.sumOf { it.monto }
            val totalAhorrosUsd = savingsTransactions.filter { it.moneda == secondaryCurrencySymbol }.sumOf { it.monto }

            val allSavingsTransactions = allTransactions
                .filter { it.tipo == TipoTransaccion.AHORRO.name }
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
                    transactionsWithDetails = transactionsWithDetails,
                    totalIngresosVes = totalIngresosVes,
                    totalIngresosUsd = totalIngresosUsd,
                    totalGastosVes = totalGastosVes,
                    totalGastosUsd = totalGastosUsd,
                    totalAhorrosVes = totalAhorrosVes,
                    totalAhorrosUsd = totalAhorrosUsd,
                    userName = user?.nombre ?: "Usuario",
                    ahorroAcumulado = user?.ahorroAcumulado ?: 0.0,
                    expenseChartData = expenseChartData,
                    incomeChartData = incomeChartData,
                    savingsChartData = savingsChartData,
                    monthlySummary = monthlySummary,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
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
        val lastClosureDate = Calendar.getInstance().apply { timeInMillis = user.fechaUltimoCierre }
        val today = Calendar.getInstance()

        if (lastClosureDate.get(Calendar.MONTH) != today.get(Calendar.MONTH) ||
            lastClosureDate.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {

            val allTransactions = repository.getAllTransacciones().first()
            val monedas = repository.getAllMonedas().first()
            val monedasMapByNombre = monedas.associateBy { it.nombre }
            val monedasMapBySimbolo = monedas.associateBy { it.simbolo }

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
        type: TipoTransaccion,
        total: Double
    ): List<PieChartData> {
        if (total <= 0) return emptyList()
        return transactions
            .filter { it.transaccion.tipo == type.name && it.categoria != null }
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