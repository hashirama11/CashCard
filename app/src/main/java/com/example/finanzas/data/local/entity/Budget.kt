package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    indices = [Index(value = ["month", "year"], unique = true)]
)
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val month: Int,
    val year: Int
)
