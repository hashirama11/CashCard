package com.example.finanzas.ui.add_transaction

import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.UserMessage
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
    val currencies: List<Moneda> = emptyList(),
    val selectedCurrency: Moneda? = null,
    val isPending: Boolean = false,
    val completionDate: Date? = null,
    val tipoCompra: String? = null,
    val imageUri: String? = null,
    val userMessages: List<UserMessage> = emptyList()
)