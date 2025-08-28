package com.example.finanzas.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.FinanzasDatabase
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.MIGRATION_4_5
import com.example.finanzas.data.local.MIGRATION_5_6
import com.example.finanzas.data.local.MIGRATION_6_7
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.model.IconosEstandar
import com.example.finanzas.model.TemaApp
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.notifications.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFinanzasDatabase(
        @ApplicationContext context: Context
    ): FinanzasDatabase {
        return Room.databaseBuilder(
            context,
            FinanzasDatabase::class.java,
            "finanzas_db"
        )
            .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val database = FinanzasDatabase.INSTANCE ?: return
                    CoroutineScope(Dispatchers.IO).launch {
                        val categoriaDao = database.categoriaDao()
                        val usuarioDao = database.usuarioDao()
                        val monedaDao = database.monedaDao()

                        // Insert default currencies
                        monedaDao.insertMoneda(Moneda(nombre = "Dólar", simbolo = "$", tasa_conversion = 1.0))
                        monedaDao.insertMoneda(Moneda(nombre = "Bolívar", simbolo = "Bs.", tasa_conversion = 36.5))


                        // 1. Insertamos el usuario por defecto (actualizado)
                        usuarioDao.upsertUsuario(
                            Usuario(
                                nombre = "Usuario",
                                email = null,
                                fechaNacimiento = null,
                                monedaPrincipal = "VES",
                                monedaSecundaria = null,
                                tema = TemaApp.CLARO.name,
                                onboardingCompletado = false,
                                ahorroAcumulado = 0.0,
                                fechaUltimoCierre = Date().time
                            )
                        )

                        // 2. Insertamos la categoría única para Ingresos
                        categoriaDao.insertCategoria(
                            Categoria(
                                nombre = "Ingreso General",
                                icono = IconosEstandar.OTROS.name,
                                tipo = TipoTransaccion.INGRESO.name,
                                esPersonalizada = false
                            )
                        )

                        // 3. Insertamos todas las categorías de Gastos
                        IconosEstandar.values().forEach { icono ->
                            categoriaDao.insertCategoria(
                                Categoria(
                                    nombre = icono.name.replace('_', ' ').lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    icono = icono.name,
                                    tipo = TipoTransaccion.GASTO.name,
                                    esPersonalizada = false
                                )
                            )
                        }
                    }
                }
            })
            .build().also {
                FinanzasDatabase.INSTANCE = it
            }
    }

    // ... (El resto del archivo no cambia)

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