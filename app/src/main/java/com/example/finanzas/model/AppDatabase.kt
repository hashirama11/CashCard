package com.example.finanzas.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.model.categoria.CategoriaDao
import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.model.gasto.GastoEntity
import com.example.finanzas.model.categoria.CategoriaEntity
import com.example.finanzas.model.user.UserDao
import com.example.finanzas.model.user.UserEntity

@Database(entities = [GastoEntity::class,
                     CategoriaEntity::class,
                     UserEntity::class],
    version = 3)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    // Declaracion de los DAO
    abstract fun gastoDao(): GastoDao
    abstract fun categoriaDao(): CategoriaDao

    abstract fun userDao(): UserDao

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

        // Creacion de la base de datos User sin perder los datos de la Version
        // Creacion de la base de datos Categoria sin perder los datos de la Version

        val MIGRATION_2_3 = object : Migration(2, 3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS user (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        email TEXT NOT NULL,
                        password TEXT NOT NULL,
                        year TEXT NOT NULL
                    )
                """.trimIndent()
                )
            }
        }
    }


}