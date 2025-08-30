package com.example.finanzas.ui.purchase_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PurchaseHistoryState())
    val state = _state.asStateFlow()

    init {
        val transactionsFlow = repository.getAllTransacciones()
        val categoriesFlow = repository.getAllCategorias()
        val monedasFlow = repository.getAllMonedas()

        combine(transactionsFlow, categoriesFlow, monedasFlow) { allTransactions, categories, monedas ->
            val categoriesMap = categories.associateBy { it.id }
            val monedasMap = monedas.associateBy { it.nombre }
            val allPurchases = allTransactions
                .filter { it.tipo == TipoTransaccion.COMPRA.name }
                .map { transaction ->
                    TransactionWithDetails(
                        transaccion = transaction,
                        categoria = categoriesMap[transaction.categoriaId],
                        moneda = monedasMap[transaction.moneda]
                    )
                }

            _state.update {
                it.copy(
                    allPurchases = allPurchases,
                    filteredPurchases = filterPurchases(allPurchases, it.selectedDate),
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onDateSelected(date: Date?) {
        _state.update {
            it.copy(
                selectedDate = date,
                filteredPurchases = filterPurchases(it.allPurchases, date)
            )
        }
    }

    private fun filterPurchases(
        purchases: List<TransactionWithDetails>,
        selectedDate: Date?
    ): List<TransactionWithDetails> {
        if (selectedDate == null) {
            return purchases
        }
        val calendar = Calendar.getInstance()
        return purchases.filter {
            calendar.time = it.transaccion.fecha
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            calendar.time = selectedDate
            val selectedYear = calendar.get(Calendar.YEAR)
            val selectedMonth = calendar.get(Calendar.MONTH)
            val selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

            year == selectedYear && month == selectedMonth && day == selectedDay
        }
    }
}
