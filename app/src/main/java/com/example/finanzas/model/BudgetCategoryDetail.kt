package com.example.finanzas.model

data class BudgetCategoryDetail(
    val categoryName: String,
    val icon: String,
    val budgetedAmount: Double,
    val actualSpending: Double,
    val categoryId: Int
)
