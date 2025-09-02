package com.example.finanzas

data class UserPreferencesState(
    val isDarkTheme: Boolean = false,
    val onboardingCompleted: Boolean? = null
)
