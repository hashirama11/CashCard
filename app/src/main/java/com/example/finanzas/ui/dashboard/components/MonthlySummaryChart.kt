package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.MonthlySummary

@Composable
fun MonthlySummaryChart(monthlySummary: List<MonthlySummary>) {
    val maxAmount = monthlySummary.flatMap { listOf(it.income, it.expense) }.maxOrNull() ?: 1.0

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
private fun ColumnScope.Bar(
    value: Double,
    maxValue: Double,
    color: Color
) {
    val barHeight = (value / maxValue * 150).dp
    Box(
        modifier = Modifier
            .width(30.dp)
            .height(barHeight.coerceAtLeast(0.dp))
            .let { if (barHeight > 0.dp) it else it.height(2.dp) } // min height for visibility
            .let {
                if (value > 0) it.background(color) else it.background(Color.Gray)
            }
    )
}
