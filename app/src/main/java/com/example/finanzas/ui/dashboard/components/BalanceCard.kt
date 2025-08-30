package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat

@Composable
fun BalanceCard(
    balancePrimario: Double,
    balanceSecundario: Double,
    symbolPrimario: String,
    symbolSecundario: String,
    type: TipoTransaccion
) {
    val numberFormat = NumberFormat.getNumberInstance().apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    val color = if (type == TipoTransaccion.INGRESO) AccentGreen else AccentRed
    val title = if (type == TipoTransaccion.INGRESO) "Ingresos del Mes" else "Gastos del Mes"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            AmountText(amount = "${symbolPrimario} ${numberFormat.format(balancePrimario)}", color = color)
            if (symbolSecundario.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                AmountText(amount = "${symbolSecundario} ${numberFormat.format(balanceSecundario)}", color = color)
            }
        }
    }
}

@Composable
private fun AmountText(amount: String, color: Color) {
    Text(
        text = amount,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = color
    )
}