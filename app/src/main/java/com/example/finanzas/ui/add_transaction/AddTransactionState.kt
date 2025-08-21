package com.example.finanzas.ui.add_transaction

import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.model.EstadoTransaccion
import com.example.finanzas.model.Moneda
import com.example.finanzas.model.TipoTransaccion
import java.util.Date

data class AddTransactionState(
    val allCategories: List<Categoria> = emptyList(),
    val filteredCategories: List<Categoria> = emptyList(),
    val selectedCategory: Categoria? = null,
    val selectedTransactionType: TipoTransaccion = TipoTransaccion.GASTO,
    val amount: String = "",
    val description: String = "",
    val isEditing: Boolean = false,
    val transactionDate: Date? = null,
    // --- NUEVOS CAMPOS ---
    val selectedCurrency: Moneda = Moneda.VES,
    val isPending: Boolean = false
)