package com.example.finanzas.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails
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

            val totalIngresos = transactions
                .filter { it.tipo == TipoTransaccion.INGRESO.name }
                .sumOf { it.monto }

            val totalGastos = transactions
                .filter { it.tipo == TipoTransaccion.GASTO.name }
                .sumOf { it.monto }

            _state.update {
                it.copy(
                    transactionsWithDetails = transactionsWithDetails,
                    totalIngresos = totalIngresos,
                    totalGastos = totalGastos,
                    userName = user?.nombre ?: "Usuario",
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}