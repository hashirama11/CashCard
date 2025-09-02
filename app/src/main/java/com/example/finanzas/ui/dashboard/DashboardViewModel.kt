package com.example.finanzas.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.ui.dashboard.DashboardState
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails
import com.example.finanzas.ui.theme.PieChartColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
class DashboardViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _selectedCurrency = MutableStateFlow<Moneda?>(null)
    val selectedCurrency: StateFlow<Moneda?> = _selectedCurrency

    val state: StateFlow<DashboardState> = combine(
        _selectedCurrency,
        repository.getAllTransacciones(),
        repository.getAllCategorias(),
        repository.getUsuario(),
        repository.getAllMonedas()
    ) { selectedCurrency, allTransactions, allCategories, user, allMonedas ->
        if (selectedCurrency == null || user == null) {
            DashboardState(isLoading = true)
        } else {
            val categoriesMap = allCategories.associateBy { it.id }
            val monedasMap = allMonedas.associateBy { it.nombre }

            val currentMonthTransactions = allTransactions.filter {
                isTransactionInCurrentMonth(it) && it.moneda == selectedCurrency.nombre
            }

            val transactionsWithDetails = currentMonthTransactions.map {
                TransactionWithDetails(it, categoriesMap[it.categoriaId], monedasMap[it.moneda])
            }

            val incomeTransactions = transactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.INGRESO.name }
            val expenseTransactions = transactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.GASTO.name }
            val savingsTransactions = transactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.AHORRO.name }

            val totalIngresos = incomeTransactions.sumOf { it.transaccion.monto }
            val totalGastos = expenseTransactions.sumOf { it.transaccion.monto }
            val totalAhorros = savingsTransactions.sumOf { it.transaccion.monto }
            val balanceNeto = totalIngresos - totalGastos

            val incomeChartData = createChartData(incomeTransactions, totalIngresos)
            val expenseChartData = createChartData(expenseTransactions, totalGastos)

            val usedCurrencyNames = allTransactions.map { it.moneda }.distinct()
            val allUsedCurrencies = allMonedas.filter { it.nombre in usedCurrencyNames }

            val displayTransactions = transactionsWithDetails
                .filter { it.transaccion.tipo != TipoTransaccion.AHORRO.name }
                .sortedByDescending { it.transaccion.fecha }

            DashboardState(
                transactions = transactionsWithDetails,
                displayTransactions = displayTransactions,
                totalIngresos = totalIngresos,
                totalGastos = totalGastos,
                totalAhorros = totalAhorros,
                balanceNeto = balanceNeto,
                incomeChartData = incomeChartData,
                expenseChartData = expenseChartData,
                isLoading = false,
                userName = user.nombre,
                usedCurrenciesInMonth = allUsedCurrencies,
                selectedCurrency = selectedCurrency
            )
        }
    }
    .flowOn(Dispatchers.Default)
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            repository.getUsuario()
                .combine(repository.getAllMonedas()) { user, allMonedas ->
                    val primaryCurrency = user?.monedaPrincipal?.let { p -> allMonedas.find { it.nombre == p } }
                    if (primaryCurrency != null) {
                        primaryCurrency
                    } else {
                        allMonedas.firstOrNull()
                    }
                }
                .collect { initialCurrency ->
                    // Always set the initial currency. If the user has manually selected one,
                    // this ViewModel will be recreated upon re-entering the screen,
                    // correctly resetting to the new primary currency.
                    _selectedCurrency.value = initialCurrency
                }
        }
    }

    fun onCurrencySelected(moneda: Moneda) {
        _selectedCurrency.value = moneda
    }

    private fun isTransactionInCurrentMonth(transaction: com.example.finanzas.data.local.entity.Transaccion): Boolean {
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