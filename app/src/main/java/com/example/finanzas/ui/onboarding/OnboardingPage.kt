package com.example.finanzas.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.example.finanzas.R

sealed class OnboardingPage(
    @RawRes val animation: Int,
    @DrawableRes val featureImage: Int,
    val title: String,
    val description: String
) {
    object First : OnboardingPage(
        animation = R.raw.onboarding_1,
        featureImage = R.drawable.feature_dashboard,
        title = "Tu Dashboard Central",
        description = "Visualiza tus ingresos, gastos y ahorros de un solo vistazo. Todo lo importante, en un solo lugar."
    )
    object Second : OnboardingPage(
        animation = R.raw.onboarding_2,
        featureImage = R.drawable.feature_add,
        title = "Registra Transacciones Fácilmente",
        description = "Añade ingresos, gastos, compras o ahorros en segundos con una interfaz clara y directa."
    )
    object Third : OnboardingPage(
        animation = R.raw.onboarding_3,
        featureImage = R.drawable.feature_charts,
        title = "Analiza tu Progreso",
        description = "Usa los gráficos interactivos para entender a dónde va tu dinero y cómo crecen tus ahorros."
    )
}