package com.example.finanzas.ui.features.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finanzas.R

@Composable
fun NavigationScreen() {
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.shopping), contentDescription = null) },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.attach_money), contentDescription = null) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.users), contentDescription = null) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.settings_24dp_1f1f1f_fill0_wght400_grad0_opsz24), contentDescription = null) },
            selected = false,
            onClick = {}
        )
    }
}