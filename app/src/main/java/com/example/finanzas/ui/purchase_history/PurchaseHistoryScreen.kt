package com.example.finanzas.ui.purchase_history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.finanzas.model.TransactionWithDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseHistoryScreen(
    viewModel: PurchaseHistoryViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.purchases.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay compras registradas.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.purchases) { purchase ->
                    PurchaseItem(purchase = purchase)
                }
            }
        }
    }
}

@Composable
fun PurchaseItem(purchase: TransactionWithDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            if (purchase.transaccion.imageUri != null) {
                AsyncImage(
                    model = purchase.transaccion.imageUri,
                    contentDescription = purchase.transaccion.descripcion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = purchase.transaccion.descripcion, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Monto: ${purchase.transaccion.monto} ${purchase.transaccion.moneda}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Tipo: ${purchase.transaccion.tipoCompra}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Categoría: ${purchase.categoria?.nombre}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
