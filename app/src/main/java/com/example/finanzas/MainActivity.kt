package com.example.finanzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.finanzas.ui.navigation.AppNavigation
import com.example.finanzas.ui.navigation.AppScreens
import com.example.finanzas.ui.theme.FinanzasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by mainViewModel.isDarkTheme.collectAsStateWithLifecycle()

            FinanzasTheme(darkTheme = isDarkTheme) { // El tema ahora es dinámico
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomAppBar {
                            val items = listOf(
                                BottomNavItem("Dashboard", Icons.Default.Home, AppScreens.Dashboard.route),
                                BottomNavItem("Perfil", Icons.Default.Person, AppScreens.Profile.route)
                            )
                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = { Text(item.title) },
                                    selected = false, // Lo haremos dinámico después
                                    onClick = { navController.navigate(item.route) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(navController = navController)
                    }
                }
            }
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)