package com.example.finanzas.ui.add_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.EstadoTransaccion
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class AddTransactionState(
    val allCategories: List<Categoria> = emptyList(),
    val filteredCategories: List<Categoria> = emptyList(),
    val selectedCategory: Categoria? = null,
    val selectedTransactionType: TipoTransaccion = TipoTransaccion.GASTO
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state = _state.asStateFlow()

    init {
        repository.getAllCategorias()
            .onEach { categories ->
                _state.value = _state.value.copy(allCategories = categories)
                filterCategories(_state.value.selectedTransactionType)
            }
            .launchIn(viewModelScope)
    }

    fun onTransactionTypeSelected(type: TipoTransaccion) {
        _state.value = _state.value.copy(selectedTransactionType = type)
        filterCategories(type)
    }

    private fun filterCategories(type: TipoTransaccion) {
        val filtered = _state.value.allCategories.filter { it.tipo == type.name }
        _state.value = _state.value.copy(
            filteredCategories = filtered,
            selectedCategory = filtered.firstOrNull()
        )
    }

    fun onCategorySelected(category: Categoria) {
        _state.value = _state.value.copy(selectedCategory = category)
    }

    fun saveTransaction(
        amount: Double,
        description: String,
        type: TipoTransaccion,
        category: Categoria?
    ) {
        if (amount <= 0 || description.isBlank() || category == null) return

        viewModelScope.launch {
            val newTransaction = Transaccion(
                monto = amount,
                moneda = "VES",
                descripcion = description,
                fecha = Date(),
                tipo = type.name,
                estado = EstadoTransaccion.CONCRETADO.name,
                categoriaId = category.id
            )
            repository.insertTransaccion(newTransaction)
        }
    }
}