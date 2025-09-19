package com.example.finanzas

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finanzas.ui.auth.BiometricAuthenticator
import com.example.finanzas.ui.navigation.AppNavigation
import com.example.finanzas.ui.navigation.AppScreens
import com.example.finanzas.ui.theme.FinanzasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() { // Hereda de AppCompatActivity
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            // Interruptor para la autenticación biométrica
            val BIOMETRIC_AUTH_ENABLED = true

            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
            val isAuthenticated by mainViewModel.isAuthenticated.collectAsStateWithLifecycle()
            var showAuthErrorDialog by remember { mutableStateOf(false) }

            val biometricAuthenticator = remember { BiometricAuthenticator(this) }

            fun promptAuth() {
                biometricAuthenticator.promptBiometricAuth(
                    title = "Autenticación Requerida",
                    subtitle = "Desbloquea para acceder a tus finanzas",
                    onSuccess = {
                        mainViewModel.onAuthenticationSuccess()
                    },
                    onError = { _, _ ->
                        showAuthErrorDialog = true
                    }
                )
            }

            FinanzasTheme(darkTheme = uiState.isDarkTheme) {
                val authBypassed = !BIOMETRIC_AUTH_ENABLED
                val showContent = uiState.onboardingCompleted == false || isAuthenticated || authBypassed

                if (showContent) {
                    uiState.onboardingCompleted?.let {
                        val startDestination = if (it) AppScreens.Dashboard.route else AppScreens.Onboarding.route
                        MainScreen(startDestination)
                    }
                } else {
                    // Onboarding completo, pero no autenticado (y la autenticación está habilitada)
                    LaunchedEffect(uiState.onboardingCompleted) {
                        if (uiState.onboardingCompleted == true) {
                            promptAuth()
                        }
                    }

                    // Muestra un indicador de carga mientras se espera la autenticación
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                if (showAuthErrorDialog) {
                    AlertDialog(
                        onDismissRequest = { /* Evitar que se cierre al tocar fuera */ },
                        title = { Text("Autenticación Fallida") },
                        text = { Text("No se pudo verificar tu identidad. Por favor, inténtalo de nuevo para continuar.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showAuthErrorDialog = false
                                    promptAuth()
                                }
                            ) {
                                Text("Reintentar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { finish() }) {
                                Text("Salir")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(startDestination: String) {
    val navController = rememberNavController()
    var showBottomBar by rememberSaveable { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        AppScreens.Dashboard.route,
        AppScreens.HistoricalBalance.route,
        AppScreens.Profile.route,
        AppScreens.Budget.route -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}

@Composable
fun AppBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Inicio", Icons.Default.Home, AppScreens.Dashboard.route),
        BottomNavItem("Presupuesto", Icons.Default.Wallet, AppScreens.Budget.route),
        BottomNavItem("Histórico", Icons.Default.AccountBox, AppScreens.HistoricalBalance.route),
        BottomNavItem("Perfil", Icons.Default.Person, AppScreens.Profile.route)
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)