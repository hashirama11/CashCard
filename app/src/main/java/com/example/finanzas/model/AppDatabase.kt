package com.example.finanzas.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finanzas.model.categoria.CategoriaConverter
import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.model.gasto.GastoEntity
import com.example.finanzas.model.gasto.LocalDateConverter

@Database(entities = [GastoEntity::class], version = 1)
@TypeConverters(LocalDateConverter::class, CategoriaConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gastoDao(): GastoDao

}