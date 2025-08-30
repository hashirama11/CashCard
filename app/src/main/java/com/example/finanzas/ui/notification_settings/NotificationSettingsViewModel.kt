package com.example.finanzas.ui.notification_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.EstadoTransaccion
import com.example.finanzas.model.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationSettingsState())
    val state = _state.asStateFlow()

    init {
        val transactionsFlow = repository.getAllTransacciones()
        val categoriesFlow = repository.getAllCategorias()
        val monedasFlow = repository.getAllMonedas()

        combine(transactionsFlow, categoriesFlow, monedasFlow) { allTransactions, categories, monedas ->
            val categoriesMap = categories.associateBy { it.id }
            val monedasMap = monedas.associateBy { it.nombre }
            val pendingTransactions = allTransactions
                .filter { it.estado == EstadoTransaccion.PENDIENTE.name }
                .map { transaction ->
                    TransactionWithDetails(
                        transaccion = transaction,
                        categoria = categoriesMap[transaction.categoriaId],
                        moneda = monedasMap[transaction.moneda]
                    )
                }

            _state.update {
                it.copy(
                    pendingTransactions = pendingTransactions,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}
