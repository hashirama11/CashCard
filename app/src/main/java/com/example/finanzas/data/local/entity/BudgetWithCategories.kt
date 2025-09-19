package com.example.finanzas.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithCategories(
    @Embedded val budget: Budget,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val budgetCategories: List<BudgetCategory>
)
