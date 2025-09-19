package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "budget_categories",
    foreignKeys = [
        ForeignKey(
            entity = Budget::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BudgetCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val budgetId: Int,
    val categoryId: Int,
    val budgetedAmount: Double
)
