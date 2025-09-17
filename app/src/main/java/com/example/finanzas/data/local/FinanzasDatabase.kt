package com.example.finanzas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.util.Converters

@Database(
    entities = [Transaccion::class, Categoria::class, Usuario::class, Moneda::class],
    version = 9,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinanzasDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun monedaDao(): MonedaDao
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE transacciones ADD COLUMN fechaConcrecion INTEGER")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `monedas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `simbolo` TEXT NOT NULL, `tasa_conversion` REAL NOT NULL)")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE usuario ADD COLUMN monedaSecundaria TEXT")
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE transacciones ADD COLUMN tipoCompra TEXT")
        db.execSQL("ALTER TABLE transacciones ADD COLUMN imageUri TEXT")
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE usuario ADD COLUMN objetivoAhorroMensual REAL NOT NULL DEFAULT 0.0")
    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        /*
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Yuan', '¥', 100.0)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Rublo', '₽', 1.0)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Won', '₩', 1.0)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Dólar Australiano', '$', 1.57)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Dólar Hong Kong', '$', 7.79)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Dólar de Macao', '$', 15.65)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Peso Argentino', '$', 100.0)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Franco Suizo', 'CHF', 0.93)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Real Brasileño', 'R$', 4.99)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Dólar Canadiense', '$', 1.34)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Dólar de Macao', '$', 15.65)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Peso Chileno', '$', 740.0)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Peso Colombiano', '$', 3950.0)")
        db.execSQL("INSERT INTO monedas (nombre, simbolo, tasa_conversion) VALUES ('Peso Uruguayo', '$', 41.0)")
        */
    }

}