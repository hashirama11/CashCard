package com.example.finanzas.ui.features.profile.category.reset

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.finanzas.ui.screen.AppNavigationBar

@Composable
fun ResetDatabaseRoute(
    onHomeClick: () -> Unit,
    onStatsClick: () -> Unit,
    onUsersClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onConfirmReset: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AppNavigationBar(
                onHomeClick = onHomeClick,
                onStatsClick = onStatsClick,
                onUsersClick = onUsersClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { _ ->
        ResetDatabaseScreen(
            onConfirmReset = onConfirmReset,
            onDismiss = onDismiss
        )
    }
}
