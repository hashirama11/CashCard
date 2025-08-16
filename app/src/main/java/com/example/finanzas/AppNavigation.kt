package com.example.finanzas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finanzas.ui.features.centerButton.CenterButtonRoute
import com.example.finanzas.ui.features.profile.UserProfileRoute
import com.example.finanzas.ui.features.profile.UserProfileViewModel
import com.example.finanzas.ui.features.profile.category.create.CategoryCreateRoute
import com.example.finanzas.ui.features.profile.category.delete.CategoryDeleteRoute
import com.example.finanzas.ui.features.profile.category.reset.ResetDatabaseRoute
import com.example.finanzas.ui.features.profile.category.update.CategoryListScreen
import com.example.finanzas.ui.features.profile.category.update.CategoryUpdateRoute
import com.example.finanzas.ui.screen.HomeRoute



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // 🏠 Home
        composable("home") {
            HomeRoute(
                onAgregarGasto = { navController.navigate("crearGasto") },
                onUsersClick = { navController.navigate("userProfile") },
                onStatsClick = { /* navegar a estadísticas */ },
                onSettingsClick = { /* navegar a settings */ }
            )
        }

        // ➕ Crear gasto
        composable("crearGasto") {
            CenterButtonRoute(onVolver = { navController.popBackStack() })
        }

        // Perfil de usuario
        // Perfil de usuario
        composable("userProfile") {
            UserProfileRoute(
                onHomeClick = { navController.navigate("home") },
                onStatsClick = { /* ... */ },
                onUsersClick = { /* ya estamos aquí */ },
                onSettingsClick = { /* ... */ },
                onAddCategoryClick = { navController.navigate("categoryCreate") },
                onUpdateCategoryClick = { navController.navigate("categoryUpdate") }, // ✅ lleva al update
                onDeleteCategoryClick = { navController.navigate("categoryDelete") }, // ✅ lleva al delete
                onResetDatabaseClick = { navController.navigate("resetDatabase") }   // ✅ lleva al reset
            )
        }



        // ➕ Crear categoría
        composable("categoryCreate") {
            CategoryCreateRoute(
                onHomeClick = { navController.navigate("home") },
                onStatsClick = { /* ... */ },
                onUsersClick = { navController.navigate("userProfile") },
                onSettingsClick = { /* ... */ }
            )
        }


        // Eliminar categoría
        composable("categoryDelete") {
            CategoryDeleteRoute(
                onHomeClick = { navController.navigate("home") },
                onStatsClick = { /* ... */ },
                onUsersClick = { navController.navigate("userProfile") },
                onSettingsClick = { /* ... */ }
            )
        }

        // Resetear BD
        composable("resetDatabase") {
            val viewModel: UserProfileViewModel = hiltViewModel()
            ResetDatabaseRoute(
                onHomeClick = { navController.navigate("home") },
                onStatsClick = { /* ... */ },
                onUsersClick = { navController.navigate("userProfile") },
                onSettingsClick = { /* ... */ },
                onConfirmReset = {
                    viewModel.resetearGastos()
                    navController.popBackStack() // vuelve al perfil después
                },
                onDismiss = {
                    navController.popBackStack() // si cancela, también vuelve
                }
            )
        }


    }
}
