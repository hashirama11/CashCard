package com.example.finanzas.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.Moneda
import com.example.finanzas.model.PieChartData
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
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        // Primero, comprobamos si necesitamos hacer un cierre de mes
        viewModelScope.launch {
            checkAndPerformMonthEndClosure()
        }

        val transactionsFlow = repository.getAllTransacciones()
        val categoriesFlow = repository.getAllCategorias()
        val userFlow = repository.getUsuario()

        combine(transactionsFlow, categoriesFlow, userFlow) { allTransactions, categories, user ->
            val categoriesMap = categories.associateBy { it.id }

            // --- LÓGICA PRINCIPAL: FILTRAR TRANSACCIONES SOLO DEL MES ACTUAL ---
            val currentMonthTransactions = allTransactions.filter { isTransactionInCurrentMonth(it) }

            val transactionsWithDetails = currentMonthTransactions.map { transaccion ->
                TransactionWithDetails(
                    transaccion = transaccion,
                    categoria = categoriesMap[transaccion.categoriaId]
                )
            }

            val ingresos = currentMonthTransactions.filter { it.tipo == TipoTransaccion.INGRESO.name }
            val gastos = currentMonthTransactions.filter { it.tipo == TipoTransaccion.GASTO.name }

            val totalIngresosVes = ingresos.filter { it.moneda == Moneda.VES.name }.sumOf { it.monto }
            val totalIngresosUsd = ingresos.filter { it.moneda == Moneda.USD.name }.sumOf { it.monto }
            val totalGastosVes = gastos.filter { it.moneda == Moneda.VES.name }.sumOf { it.monto }
            val totalGastosUsd = gastos.filter { it.moneda == Moneda.USD.name }.sumOf { it.monto }

            val totalGastosConsolidados = gastos.sumOf { it.monto }
            val totalIngresosConsolidados = ingresos.sumOf { it.monto }

            val expenseChartData = createChartData(transactionsWithDetails, TipoTransaccion.GASTO, totalGastosConsolidados)
            val incomeChartData = createChartData(transactionsWithDetails, TipoTransaccion.INGRESO, totalIngresosConsolidados)

            _state.update {
                it.copy(
                    transactionsWithDetails = transactionsWithDetails,
                    totalIngresosVes = totalIngresosVes,
                    totalIngresosUsd = totalIngresosUsd,
                    totalGastosVes = totalGastosVes,
                    totalGastosUsd = totalGastosUsd,
                    userName = user?.nombre ?: "Usuario",
                    ahorroAcumulado = user?.ahorroAcumulado ?: 0.0, // <-- Nuevo
                    expenseChartData = expenseChartData,
                    incomeChartData = incomeChartData,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun checkAndPerformMonthEndClosure() {
        val user = repository.getUsuario().first() ?: return
        val lastClosureDate = Calendar.getInstance().apply { timeInMillis = user.fechaUltimoCierre }
        val today = Calendar.getInstance()

        // Si el mes o el año de la última vez que abrimos la app es diferente al de hoy, hacemos el cierre
        if (lastClosureDate.get(Calendar.MONTH) != today.get(Calendar.MONTH) ||
            lastClosureDate.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {

            val allTransactions = repository.getAllTransacciones().first()

            // Filtramos las transacciones del mes que acaba de terminar
            val lastMonthTransactions = allTransactions.filter {
                val txCal = Calendar.getInstance().apply { time = it.fecha }
                txCal.get(Calendar.MONTH) == lastClosureDate.get(Calendar.MONTH) &&
                        txCal.get(Calendar.YEAR) == lastClosureDate.get(Calendar.YEAR)
            }

            val ingresos = lastMonthTransactions.filter { it.tipo == TipoTransaccion.INGRESO.name }.sumOf { it.monto }
            val gastos = lastMonthTransactions.filter { it.tipo == TipoTransaccion.GASTO.name }.sumOf { it.monto }

            val ahorroDelMes = ingresos - gastos

            var newAhorroAcumulado = user.ahorroAcumulado
            if (ahorroDelMes > 0) {
                newAhorroAcumulado += ahorroDelMes
            }

            // Actualizamos el usuario con el nuevo ahorro y la fecha de hoy como último cierre
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