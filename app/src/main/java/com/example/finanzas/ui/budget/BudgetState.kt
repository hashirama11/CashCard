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
    val projectedIncome: Double = 0.0,
    val actualIncome: Double = 0.0,
    val budgetedExpenses: Double = 0.0,
    val actualExpenses: Double = 0.0,
    val balance: Double = 0.0
)
