package com.example.finanzas.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.model.categoria.CategoriaDao
import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.model.gasto.GastoEntity
import com.example.finanzas.model.categoria.CategoriaEntity
import com.example.finanzas.model.categoria.categoriasPorDefecto
import com.example.finanzas.model.user.UserDao
import com.example.finanzas.model.user.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        GastoEntity::class,
        CategoriaEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gastoDao(): GastoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun userDao(): UserDao
}
