package com.example.finanzas.ui.all_transactions

import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails

// NUEVO: Enum para los tipos de agrupación
enum class GroupingType {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

// NUEVO: Data class para representar un grupo de transacciones (ej: "Hoy", "Ayer", "Agosto 2025")
data class TransactionGroup(
    val title: String,
    val transactions: List<TransactionWithDetails>
)

data class AllTransactionsState(
    val allTransactions: List<TransactionWithDetails> = emptyList(),
    // MODIFICADO: Esta es la lista que ahora usará la UI
    val groupedTransactions: List<TransactionGroup> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val filterType: TipoTransaccion? = null,
    // NUEVO: Estado para saber qué agrupación está seleccionada
    val selectedGrouping: GroupingType = GroupingType.DAILY
)
