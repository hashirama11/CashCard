package com.example.finanzas.ui.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.finanzas.R

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    onAddCategoryClick: () -> Unit,
    onDeleteCategoryClick: () -> Unit,
    onResetDatabaseClick: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Foto de perfil
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.users),
                contentDescription = "Foto de usuario",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Datos del usuario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Juan Pérez", style = MaterialTheme.typography.titleLarge)
                Text(
                    "juan.perez@email.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Año: 2025",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Botón para agregar categoría
        Button(
            onClick = onAddCategoryClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar categoría", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Agregar categoría", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

                Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Botón para eliminar categoría
        Button(
            onClick = onDeleteCategoryClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), // Rojo
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar categoría", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Eliminar categoría", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Botón para restablecer base de datos de gastos
        Button(
            onClick = { showResetDialog = true }, // 👈 mostramos diálogo
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)), // Naranja
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Restablecer base de datos", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Restablecer los Gastos", color = Color.White)
        }
    }

    // 🔹 Diálogo de confirmación
    if (showResetDialog) {
        Dialog(onDismissRequest = { showResetDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Restablecer base de datos?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Esta acción eliminará todos los gastos registrados. ¿Deseas continuar?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { showResetDialog = false },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = {
                                onResetDatabaseClick() // 👈 ejecuta la acción
                                showResetDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), // rojo para peligro
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Confirmar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}



@Composable
@Preview
fun UserProfileScreenPreview() {
    UserProfileScreen(
        onAddCategoryClick = {},
        onDeleteCategoryClick = {},
        onResetDatabaseClick = {}
    )
}
