package com.example.finanzas.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.domain.use_case.ExportBudgetUseCase
import com.example.finanzas.domain.use_case.GetMonthlyBudgetDetailsUseCase
import com.example.finanzas.domain.use_case.UpdateCategoryBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetDashboardState())
    val uiState get() = _uiState.asStateFlow()

    init {
        loadDataForDate(Calendar.getInstance())
    }

    private fun loadDataForDate(date: Calendar) {
        _uiState.update { it.copy(isLoading = true, selectedDate = date) }

        val month = date.get(Calendar.MONTH)
        val year = date.get(Calendar.YEAR)

        getMonthlyBudgetDetailsUseCase(month, year).onEach { details ->
            val hasBudget = details.incomeCategories.isNotEmpty() || details.expenseCategories.isNotEmpty()

            val projectedIncome = details.incomeCategories.sumOf { it.budgetedAmount }
            val actualIncome = details.incomeCategories.sumOf { it.actualAmount }
            val budgetedExpenses = details.expenseCategories.sumOf { it.budgetedAmount }
            val actualExpenses = details.expenseCategories.sumOf { it.actualAmount }
            val balance = actualIncome - actualExpenses

            _uiState.update {
                it.copy(
                    isLoading = false,
                    hasBudget = hasBudget,
                    incomeCategories = details.incomeCategories,
                    expenseCategories = details.expenseCategories,
                    budgetSummary = BudgetSummary(
                        projectedIncome = projectedIncome,
                        actualIncome = actualIncome,
                        budgetedExpenses = budgetedExpenses,
                        actualExpenses = actualExpenses,
                        balance = balance
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onDateChanged(date: Calendar) {
        loadDataForDate(date)
    }

    fun onPreviousMonth() {
        val newDate = _uiState.value.selectedDate.clone() as Calendar
        newDate.add(Calendar.MONTH, -1)
        loadDataForDate(newDate)
    }

    fun onNextMonth() {
        val newDate = _uiState.value.selectedDate.clone() as Calendar
        newDate.add(Calendar.MONTH, 1)
        loadDataForDate(newDate)
    }


    fun updateBudgetForCategory(categoryId: Int, amount: Double) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val month = currentState.selectedDate.get(Calendar.MONTH)
            val year = currentState.selectedDate.get(Calendar.YEAR)
            updateCategoryBudgetUseCase(
                year = year,
                month = month,
                categoryId = categoryId,
                amount = amount
            )
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
