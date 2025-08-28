package com.example.finanzas.ui.notification_settings

import com.example.finanzas.model.TransactionWithDetails

data class NotificationSettingsState(
    val pendingTransactions: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = true
)
