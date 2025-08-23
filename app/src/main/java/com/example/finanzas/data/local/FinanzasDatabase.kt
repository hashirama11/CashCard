package com.example.finanzas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.util.Converters

@Database(
    entities = [Transaccion::class, Categoria::class, Usuario::class],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinanzasDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao

    // --- ESTE ES EL BLOQUE AÑADIDO ---
    companion object {
        @Volatile
        var INSTANCE: FinanzasDatabase? = null
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE transacciones ADD COLUMN fechaConcrecion INTEGER")
    }
}