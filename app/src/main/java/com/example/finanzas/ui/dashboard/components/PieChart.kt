package com.example.finanzas.ui.dashboard.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.model.PieChartData
import kotlin.math.roundToInt

@Composable
fun PieChartCard(
    chartData: List<PieChartData>,
    title: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))

            if (chartData.isEmpty()) {
                Text("No hay datos para mostrar.")
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedPieChart(chartData = chartData, modifier = Modifier.weight(1f)) // <-- USAMOS EL NUEVO COMPOSABLE
                    Spacer(modifier = Modifier.width(16.dp))
                    PieChartLegend(chartData = chartData, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun AnimatedPieChart(chartData: List<PieChartData>, modifier: Modifier = Modifier) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(chartData) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Canvas(modifier = modifier.size(150.dp)) {
        var startAngle = -90f
        chartData.forEach { data ->
            val sweepAngle = data.value * 360f * animationProgress.value // <-- APLICAMOS LA ANIMACIÓN
            drawArc(
                color = data.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 60f)
            )
            startAngle += data.value * 360f // El ángulo inicial no se anima para mantener la posición
        }
    }
}


@Composable
private fun PieChartLegend(chartData: List<PieChartData>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.heightIn(max = 150.dp)) {
        items(chartData.size) { index ->
            val data = chartData[index]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color = data.color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${data.categoryName} (${(data.value * 100).roundToInt()}%)",
                    fontSize = 14.sp
                )
            }
        }
    }
}