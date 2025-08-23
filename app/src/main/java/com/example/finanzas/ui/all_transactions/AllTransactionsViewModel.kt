package com.example.finanzas.ui.all_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AllTransactionsViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AllTransactionsState())
    val state = _state.asStateFlow()

    private val transactionsFlow = repository.getAllTransacciones()
    private val categoriesFlow = repository.getAllCategorias()

    init {
        viewModelScope.launch {
            combine(transactionsFlow, categoriesFlow, _state) { transactions, categories, currentState ->
                val categoriesMap = categories.associateBy { it.id }
                val transactionsWithDetails = transactions.map { transaccion ->
                    TransactionWithDetails(transaccion, categoriesMap[transaccion.categoriaId])
                }

                val filtered = transactionsWithDetails.filter { details ->
                    val matchesSearch = details.transaccion.descripcion.contains(currentState.searchQuery, ignoreCase = true) ||
                            details.categoria?.nombre?.contains(currentState.searchQuery, ignoreCase = true) == true
                    val matchesType = currentState.filterType == null || details.transaccion.tipo == currentState.filterType.name
                    matchesSearch && matchesType
                }

                // NUEVO: Llamamos a la función para agrupar las transacciones filtradas
                val grouped = groupTransactions(filtered, currentState.selectedGrouping)

                currentState.copy(
                    allTransactions = transactionsWithDetails,
                    groupedTransactions = grouped, // Actualizamos la nueva lista
                    isLoading = false
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    // --- NUEVAS FUNCIONES ---

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onFilterTypeChange(type: TipoTransaccion?) {
        val newFilter = if (_state.value.filterType == type) null else type
        _state.update { it.copy(filterType = newFilter) }
    }

    fun onGroupingChange(grouping: GroupingType) {
        _state.update { it.copy(selectedGrouping = grouping) }
    }

    // Esta es la función principal que realiza la agrupación
    private fun groupTransactions(transactions: List<TransactionWithDetails>, grouping: GroupingType): List<TransactionGroup> {
        val sortedTransactions = transactions.sortedByDescending { it.transaccion.fecha }
        val locale = Locale("es", "VE")

        val groupedMap = when (grouping) {
            GroupingType.DAILY -> {
                val today = Calendar.getInstance()
                val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
                val dayFormatter = SimpleDateFormat("dd 'de' MMMM, yyyy", locale)

                sortedTransactions.groupBy {
                    val cal = Calendar.getInstance().apply { time = it.transaccion.fecha }
                    when {
                        isSameDay(cal, today) -> "Hoy"
                        isSameDay(cal, yesterday) -> "Ayer"
                        else -> dayFormatter.format(it.transaccion.fecha)
                    }
                }
            }
            GroupingType.WEEKLY -> {
                val weekFormatter = SimpleDateFormat("'Semana del' dd 'de' MMMM", locale)
                sortedTransactions.groupBy {
                    val cal = Calendar.getInstance().apply { time = it.transaccion.fecha }
                    cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                    weekFormatter.format(cal.time)
                }
            }
            GroupingType.MONTHLY -> {
                val monthFormatter = SimpleDateFormat("MMMM yyyy", locale)
                sortedTransactions.groupBy { monthFormatter.format(it.transaccion.fecha).replaceFirstChar { char -> char.uppercase() } }
            }
            GroupingType.YEARLY -> {
                val yearFormatter = SimpleDateFormat("yyyy", locale)
                sortedTransactions.groupBy { yearFormatter.format(it.transaccion.fecha) }
            }
        }

        return groupedMap.map { (title, trans) -> TransactionGroup(title, trans) }
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}