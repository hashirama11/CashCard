package com.example.finanzas

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finanzas.ui.features.centerButton.CenterButtonRoute
import com.example.finanzas.ui.features.centerButton.CrearGastoRoute
import com.example.finanzas.ui.features.dashboard_home.DashboardRoute
import com.example.finanzas.ui.screen.HomeRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            HomeRoute(
                onAgregarGasto = { navController.navigate("crear_gasto") }
            )
        }
        composable("crear_gasto") {
            CrearGastoRoute(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}



