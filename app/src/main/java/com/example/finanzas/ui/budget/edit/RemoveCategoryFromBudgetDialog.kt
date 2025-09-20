package com.example.finanzas.ui.budget.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.data.local.entity.Categoria

@Composable
fun RemoveCategoryFromBudgetDialog(
    categoriesInBudget: List<Categoria>,
    onRemoveCategory: (Categoria) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<Categoria?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar Categoría del Presupuesto") },
        text = {
            LazyColumn {
                items(categoriesInBudget) { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategory = category }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedCategory == category,
                            onCheckedChange = { selectedCategory = category }
                        )
                        Text(text = category.nombre)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCategory?.let { onRemoveCategory(it) }
                    onDismiss()
                },
                enabled = selectedCategory != null
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
