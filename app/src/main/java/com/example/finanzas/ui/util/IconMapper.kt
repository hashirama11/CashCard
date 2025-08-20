package com.example.finanzas.ui.util

import androidx.compose.runtime.Composable
import com.example.finanzas.R
import com.example.finanzas.model.IconosEstandar

fun getIconResource(iconName: String?): Int {
    return IconosEstandar.values().find { it.name == iconName }?.resourceId ?: R.drawable.attach_money
}