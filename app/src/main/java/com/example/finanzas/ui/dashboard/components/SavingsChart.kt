package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.SavingsChartData

@Composable
fun SavingsChart(savingsChartData: SavingsChartData?) {
    if (savingsChartData == null || savingsChartData.points.size < 2) {
        // Not enough data to draw a chart
        return
    }

    val points = savingsChartData.points
    val color = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val xMin = points.first().date.time
        val xMax = points.last().date.time
        val yMin = points.minOf { it.amount }
        val yMax = points.maxOf { it.amount }

        val path = Path()
        points.forEachIndexed { index, point ->
            val x = (point.date.time - xMin).toFloat() / (xMax - xMin).toFloat() * size.width
            val y = (1 - (point.amount - yMin) / (yMax - yMin)) * size.height
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
