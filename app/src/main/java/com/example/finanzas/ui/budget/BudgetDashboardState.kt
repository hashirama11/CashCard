package com.example.finanzas.ui.budget

import com.example.finanzas.model.BudgetCategoryDetail

data class BudgetDashboardState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedMonth: Int,
    val selectedYear: Int,
    val monthlyGoalSummary: MonthlyGoalSummary = MonthlyGoalSummary(),
    val incomeCategories: List<BudgetCategoryDetail> = emptyList(),
    val expenseCategories: List<BudgetCategoryDetail> = emptyList(),
    val csvContent: String? = null
)

data class MonthlyGoalSummary(
    val savingsRate: Float = 0f,
    val balanceDelMes: Double = 0.0,
    val monthlyGoal: Double = 0.0,
    val totalIngresos: Double = 0.0,
    val totalGastos: Double = 0.0
)
