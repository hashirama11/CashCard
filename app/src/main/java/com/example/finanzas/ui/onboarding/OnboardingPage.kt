package com.example.finanzas.ui.onboarding

import androidx.annotation.RawRes
import com.example.finanzas.R

sealed class OnboardingPage(
    @RawRes val animation: Int,
    val title: String,
    val description: String
) {
    object First : OnboardingPage(
        animation = R.raw.onboarding_1,
        title = "Bienvenido a Finanzas App",
        description = "Tu asistente personal para tomar el control de tus ingresos y gastos de una manera sencilla e intuitiva."
    )
    object Second : OnboardingPage(
        animation = R.raw.onboarding_2,
        title = "Registra y Visualiza",
        description = "Añade tus transacciones diarias y observa cómo tus finanzas toman forma con nuestros gráficos interactivos."
    )
    object Third : OnboardingPage(
        animation = R.raw.onboarding_3,
        title = "Personaliza tu Experiencia",
        description = "Crea tus propias categorías, elige tu moneda y configura la apariencia de la app a tu gusto."
    )
}