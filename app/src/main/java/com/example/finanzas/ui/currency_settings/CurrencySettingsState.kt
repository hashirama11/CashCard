package com.example.finanzas.ui.currency_settings

import com.example.finanzas.data.local.entity.Moneda

data class CurrencySettingsState(
    val allCurrencies: List<Moneda> = emptyList(),
    val primaryCurrency: Moneda? = null,
    val secondaryCurrency: Moneda? = null,
    val isLoading: Boolean = true,
    val saveSuccess: Boolean = false
)
