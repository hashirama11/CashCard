package com.example.finanzas.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.FinanzasDatabase
import com.example.finanzas.data.local.MIGRATION_4_5
import com.example.finanzas.data.local.MIGRATION_5_6
import com.example.finanzas.data.local.MIGRATION_6_7
import com.example.finanzas.data.local.MIGRATION_7_8
import com.example.finanzas.data.local.MIGRATION_8_9
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.notifications.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provee el RoomDatabase.Callback para Room.
     * Este callback se encarga de la inicialización de la base de datos (CREATE)
     * para nuevas instalaciones.
     * En este nuevo enfoque, el callback onCreate es minimalista,
     * delegando el poblamiento de datos al DataInitializer para evitar bloqueos.
     */
    @Provides
    @Singleton
    fun provideDatabaseCallback(
        // Ya no necesitamos dbProvider ni CoroutineScope aquí para poblar,
        // ya que DataInitializer lo hará post-creación/migración.
        // Si necesitas alguna lógica esencial de esquema en onCreate, la pones aquí.
    ): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Todo el poblamiento de datos iniciales se ha movido
                // al DataInitializer, que se ejecuta asíncronamente
                // después de que la app se levanta.
                // Este onCreate se ejecutará cuando la DB se cree *por primera vez*.
                // Si aquí se necesita alguna inicialización de esquema muy específica
                // que no sea un INSERT de datos, iría aquí.
                // Ej: db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_date` ON `transacciones` (`fecha`)")
            }
        }
    }


    @Provides
    @Singleton
    fun provideFinanzasDatabase(
        @ApplicationContext context: Context,
        // Inyectamos el callback que acabamos de definir, ya no el dbProvider para onCreate
        callback: RoomDatabase.Callback
    ): FinanzasDatabase {
        return Room.databaseBuilder(
            context,
            FinanzasDatabase::class.java,
            "finanzas_db"
        )
            .addMigrations(
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8,
                MIGRATION_8_9 // MIGRATION_8_9 ahora estará "vacía" de inserts
            )
            .addCallback(callback) // Usamos el callback inyectado
            .build()
    }

    @Provides
    @Singleton
    fun provideTransaccionDao(database: FinanzasDatabase): TransaccionDao = database.transaccionDao()

    @Provides
    @Singleton
    fun provideCategoriaDao(database: FinanzasDatabase): CategoriaDao = database.categoriaDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(database: FinanzasDatabase): UsuarioDao = database.usuarioDao()

    @Provides
    @Singleton
    fun provideMonedaDao(database: FinanzasDatabase): MonedaDao = database.monedaDao()

    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AlarmScheduler(context)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pending_transaction_channel",
                "Recordatorios de Transacciones",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
}