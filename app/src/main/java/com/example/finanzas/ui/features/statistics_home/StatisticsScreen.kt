package com.example.finanzas.ui.features.statistics_home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.ui.screen.PieChartSampleGreen

// Clase para representar el estado de la pantalla de estadísticas
@Composable
fun StatisticsScreen(
    state: StatisticsUiState
) {
    val totalGastos = state.categoriasConGastos.sumOf { it.total }
    val proportions = if (totalGastos > 0) {
        state.categoriasConGastos.map { (it.total / totalGastos).toFloat() }
    } else {
        emptyList()
    }

    val colors = listOf(
        Color(0xFF43A047),
        Color(0xFF66BB6A),
        Color(0xFF81C784),
        Color(0xFFA5D6A7),
        Color(0xFFC8E6C9)
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            "Estadísticas",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1B5E20)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Distribución de gastos",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (proportions.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PieChartWithPercentages(
                            colors = colors.take(state.categoriasConGastos.size),
                            proportions = proportions
                        )

                        LegendList(
                            categorias = state.categoriasConGastos,
                            colors = colors,
                            total = totalGastos
                        )
                    }
                } else {
                    Text("No hay datos para mostrar", color = Color.Gray)
                }
            }
        }
    }
}


@Composable
fun PieChartWithPercentages(
    colors: List<Color>,
    proportions: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(150.dp)) {
        var startAngle = -90f
        proportions.forEachIndexed { index, proportion ->
            val sweepAngle = proportion * 360f
            drawArc(
                color = colors.getOrElse(index) { Color.Gray },
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )

            // Calcular posición para el texto
            val angle = startAngle + sweepAngle / 2
            val radius = size.minDimension / 3
            val percentage = (proportion * 100).toInt()

            if (percentage > 0) {
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "$percentage%",
                        (center.x + radius * kotlin.math.cos(Math.toRadians(angle.toDouble()))).toFloat(),
                        (center.y + radius * kotlin.math.sin(Math.toRadians(angle.toDouble()))).toFloat(),
                        android.graphics.Paint().apply {
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 30f
                            color = android.graphics.Color.WHITE
                            isFakeBoldText = true
                        }
                    )
                }
            }

            startAngle += sweepAngle
        }
    }
}

@Composable
fun LegendList(
    categorias: List<CategoriaConGasto>,
    colors: List<Color>,
    total: Double,
    modifier: Modifier = Modifier
) {
    LazyColumn( // 👈 reemplazamos Column por LazyColumn
        modifier = modifier
            .heightIn(max = 200.dp), // 👈 límite de altura (ej: 200dp)
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(categorias.size) { index ->
            val categoria = categorias[index]
            val porcentaje = if (total > 0) (categoria.total / total * 100) else 0.0

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(colors.getOrElse(index) { Color.Gray }, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${categoria.categoria}: ${"%.1f".format(porcentaje)}%",
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }
}





// Preview de la pantalla de estadísticas
@Composable
@Preview()
fun StatisticsScreenPreview() {
    StatisticsScreen(
        state = StatisticsUiState(
            categoriasConGastos = listOf(
                CategoriaConGasto("Categoría 1", 100.0),
                CategoriaConGasto("Categoría 2", 200.0),
                )
        )
    )
}