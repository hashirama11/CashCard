package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.MonthlySummary

@Composable
fun MonthlySummaryChart(monthlySummary: List<MonthlySummary>) {
    val maxAmount = monthlySummary.flatMap { listOf(it.income, it.expense) }.maxOrNull()?.coerceAtLeast(1.0) ?: 1.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        monthlySummary.forEach { summary ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Bar(
                        value = summary.income,
                        maxValue = maxAmount,
                        color = Color.Green
                    )
                    Bar(
                        value = summary.expense,
                        maxValue = maxAmount,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = summary.month, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun Bar(
    value: Double,
    maxValue: Double,
    color: Color
) {
    val barHeight = (value / maxValue * 150).dp
    Box(
        modifier = Modifier
            .width(30.dp)
            .height(barHeight.coerceAtLeast(0.dp))
            .background(color)
    )
}
