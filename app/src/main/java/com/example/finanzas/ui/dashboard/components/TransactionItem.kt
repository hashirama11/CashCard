package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import com.example.finanzas.ui.util.getIconResource
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.unit.dp

@Composable
fun TransactionItem(
    transactionDetails: TransactionWithDetails,
    onClick: () -> Unit
) {
    val transaction = transactionDetails.transaccion
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "VE"))
    val amountColor = if (transaction.tipo == TipoTransaccion.INGRESO.name) AccentGreen else AccentRed
    val sign = if (transaction.tipo == TipoTransaccion.INGRESO.name) "+" else "-"
    val iconRes = getIconResource(iconName = transactionDetails.categoria?.icono)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = transactionDetails.categoria?.nombre,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = transactionDetails.categoria?.nombre ?: "Sin Categoría",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        Text(
            text = "$sign${currencyFormat.format(transaction.monto)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}