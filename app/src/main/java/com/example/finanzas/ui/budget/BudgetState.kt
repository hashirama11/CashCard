package com.example.finanzas.ui.budget

import com.example.finanzas.model.BudgetCategoryDetail

data class BudgetUiState(
    val isLoading: Boolean = true,
    val overallSummary: BudgetSummary? = null,
    val budgetedCategories: List<BudgetCategoryDetail> = emptyList(),
    val error: String? = null,
    val selectedMonth: Int,
    val selectedYear: Int
)

data class BudgetSummary(
    val projectedIncome: Double,
    val actualIncome: Double,
    val budgetedSpending: Double,
    val actualSpending: Double
)
