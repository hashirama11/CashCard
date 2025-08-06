package com.example.finanzas.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GastoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gastoDao(): GastoDao

}