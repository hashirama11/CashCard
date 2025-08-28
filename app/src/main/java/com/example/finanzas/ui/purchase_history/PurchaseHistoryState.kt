package com.example.finanzas.ui.purchase_history

import com.example.finanzas.model.TransactionWithDetails

data class PurchaseHistoryState(
    val purchases: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = true
)
