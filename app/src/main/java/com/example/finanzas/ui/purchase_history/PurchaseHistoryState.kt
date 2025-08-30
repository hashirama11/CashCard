package com.example.finanzas.ui.purchase_history

import com.example.finanzas.model.TransactionWithDetails
import java.util.Date

data class PurchaseHistoryState(
    val allPurchases: List<TransactionWithDetails> = emptyList(),
    val filteredPurchases: List<TransactionWithDetails> = emptyList(),
    val selectedDate: Date? = null,
    val isLoading: Boolean = true
)
