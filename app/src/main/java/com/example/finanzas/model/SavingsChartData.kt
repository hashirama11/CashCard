package com.example.finanzas.model

import java.util.Date

data class SavingsChartData(
    val points: List<SavingsDataPoint>
)

data class SavingsDataPoint(
    val date: Date,
    val amount: Float
)
