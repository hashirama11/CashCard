package com.example.finanzas.ui.add_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.EstadoTransaccion
import com.example.finanzas.model.Moneda
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: FinanzasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state = _state.asStateFlow()

    private val transactionId: Int = savedStateHandle.get<String>("transactionId")?.toIntOrNull() ?: -1

    init {
        _state.update { it.copy(isEditing = transactionId != -1) }

        viewModelScope.launch {
            val categories = repository.getAllCategorias().first()
            _state.update { it.copy(allCategories = categories) }

            if (_state.value.isEditing) {
                val transactionToEdit = repository.getTransaccionById(transactionId).first()
                transactionToEdit?.let { tx ->
                    val category = categories.find { it.id == tx.categoriaId }
                    _state.update {
                        it.copy(
                            amount = tx.monto.toString(),
                            description = tx.descripcion,
                            selectedTransactionType = TipoTransaccion.valueOf(tx.tipo),
                            selectedCategory = category,
                            transactionDate = tx.fecha,
                            // --- CARGAMOS LOS NUEVOS DATOS ---
                            selectedCurrency = Moneda.valueOf(tx.moneda),
                            isPending = tx.estado == EstadoTransaccion.PENDIENTE.name
                        )
                    }
                }
            }
            filterCategories(_state.value.selectedTransactionType)
        }
    }

    fun onAmountChange(newAmount: String) { _state.update { it.copy(amount = newAmount) } }
    fun onDescriptionChange(newDescription: String) { _state.update { it.copy(description = newDescription) } }
    fun onCategorySelected(category: Categoria) { _state.update { it.copy(selectedCategory = category) } }
    // --- NUEVAS FUNCIONES ---
    fun onCurrencySelected(currency: Moneda) { _state.update { it.copy(selectedCurrency = currency) } }
    fun onPendingStatusChange(isPending: Boolean) { _state.update { it.copy(isPending = isPending) } }

    fun onTransactionTypeSelected(type: TipoTransaccion) {
        _state.update { it.copy(selectedTransactionType = type) }
        filterCategories(type)
    }

    private fun filterCategories(type: TipoTransaccion) {
        val filtered = _state.value.allCategories.filter { it.tipo == type.name }
        val currentCategory = _state.value.selectedCategory
        val isCurrentCategoryStillValid = filtered.any { it.id == currentCategory?.id }

        _state.update {
            it.copy(
                filteredCategories = filtered,
                selectedCategory = if (_state.value.isEditing && isCurrentCategoryStillValid) currentCategory else filtered.firstOrNull()
            )
        }
    }

    fun saveTransaction() {
        val currentState = _state.value
        val amountDouble = currentState.amount.toDoubleOrNull() ?: 0.0
        if (amountDouble <= 0 || currentState.description.isBlank() || currentState.selectedCategory == null) return

        val transactionToSave = Transaccion(
            id = if (currentState.isEditing) transactionId else 0,
            monto = amountDouble,
            // --- GUARDAMOS LOS NUEVOS DATOS ---
            moneda = currentState.selectedCurrency.name,
            descripcion = currentState.description,
            fecha = if (currentState.isEditing) currentState.transactionDate ?: Date() else Date(),
            tipo = currentState.selectedTransactionType.name,
            estado = if (currentState.isPending) EstadoTransaccion.PENDIENTE.name else EstadoTransaccion.CONCRETADO.name,
            categoriaId = currentState.selectedCategory.id
        )

        viewModelScope.launch {
            if (currentState.isEditing) {
                repository.updateTransaction(transactionToSave)
            } else {
                repository.insertTransaccion(transactionToSave)
            }
        }
    }
}