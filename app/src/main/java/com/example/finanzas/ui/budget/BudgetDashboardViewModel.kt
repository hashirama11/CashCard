package com.example.finanzas.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.domain.use_case.BudgetDetails
import com.example.finanzas.domain.use_case.ExportBudgetUseCase
import com.example.finanzas.domain.use_case.GetMonthlyBudgetDetailsUseCase
import com.example.finanzas.domain.use_case.UpdateCategoryBudgetUseCase
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetDashboardViewModel @Inject constructor(
    private val getMonthlyBudgetDetailsUseCase: GetMonthlyBudgetDetailsUseCase,
    private val updateCategoryBudgetUseCase: UpdateCategoryBudgetUseCase,
    private val exportBudgetUseCase: ExportBudgetUseCase,
    private val repository: FinanzasRepository // For monthly goal part
) : ViewModel() {

    private val _uiState: MutableStateFlow<BudgetDashboardState>
    val uiState get() = _uiState.asStateFlow()

    init {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        _uiState = MutableStateFlow(BudgetDashboardState(selectedMonth = currentMonth, selectedYear = currentYear))

        loadDataForMonth(currentMonth, currentYear)
    }

    private fun loadDataForMonth(month: Int, year: Int) {
        _uiState.update { it.copy(isLoading = true, selectedMonth = month, selectedYear = year) }

        // Combine budget details and monthly goal data
        val budgetDetailsFlow = getMonthlyBudgetDetailsUseCase(month, year)
        val userFlow = repository.getUsuario()
        val transactionsFlow = repository.getAllTransacciones() // This could be optimized to fetch only for the month

        combine(budgetDetailsFlow, userFlow, transactionsFlow) { details, user, transactions ->
            // Monthly Goal Calculation
            val monthlyGoalSummary = user?.let {
                val currentMonthTransactions = transactions.filter { tx ->
                    val cal = Calendar.getInstance().apply { time = tx.fecha }
                    cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
                }
                val ingresos = currentMonthTransactions.filter { it.tipo == TipoTransaccion.INGRESO.name }.sumOf { it.monto }
                val gastos = currentMonthTransactions.filter { it.tipo == TipoTransaccion.GASTO.name }.sumOf { it.monto }
                val balance = ingresos - gastos
                val rate = if (ingresos > 0) (balance / ingresos).toFloat() else 0f

                MonthlyGoalSummary(
                    savingsRate = rate.coerceIn(0f, 1f),
                    balanceDelMes = balance,
                    monthlyGoal = it.objetivoAhorroMensual,
                    totalIngresos = ingresos,
                    totalGastos = gastos
                )
            } ?: MonthlyGoalSummary()

            // Update state with both budget details and monthly goal summary
            _uiState.update {
                it.copy(
                    isLoading = false,
                    incomeCategories = details.incomeCategories,
                    expenseCategories = details.expenseCategories,
                    monthlyGoalSummary = monthlyGoalSummary
                )
            }
        }.onEach {
            // This space can be used if we need to react to the combined flow
        }.launchIn(viewModelScope)
    }

    fun onMonthYearChanged(month: Int, year: Int) {
        loadDataForMonth(month, year)
    }

    fun updateBudgetForCategory(categoryId: Int, amount: Double) {
        viewModelScope.launch {
            val currentState = _uiState.value
            updateCategoryBudgetUseCase(
                year = currentState.selectedYear,
                month = currentState.selectedMonth,
                categoryId = categoryId,
                amount = amount
            )
            // Data will reload automatically due to the reactive flows
        }
    }

    fun onExportClicked() {
        val currentState = _uiState.value
        val csv = exportBudgetUseCase(
            incomeCategories = currentState.incomeCategories,
            expenseCategories = currentState.expenseCategories
        )
        _uiState.update { it.copy(csvContent = csv) }
    }

    fun onExportHandled() {
        _uiState.update { it.copy(csvContent = null) }
    }
}
