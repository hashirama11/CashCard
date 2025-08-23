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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

        val biometricAuthenticator = BiometricAuthenticator(this)

        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.onboardingCompleted.value == null
        }

        setContent {
            val isDarkTheme by mainViewModel.isDarkTheme.collectAsStateWithLifecycle()
            val onboardingCompleted by mainViewModel.onboardingCompleted.collectAsStateWithLifecycle()
            val isAuthenticated by mainViewModel.isAuthenticated.collectAsStateWithLifecycle()

            FinanzasTheme(darkTheme = isDarkTheme) {
                // Determinamos si debemos mostrar el contenido principal
                val showContent = onboardingCompleted == false || isAuthenticated

                if (showContent) {
                    // Si el usuario está en el onboarding o ya se autenticó, mostramos la app.
                    onboardingCompleted?.let {
                        val startDestination = if (it) AppScreens.Dashboard.route else AppScreens.Onboarding.route
                        MainScreen(startDestination)
                    }
                } else {
                    // Si el onboarding está completo pero no se ha autenticado, lanzamos el prompt.
                    LaunchedEffect(onboardingCompleted) {
                        if (onboardingCompleted == true) {
                            biometricAuthenticator.promptBiometricAuth(
                                title = "Autenticación Requerida",
                                subtitle = "Desbloquea para acceder a tus finanzas",
                                onSuccess = {
                                    // Si la autenticación es exitosa, lo notificamos al ViewModel
                                    mainViewModel.onAuthenticationSuccess()
                                },
                                onError = { _, _ ->
                                    // Si el usuario cancela o hay un error, cerramos la app para proteger los datos.
                                    finish()
                                }
                            )
                        }
                    }
                    // Muestra una pantalla de carga mientras el diálogo biométrico está activo.
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
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
        AppScreens.MonthlyGoal.route,
        AppScreens.HistoricalBalance.route,
        AppScreens.Profile.route -> true
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
        BottomNavItem("Meta Mensual", Icons.Default.CheckCircle, AppScreens.MonthlyGoal.route),
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