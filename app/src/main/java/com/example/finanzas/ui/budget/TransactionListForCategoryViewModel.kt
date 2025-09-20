package com.example.finanzas.ui.budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionListForCategoryState(
    val transactions: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class TransactionListForCategoryViewModel @Inject constructor(
    private val finanzasRepository: FinanzasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionListForCategoryState())
    val uiState = _uiState.asStateFlow()

    private val categoryId: Int = savedStateHandle.get<Int>("categoryId")!!

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            val category = finanzasRepository.getCategoriaById(categoryId)
            finanzasRepository.getAllTransacciones().collect { allTransactions ->
                val filteredTransactions = allTransactions
                    .filter { it.categoriaId == categoryId }
                    .map { transaction ->
                        TransactionWithDetails(
                            id = transaction.id,
                            amount = transaction.monto,
                            date = transaction.fecha,
                            description = transaction.descripcion,
                            categoryName = category?.nombre ?: "Categoría",
                            iconName = category?.icono ?: "DEFAULT",
                            currency = transaction.moneda,
                            type = transaction.tipo
                        )
                    }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        transactions = filteredTransactions
                    )
                }
            }
        }
    }
}
