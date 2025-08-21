package com.example.finanzas.ui.balance.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.finanzas.ui.balance.MonthlyFlow
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed

@Composable
fun MonthlyFlowChart(
    monthlyFlows: List<MonthlyFlow>
) {
    val maxFlow = remember(monthlyFlows) {
        (monthlyFlows.maxOfOrNull { maxOf(it.ingresos, it.gastos) } ?: 1f).coerceAtLeast(1f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        monthlyFlows.forEach { flow ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Bar(value = flow.ingresos, maxValue = maxFlow, color = AccentGreen.copy(alpha = 0.8f))
                    Bar(value = flow.gastos, maxValue = maxFlow, color = AccentRed.copy(alpha = 0.8f))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = flow.month, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun Bar(
    value: Float,
    maxValue: Float,
    color: androidx.compose.ui.graphics.Color
) {
    var targetHeight by remember { mutableStateOf(0f) }
    val animatedHeight by animateFloatAsState(
        targetValue = targetHeight,
        animationSpec = tween(1000), label = ""
    )
    LaunchedEffect(value) {
        targetHeight = value / maxValue
    }

    Box(
        modifier = Modifier
            .width(18.dp)
            .fillMaxHeight(fraction = animatedHeight)
            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            .background(color)
    )
}