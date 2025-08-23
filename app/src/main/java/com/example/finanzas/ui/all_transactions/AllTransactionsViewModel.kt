package com.example.finanzas.ui.all_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AllTransactionsState(
    val allTransactions: List<TransactionWithDetails> = emptyList(),
    val filteredTransactions: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val filterType: TipoTransaccion? = null
)

@HiltViewModel
class AllTransactionsViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AllTransactionsState())
    val state = _state.asStateFlow()

    private val transactionsFlow = repository.getAllTransacciones()
    private val categoriesFlow = repository.getAllCategorias()

    init {
        viewModelScope.launch {
            combine(transactionsFlow, categoriesFlow, _state) { transactions, categories, currentState ->
                val categoriesMap = categories.associateBy { it.id }
                val transactionsWithDetails = transactions.map { transaccion ->
                    TransactionWithDetails(transaccion, categoriesMap[transaccion.categoriaId])
                }

                // --- LÓGICA DE FILTRADO ---
                val filtered = transactionsWithDetails.filter { details ->
                    val matchesSearch = details.transaccion.descripcion.contains(currentState.searchQuery, ignoreCase = true) ||
                            details.categoria?.nombre?.contains(currentState.searchQuery, ignoreCase = true) == true
                    val matchesType = currentState.filterType == null || details.transaccion.tipo == currentState.filterType.name
                    matchesSearch && matchesType
                }

                currentState.copy(
                    allTransactions = transactionsWithDetails,
                    filteredTransactions = filtered,
                    isLoading = false
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onFilterTypeChange(type: TipoTransaccion?) {
        val newFilter = if (_state.value.filterType == type) null else type
        _state.update { it.copy(filterType = newFilter) }
    }
}