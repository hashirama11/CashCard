package com.example.finanzas.model

data class BudgetCategoryDetail(
    val categoryName: String,
    val icon: String,
    val categoryType: String,
    val budgetedAmount: Double,
    val actualAmount: Double,
    val categoryId: Int
)
