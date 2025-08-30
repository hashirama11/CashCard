package com.example.finanzas.ui.currency_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.repository.FinanzasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCustomCurrencyViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    fun addCurrency(name: String, symbol: String, rate: Double) {
        if (name.isBlank() || symbol.isBlank() || rate <= 0) {
            return
        }
        viewModelScope.launch {
            val newCurrency = Moneda(
                nombre = name,
                simbolo = symbol,
                tasa_conversion = rate
            )
            repository.insertMoneda(newCurrency)
        }
    }
}
