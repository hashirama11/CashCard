package com.example.finanzas.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        val transactionsFlow = repository.getAllTransacciones()
        val categoriesFlow = repository.getAllCategorias()
        val userFlow = repository.getUsuario()

        combine(transactionsFlow, categoriesFlow, userFlow) { transactions, categories, user ->
            val categoriesMap = categories.associateBy { it.id }
            val transactionsWithDetails = transactions.map { transaccion ->
                TransactionWithDetails(
                    transaccion = transaccion,
                    categoria = categoriesMap[transaccion.categoriaId]
                )
            }

            // --- LÓGICA DE CÁLCULO MULTIMONEDA ---
            val ingresos = transactions.filter { it.tipo == TipoTransaccion.INGRESO.name }
            val gastos = transactions.filter { it.tipo == TipoTransaccion.GASTO.name }

            val totalIngresosVes = ingresos.filter { it.moneda == Moneda.VES.name }.sumOf { it.monto }
            val totalIngresosUsd = ingresos.filter { it.moneda == Moneda.USD.name }.sumOf { it.monto }
            val totalGastosVes = gastos.filter { it.moneda == Moneda.VES.name }.sumOf { it.monto }
            val totalGastosUsd = gastos.filter { it.moneda == Moneda.USD.name }.sumOf { it.monto }

            val totalGastosConsolidados = transactions
                .filter { it.tipo == TipoTransaccion.GASTO.name }
                .sumOf { it.monto }

            val totalIngresosConsolidados = transactions
                .filter { it.tipo == TipoTransaccion.INGRESO.name }
                .sumOf { it.monto }


            // --- LÓGICA PARA GRÁFICOS (SE MANTIENE CONSOLIDADA) ---
            val expenseChartData = if (totalGastosConsolidados > 0) {
                transactionsWithDetails
                    .filter { it.transaccion.tipo == TipoTransaccion.GASTO.name && it.categoria != null }
                    .groupBy { it.categoria!! }
                    .mapValues { (_, txs) -> txs.sumOf { it.transaccion.monto } }
                    .map { (category, sum) ->
                        val percentage = (sum / totalGastosConsolidados).toFloat()
                        val color = PieChartColors[category.id % PieChartColors.size]
                        PieChartData(percentage, color, category.nombre)
                    }
                    .sortedByDescending { it.value }
            } else {
                emptyList()
            }

            val incomeChartData = if (totalIngresosConsolidados > 0) {
                transactionsWithDetails
                    .filter { it.transaccion.tipo == TipoTransaccion.INGRESO.name && it.categoria != null }
                    .groupBy { it.categoria!! }
                    .mapValues { (_, txs) -> txs.sumOf { it.transaccion.monto } }
                    .map { (category, sum) ->
                        val percentage = (sum / totalIngresosConsolidados).toFloat()
                        val color = PieChartColors[category.id % PieChartColors.size]
                        PieChartData(percentage, color, category.nombre)
                    }
                    .sortedByDescending { it.value }
            } else {
                emptyList()
            }

            _state.update {
                it.copy(
                    transactionsWithDetails = transactionsWithDetails,
                    totalIngresosVes = totalIngresosVes,
                    totalIngresosUsd = totalIngresosUsd,
                    totalGastosVes = totalGastosVes,
                    totalGastosUsd = totalGastosUsd,
                    userName = user?.nombre ?: "Usuario",
                    expenseChartData = expenseChartData,
                    incomeChartData = incomeChartData,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}