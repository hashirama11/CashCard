package com.example.finanzas.model

data class BudgetCategoryDetail(
    val categoryName: String,
    val icon: String,
    val categoryType: String,
    val budgetedAmount: Double,
    val actualAmounts: Map<String, Double>,
    val categoryId: Int
) {
    val actualAmount: Double
        get() = actualAmounts.values.sum()
}
