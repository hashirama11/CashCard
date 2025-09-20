package com.example.finanzas.ui.budget

import com.example.finanzas.model.BudgetCategoryDetail
import java.util.Calendar

data class BudgetDashboardState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedDate: Calendar = Calendar.getInstance(),
    val hasBudget: Boolean = false,
    val budgetSummary: BudgetSummary = BudgetSummary(),
    val incomeCategories: List<BudgetCategoryDetail> = emptyList(),
    val expenseCategories: List<BudgetCategoryDetail> = emptyList(),
    val csvContent: String? = null
)
