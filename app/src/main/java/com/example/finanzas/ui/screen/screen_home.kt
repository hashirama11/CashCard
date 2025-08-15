package com.example.finanzas.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.ui.features.dashboard_home.DashboardViewModel
import com.example.finanzas.ui.features.statistics_home.StatisticsViewModel
import com.example.finanzas.ui.features.welcome_user.WelcomeViewModel
import com.example.finanzas.R
import com.example.finanzas.ui.features.dashboard_home.DashboardScreen
import com.example.finanzas.ui.features.dashboard_home.DashboardUiState
import com.example.finanzas.ui.features.statistics_home.CategoriaConGasto
import com.example.finanzas.ui.features.statistics_home.StatisticsScreen
import com.example.finanzas.ui.features.statistics_home.StatisticsUiState
import com.example.finanzas.ui.features.welcome_user.WelcomeScreen
import com.example.finanzas.ui.features.welcome_user.WelcomeUiState

@Composable
fun HomeRoute(
    onAgregarGasto: () -> Unit,
    viewModelWelcome: WelcomeViewModel = hiltViewModel(),
    viewModelDashboard: DashboardViewModel = hiltViewModel(),
    viewModelStatistics: StatisticsViewModel = hiltViewModel()
) {
    val uiStateWelcome by viewModelWelcome.state.collectAsState()
    val uiStateDashboard by viewModelDashboard.state.collectAsState()
    val uiStateStatistics by viewModelStatistics.state.collectAsState()

    // Cargar datos iniciales
    LaunchedEffect(Unit) {
        viewModelWelcome.getUser()
        viewModelDashboard.obtenerSaldoActual()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarGasto,
                containerColor = Color(0xFF43A047),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Agregar gasto",
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            AppNavigationBar(
                onHomeClick = { /* Aquí luego iría navegación al home */ },
                onStatsClick = { /* Navegar a estadísticas */ },
                onUsersClick = { /* Navegar a usuarios */ },
                onSettingsClick = { /* Navegar a ajustes */ }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeScreen(state = uiStateWelcome)
            Spacer(modifier = Modifier.height(8.dp))
            DashboardScreen(state = uiStateDashboard)
            Spacer(modifier = Modifier.height(8.dp))
            StatisticsScreen(state = uiStateStatistics)
        }
    }
}

@Composable
fun AppNavigationBar(
    onHomeClick: () -> Unit,
    onStatsClick: () -> Unit,
    onUsersClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.home), contentDescription = "Home") },
            selected = true,
            onClick = onHomeClick
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.shopping), contentDescription = "Compras") },
            selected = false,
            onClick = onStatsClick
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.users), contentDescription = "Usuarios") },
            selected = false,
            onClick = onUsersClick
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.settings_24dp_1f1f1f_fill0_wght400_grad0_opsz24), contentDescription = "Configuración") },
            selected = false,
            onClick = onSettingsClick
        )
    }
}


@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(
        welcomeState = WelcomeUiState(
            user = "Juan Pérez"
        ),
        dashboardState = DashboardUiState(
            saldoActual = 1520.50
        ),
        statisticsState = StatisticsUiState(
            categoriasConGastos = listOf(
                CategoriaConGasto("Alimentos", 450.0),
                CategoriaConGasto("Transporte", 250.0),
                CategoriaConGasto("Ocio", 320.0),
                CategoriaConGasto("Otros", 100.0)
            )
        ),
        onAgregarGasto = {}
    )
}

@Composable
fun HomeScreen(
    welcomeState: WelcomeUiState,
    dashboardState: DashboardUiState,
    statisticsState: StatisticsUiState,
    onAgregarGasto: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarGasto,
                containerColor = Color(0xFF43A047),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Agregar gasto",
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            AppNavigationBar(
                onHomeClick = {},
                onStatsClick = {},
                onUsersClick = {},
                onSettingsClick = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeScreen(state = welcomeState)
            Spacer(modifier = Modifier.height(8.dp))
            DashboardScreen(state = dashboardState)
            Spacer(modifier = Modifier.height(8.dp))
            StatisticsScreen(state = statisticsState)
        }
    }
}
