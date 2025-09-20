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
    val actualIncome: Map<String, Double> = emptyMap(),
    val budgetedExpenses: Double = 0.0,
    val actualExpenses: Map<String, Double> = emptyMap(),
) {
    val totalActualIncome: Double
        get() = actualIncome.values.sum()

    val totalActualExpenses: Double
        get() = actualExpenses.values.sum()
}
