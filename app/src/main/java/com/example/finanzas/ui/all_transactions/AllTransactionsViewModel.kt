package com.example.finanzas.ui.all_transactions



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AllTransactionsState(
    val allTransactions: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class AllTransactionsViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AllTransactionsState())
    val state = _state.asStateFlow()

    init {
        val transactionsFlow = repository.getAllTransacciones()
        val categoriesFlow = repository.getAllCategorias()

        combine(transactionsFlow, categoriesFlow) { transactions, categories ->
            val categoriesMap = categories.associateBy { it.id }
            val transactionsWithDetails = transactions.map { transaccion ->
                TransactionWithDetails(
                    transaccion = transaccion,
                    categoria = categoriesMap[transaccion.categoriaId]
                )
            }
            _state.update {
                it.copy(
                    allTransactions = transactionsWithDetails,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}