package com.example.finanzas.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.model.categoria.CategoriaDao
import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.model.gasto.GastoEntity
import com.example.finanzas.model.gasto.LocalDateConverter
import com.example.finanzas.model.categoria.CategoriaEntity

@Database(entities = [GastoEntity::class,
                     CategoriaEntity::class],
    version = 2)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    // Declaracion de los DAO
    abstract fun gastoDao(): GastoDao
    abstract fun categoriaDao(): CategoriaDao

    // Creacion de la base de datos Categoria sin perder los datos de la Version
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS categorias (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombre TEXT NOT NULL,
                        icono INTEGER NOT NULL
                    )
                """.trimIndent()
                )
            }
        }
    }

}