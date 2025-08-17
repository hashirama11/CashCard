package com.example.finanzas.ui.features.profile.category.reset

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    ) { paddingValues ->
        ResetDatabaseScreen(
            onConfirmReset = onConfirmReset,
            onDismiss = onDismiss,
            modifier = Modifier.padding(paddingValues)
        )
    }
}