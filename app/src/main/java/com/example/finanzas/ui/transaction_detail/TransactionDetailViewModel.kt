package com.example.finanzas.ui.transaction_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val repository: FinanzasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Int = savedStateHandle.get<Int>("transactionId") ?: 0

    val transactionDetails = combine(
        repository.getTransaccionById(transactionId),
        repository.getAllCategorias()
    ) { transaction, categories ->
        if (transaction == null) return@combine null
        val category = categories.find { it.id == transaction.categoriaId }
        TransactionWithDetails(transaction, category)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    fun deleteTransaction() {
        viewModelScope.launch {
            repository.deleteTransaccionById(transactionId)
        }
    }
}
