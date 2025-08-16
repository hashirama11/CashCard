package com.example.finanzas.ui.features.profile.category.update

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanzas.R
import com.example.finanzas.model.categoria.CategoriaEntity


@Composable
fun CategoryUpdateScreen(
    categorias: List<CategoriaEntity>,
    categoriaSeleccionada: CategoriaEntity?,
    onCategoriaClick: (CategoriaEntity) -> Unit,
    nombre: String,
    onNombreChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onActualizarClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Actualizar Categoría",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Lista de categorías (solo nombre)
        LazyColumn {
            items(categorias) { categoria ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onCategoriaClick(categoria) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (categoriaSeleccionada?.id == categoria.id)
                            Color(0xFFE0F7FA) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(categoria.nombre, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Campo de texto para actualizar el nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChanged,
            label = { Text("Nuevo nombre de la categoría") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Botón actualizar
        Button(
            onClick = onActualizarClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
            shape = RoundedCornerShape(12.dp),
            enabled = categoriaSeleccionada != null
        ) {
            Text("Actualizar categoría", color = Color.White)
        }
    }
}


@Composable
fun CategoryListScreen(
    categorias: List<CategoriaEntity>,
    onCategoriaClick: (CategoriaEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Selecciona una categoría",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        categorias.forEach { categoria ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onCategoriaClick(categoria) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = categoria.icono),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0xFF43A047)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        categoria.nombre,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

