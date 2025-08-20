package com.example.finanzas.ui.add_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.EstadoTransaccion
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    fun saveTransaction(
        amount: Double,
        description: String,
        type: TipoTransaccion
    ) {
        if (amount <= 0 || description.isBlank()) {
            // TODO: Mostrar error al usuario
            return
        }

        viewModelScope.launch {
            val newTransaction = Transaccion(
                monto = amount,
                moneda = "VES", // Moneda por defecto
                descripcion = description,
                fecha = Date(), // Fecha actual
                tipo = type.name,
                estado = EstadoTransaccion.CONCRETADO.name,
                categoriaId = null // TODO: Implementar selección de categoría
            )
            repository.insertTransaccion(newTransaction)
        }
    }
}