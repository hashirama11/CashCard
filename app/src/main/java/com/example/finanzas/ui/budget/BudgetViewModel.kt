package com.example.finanzas.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.domain.use_case.GetMonthlyBudgetDetailsUseCase
import com.example.finanzas.model.BudgetCategoryDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getMonthlyBudgetDetailsUseCase: GetMonthlyBudgetDetailsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<BudgetUiState>
    val uiState: StateFlow<BudgetUiState>

    init {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        _uiState = MutableStateFlow(BudgetUiState(selectedMonth = currentMonth, selectedYear = currentYear))
        uiState = _uiState.asStateFlow()
        loadBudgetDetails(currentMonth, currentYear)
    }

    fun loadBudgetDetails(month: Int, year: Int) {
        _uiState.update { it.copy(isLoading = true, selectedMonth = month, selectedYear = year) }

        getMonthlyBudgetDetailsUseCase(month, year)
            .onEach { details ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        budgetedCategories = details,
                        overallSummary = calculateSummary(details)
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading budget details: ${e.localizedMessage}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun calculateSummary(details: List<BudgetCategoryDetail>): BudgetSummary {
        val budgetedSpending = details.sumOf { it.budgetedAmount }
        val actualSpending = details.sumOf { it.actualSpending }

        // Placeholder for projected and actual income
        // This should be fetched from the budget entity or another source
        val projectedIncome = _uiState.value.overallSummary?.projectedIncome ?: 0.0
        val actualIncome = _uiState.value.overallSummary?.actualIncome ?: 0.0

        return BudgetSummary(
            projectedIncome = projectedIncome,
            actualIncome = actualIncome,
            budgetedSpending = budgetedSpending,
            actualSpending = actualSpending
        )
    }
}
