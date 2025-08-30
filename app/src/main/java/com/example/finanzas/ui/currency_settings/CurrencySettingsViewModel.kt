package com.example.finanzas.ui.currency_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.repository.FinanzasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencySettingsViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CurrencySettingsState())
    val state = _state.asStateFlow()

    init {
        val currenciesFlow = repository.getAllMonedas()
        val userFlow = repository.getUsuario()

        combine(currenciesFlow, userFlow) { currencies, user ->
            val primary = currencies.find { it.nombre == user?.monedaPrincipal }
            val secondary = currencies.find { it.nombre == user?.monedaSecundaria }
            _state.update {
                it.copy(
                    allCurrencies = currencies,
                    primaryCurrency = primary,
                    secondaryCurrency = secondary,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onPrimaryCurrencySelected(moneda: Moneda) {
        _state.update { it.copy(primaryCurrency = moneda) }
    }

    fun onSecondaryCurrencySelected(moneda: Moneda) {
        _state.update { it.copy(secondaryCurrency = moneda) }
    }

    fun saveCurrencySettings() {
        viewModelScope.launch {
            val currentState = _state.value
            val user = repository.getUsuario().first()
            if (user != null && currentState.primaryCurrency != null) {
                val updatedUser = user.copy(
                    monedaPrincipal = currentState.primaryCurrency.nombre,
                    monedaSecundaria = currentState.secondaryCurrency?.nombre
                )
                repository.upsertUsuario(updatedUser)
                _state.update { it.copy(saveSuccess = true) }
            }
        }
    }

    fun resetSaveSuccess() {
        _state.update { it.copy(saveSuccess = false) }
    }
}
