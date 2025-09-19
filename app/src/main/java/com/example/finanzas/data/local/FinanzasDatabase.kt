package com.example.finanzas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.dao.BudgetDao
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Budget
import com.example.finanzas.data.local.entity.BudgetCategory
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.util.Converters

@Database(
    entities = [
        Transaccion::class,
        Categoria::class,
        Usuario::class,
        Moneda::class,
        Budget::class,
        BudgetCategory::class
    ],
    version = 11,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinanzasDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun monedaDao(): MonedaDao
    abstract fun budgetDao(): BudgetDao
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
        // --- Migration for 'monedas' table ---
        // Recreate the table to make tasa_conversion nullable and fix any corruption.
        db.execSQL("""
            CREATE TABLE monedas_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL,
                simbolo TEXT NOT NULL,
                tasa_conversion REAL
            )
        """)
        // Copy data if the old table exists and is not corrupt.
        // A simple try-catch block is the most resilient way to handle
        // the possibility of the old table being malformed.
        try {
            db.execSQL("""
                INSERT INTO monedas_new (id, nombre, simbolo, tasa_conversion)
                SELECT id, nombre, simbolo, tasa_conversion FROM monedas
            """)
        } catch (e: Exception) {
            // Ignore if the old table is corrupt or doesn't exist.
            // The new table will be empty, which is a safe state.
        }
        db.execSQL("DROP TABLE IF EXISTS monedas")
        db.execSQL("ALTER TABLE monedas_new RENAME TO monedas")


        // --- Migration for 'usuario' table ---
        // This migration is defensive to handle inconsistent database states.
        // It checks for the existence of columns before trying to add them.
        val cursor = db.query("PRAGMA table_info(usuario)")
        val columns = mutableSetOf<String>()
        val nameIndex = cursor.getColumnIndex("name")
        if (nameIndex >= 0) {
            while (cursor.moveToNext()) {
                columns.add(cursor.getString(nameIndex))
            }
        }
        cursor.close()

        if (!columns.contains("monedaSecundaria")) {
            db.execSQL("ALTER TABLE usuario ADD COLUMN monedaSecundaria TEXT")
        }
        if (!columns.contains("ahorroAcumulado")) {
            db.execSQL("ALTER TABLE usuario ADD COLUMN ahorroAcumulado REAL NOT NULL DEFAULT 0.0")
        }
        if (!columns.contains("fechaUltimoCierre")) {
            db.execSQL("ALTER TABLE usuario ADD COLUMN fechaUltimoCierre INTEGER NOT NULL DEFAULT 0")
        }
    }
}

val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // No schema changes required for this version.
        // This version bump is used to trigger the DataInitializer to add new currencies.
    }
}

val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `budgets` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `month` INTEGER NOT NULL,
                `year` INTEGER NOT NULL,
                `projectedIncome` REAL NOT NULL
            )
        """)
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_budgets_month_year` ON `budgets` (`month`, `year`)")

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `budget_categories` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `budgetId` INTEGER NOT NULL,
                `categoryId` INTEGER NOT NULL,
                `budgetedAmount` REAL NOT NULL,
                FOREIGN KEY(`budgetId`) REFERENCES `budgets`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`categoryId`) REFERENCES `categorias`(`id`) ON DELETE CASCADE
            )
        """)
    }
}