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
import java.util.Currency
import java.util.Locale

@Composable
fun BalanceCard(
    balanceVes: Double,
    balanceUsd: Double,
    type: TipoTransaccion
) {
    val vesFormat = NumberFormat.getCurrencyInstance(Locale("es", "VE")).apply {
        currency = Currency.getInstance("VES")
        maximumFractionDigits = 2
    }
    val usdFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        currency = Currency.getInstance("USD")
    }

    val title = if (type == TipoTransaccion.INGRESO) "Total Ingresos" else "Total Gastos"
    val color = if (type == TipoTransaccion.INGRESO) AccentGreen else AccentRed

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
            // Monto en USD
            AmountText(amount = usdFormat.format(balanceUsd), color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Divider()
            Spacer(modifier = Modifier.height(4.dp))
            // Monto en VES
            AmountText(amount = vesFormat.format(balanceVes), color = color)
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