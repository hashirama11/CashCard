package com.example.finanzas.ui.features.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.ui.screen.AppNavigationBar

@Composable
fun UserProfileRoute(
    onHomeClick: () -> Unit,
    onStatsClick: () -> Unit,
    onUsersClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddCategoryClick: () -> Unit,
    onUpdateCategoryClick: () -> Unit,
    onDeleteCategoryClick: () -> Unit,
    onResetDatabaseClick: () -> Unit
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
    ) { padding ->
        UserProfileScreen(
            modifier = Modifier.padding(padding),
            onAddCategoryClick = onAddCategoryClick,
            onDeleteCategoryClick = onDeleteCategoryClick,
            onResetDatabaseClick = onResetDatabaseClick
        )
    }
}
