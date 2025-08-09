package com.example.finanzas.model.categoria

import androidx.annotation.DrawableRes
import com.example.finanzas.R

enum class Categoria(@DrawableRes val icono : Int){
    ALIMENTACION(R.drawable.dining),
    ROPA(R.drawable.apparel),
    TRANSPORTE(R.drawable.emoji_transportation),
    SALUD(R.drawable.health_cross),
    ENTRETENIMIENTO(R.drawable.books_movies_and_music_24),
    GASOLINA(R.drawable.local_gas_station),
    REPUESTOS(R.drawable.handyman),
    MANTENIMIENTO(R.drawable.handyman),
    HIGIENE(R.drawable.attach_money),
    AFEITADA(R.drawable.attach_money),
    UTENSILIOS(R.drawable.shopping),
    OTROS(R.drawable.shopping)
}