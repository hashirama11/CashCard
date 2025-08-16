package com.example.finanzas.ui.features.profile.category.delete

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.categoria.CategoriaEntity


@Composable
fun CategoryDeleteScreen(
    modifier: Modifier = Modifier,
    categorias: List<CategoriaEntity>,  // ✅ lista de categorías
    categoriaSeleccionada: CategoriaEntity?,
    onCategoriaClick: (CategoriaEntity) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDialog: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Eliminar Categoría",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Lista de categorías (solo nombre)
        LazyColumn {
            items(categorias) { categoria ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            onCategoriaClick(categoria)
                            showDialog = true
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = categoria.nombre,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

    // 🔹 Diálogo de confirmación
    if (showDialog && categoriaSeleccionada != null) {
        AlertDialog(
            onDismissRequest = { onDismissDialog(); showDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que deseas eliminar la categoría '${categoriaSeleccionada.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmDelete()
                        showDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismissDialog()
                        showDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
