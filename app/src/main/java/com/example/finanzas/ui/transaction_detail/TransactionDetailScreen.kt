package com.example.finanzas.ui.transaction_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.Moneda
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val transactionDetails by viewModel.transactionDetails.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Transacción") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        transactionDetails?.let { details ->
            val transaction = details.transaccion
            val category = details.categoria
            val isIncome = transaction.tipo == TipoTransaccion.INGRESO.name
            val amountColor = if (isIncome) AccentGreen else AccentRed
            val currencyFormat = NumberFormat.getCurrencyInstance().apply {
                currency = Currency.getInstance(if (transaction.moneda == Moneda.USD.name) "USD" else "VES")
                if (transaction.moneda == Moneda.VES.name) {
                    maximumFractionDigits = 2
                    (this as java.text.DecimalFormat).decimalFormatSymbols = java.text.DecimalFormatSymbols(Locale("es", "VE")).apply {
                        currencySymbol = "Bs."
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Monto y descripción
                Text(
                    text = currencyFormat.format(transaction.monto),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = amountColor
                )
                Text(
                    text = transaction.descripcion,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                // Fila de detalles
                DetailRow(label = "Categoría", value = category?.nombre ?: "Sin categoría")
                DetailRow(label = "Fecha", value = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "VE")).format(transaction.fecha))
                DetailRow(label = "Moneda", value = transaction.moneda)
                DetailRow(label = "Estado", value = transaction.estado.replaceFirstChar { it.uppercase() })
                Spacer(modifier = Modifier.weight(1f))
                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { onEditClick(transaction.id) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Editar") }
                    Button(
                        onClick = { showDeleteDialog = true }, // <-- Esto abre el diálogo
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Eliminar") }
                }
            }
        }
    }

    // --- CÓDIGO DEL DIÁLOGO DE ELIMINACIÓN (AHORA COMPLETO Y FUNCIONAL) ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar esta transacción? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        // 1. Llama a la función para borrar en el ViewModel
                        viewModel.deleteTransaction()
                        // 2. Cierra el diálogo
                        showDeleteDialog = false
                        // 3. Regresa a la pantalla anterior
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}