package com.example.finanzas.model

import androidx.annotation.DrawableRes
import com.example.finanzas.R

enum class IconosEstandar(@DrawableRes val resourceId: Int) {
    // Vivienda
    ALQUILER(R.drawable.real_estate_agent),
    HIPOTECA(R.drawable.apartment_24dp),
    AGUA(R.drawable.water_drop_24dp),
    GAS(R.drawable.local_gas_station),
    LUZ(R.drawable.lightbulb_2_24dp),
    INTERNET(R.drawable.android_wifi_3_bar_24dp),

    // Alimentacion
    SUPERMERCADO(R.drawable.shopping),
    RESTAURANTES(R.drawable.restaurant_24dp),
    CAFETERIAS(R.drawable.coffee_maker_24dp),
    COMIDA_RAPIDA(R.drawable.lunch_dining_24dp),
    DELIVERY(R.drawable.delivery_truck_speed),

    // Transporte
    COMBUSTIBLE(R.drawable.local_gas_station),
    TRANSPORTE_PUBLICO(R.drawable.emoji_transportation_24dp),
    MANTENIMIENTO_VEHICULO(R.drawable.handyman),
    SEGUROS_VEHICULO(R.drawable.health_cross), // Renombrado para ser más específico

    // Salud
    MEDICINA(R.drawable.health_cross),
    FARMACIA(R.drawable.admin_meds_24dp),
    CONSULTAS_MEDICAS(R.drawable.health_and_safety_24dp),
    SEGURO_SALUD(R.drawable.health_cross), // Renombrado para ser más específico

    // Educacion
    CURSOS(R.drawable.history_edu),
    COLEGIOS(R.drawable.co_present_24dp),
    UNIVERSIDAD(R.drawable.history_edu),
    LIBROS(R.drawable.book_4_24dp),
    MATERIALES(R.drawable.book_4_24dp),
    SUSCRIPCIONES_EDUCACION(R.drawable.book_4_24dp),

    // Entretenimiento y Ocio
    CINE(R.drawable.cinematic_blur_24dp),
    TEATRO(R.drawable.theater_comedy_24dp),
    EVENTOS(R.drawable.cinematic_blur_24dp),
    VIDEOJUEGOS(R.drawable.stadia_controller_24dp),
    VIAJES(R.drawable.travel),
    TURISMO(R.drawable.travel),
    DEPORTES(R.drawable.sports_handball_24dp),
    GIMNASIO(R.drawable.fitness_center_24dp),

    // Ropa y accesorios
    VESTIMENTA(R.drawable.apparel),
    CALZADO(R.drawable.steps_24dp),
    ACCESORIOS(R.drawable.floor_lamp_24dp),

    // Hogar
    ELECTRODOMESTICOS(R.drawable.tv_24dp),
    MUEBLES(R.drawable.chair_24dp),
    DECORACION(R.drawable.floor_lamp_24dp),

    // Financieros
    CREDITOS(R.drawable.credit_card_24dp),
    DEUDAS(R.drawable.attach_money),
    INTERESES_BANCARIOS(R.drawable.savings_24dp),
    COMISIONES(R.drawable.attach_money),

    // Mascotas
    CUIDADO_MASCOTAS(R.drawable.pets_24),

    // Otros
    OTROS(R.drawable.attach_money)
}