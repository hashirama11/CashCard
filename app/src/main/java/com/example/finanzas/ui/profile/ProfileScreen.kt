package com.example.finanzas.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.TemaApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val user by viewModel.userState.collectAsState()

    // Inicializa al usuario la primera vez que se entra en la pantalla
    LaunchedEffect(Unit) {
        viewModel.initializeUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil y Configuración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            user?.let {
                Text(text = "Hola, ${it.nombre}", style = MaterialTheme.typography.headlineMedium)

                Divider()

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
            }
        }
    }
}