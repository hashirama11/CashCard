package com.example.finanzas.ui.onboarding

import androidx.annotation.DrawableRes
import com.example.finanzas.R

sealed class OnboardingPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
) {
    object First : OnboardingPage(
        image = R.drawable.real_estate_agent, // Usaremos iconos existentes
        title = "Bienvenido a Finanzas App",
        description = "Tu asistente personal para tomar el control de tus ingresos y gastos de una manera sencilla e intuitiva."
    )
    object Second : OnboardingPage(
        image = R.drawable.travel,
        title = "Registra y Visualiza",
        description = "Añade tus transacciones diarias y observa cómo tus finanzas toman forma con nuestros gráficos interactivos."
    )
    object Third : OnboardingPage(
        image = R.drawable.apparel,
        title = "Personaliza tu Experiencia",
        description = "Crea tus propias categorías, elige tu moneda y configura la apariencia de la app a tu gusto."
    )
}