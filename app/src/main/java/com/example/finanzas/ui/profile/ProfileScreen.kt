package com.example.finanzas.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.TemaApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCategoryManagementClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onCurrencySettingsClick: () -> Unit
) {
    val user by viewModel.userState.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    var name by remember { mutableStateOf(user?.nombre ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }

    LaunchedEffect(user) {
        user?.let {
            name = it.nombre
            email = it.email ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil y Configuración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleEditMode() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            user?.let {
                // --- PERFIL DE USUARIO ---
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(120.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Icon(
                        Icons.Default.AccountBox,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (editMode) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electrónico") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.updateProfile(name, email) }) {
                        Text("Guardar")
                    }
                } else {
                    Text(
                        text = it.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = it.email ?: "Sin correo electrónico",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                // --- FIN DE PERFIL ---

                Spacer(modifier = Modifier.height(24.dp))
                Divider()

                // --- SECCIÓN DE CONFIGURACIÓN ---
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Configuración",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge)
                        Switch(
                            checked = it.tema == TemaApp.OSCURO.name,
                            onCheckedChange = { isChecked ->
                                val newTheme = if (isChecked) TemaApp.OSCURO else TemaApp.CLARO
                                viewModel.updateTheme(newTheme)
                            }
                        )
                    }
                    Button(
                        onClick = onCategoryManagementClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Gestionar mis categorías")
                    }
                    Button(
                        onClick = onNotificationSettingsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Gestionar notificaciones")
                    }
                    Button(
                        onClick = onCurrencySettingsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Ajustes de Moneda")
                    }
                }
            }
        }
    }
}