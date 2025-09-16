package com.example.finanzas.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.FinanzasDatabase
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.MIGRATION_4_5
import com.example.finanzas.data.local.MIGRATION_5_6
import com.example.finanzas.data.local.MIGRATION_6_7
import com.example.finanzas.data.local.MIGRATION_7_8
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
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFinanzasDatabase(
        @ApplicationContext context: Context,
        dbProvider: Provider<FinanzasDatabase>
    ): FinanzasDatabase {
        return Room.databaseBuilder(
            context,
            FinanzasDatabase::class.java,
            "finanzas_db"
        )
            .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val database = dbProvider.get()
                        val categoriaDao = database.categoriaDao()
                        val usuarioDao = database.usuarioDao()
                        val monedaDao = database.monedaDao()

                        database.withTransaction {
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar", simbolo = "$", tasa_conversion = 1.0))
                            monedaDao.insertMoneda(Moneda(nombre = "Bolívar", simbolo = "Bs.", tasa_conversion = 36.5))
                            monedaDao.insertMoneda(Moneda(nombre = "Yuan", simbolo = "¥", tasa_conversion = 100.0))
                            monedaDao.insertMoneda(Moneda(nombre = "Euro", simbolo = "€", tasa_conversion = 0.92))
                            monedaDao.insertMoneda(Moneda(nombre = "Yen", simbolo = "¥", tasa_conversion = 145.5))
                            monedaDao.insertMoneda(Moneda(nombre = "Libra Esterlina", simbolo = "£", tasa_conversion = 0.79))
                            monedaDao.insertMoneda(Moneda(nombre = "Rublo", simbolo = "₽", tasa_conversion = 1.34))
                            monedaDao.insertMoneda(Moneda(nombre = "Peso Argentino", simbolo = "$", tasa_conversion = 100.0))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar Australiano", simbolo = "$", tasa_conversion = 1.57))
                            monedaDao.insertMoneda(Moneda(nombre = "Franco Suizo", simbolo = "CHF", tasa_conversion = 0.93))
                            monedaDao.insertMoneda(Moneda(nombre = "Real Brasileño", simbolo = "R$", tasa_conversion = 4.99))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar Canadiense", simbolo = "$", tasa_conversion = 1.34))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar de Hong Kong", simbolo = "$", tasa_conversion = 7.79))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar de Macao", simbolo = "$", tasa_conversion = 15.65))
                            monedaDao.insertMoneda(Moneda(nombre = "Peso Chileno", simbolo = "$", tasa_conversion = 740.0))
                            monedaDao.insertMoneda(Moneda(nombre = "Peso Colombiano", simbolo = "$", tasa_conversion = 3950.0))
                            monedaDao.insertMoneda(Moneda(nombre = "Franco Suizo", simbolo = "CHF", tasa_conversion = 0.93))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar de Nueva Zelanda", simbolo = "$", tasa_conversion = 1.66))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar de Sri Lanka", simbolo = "$", tasa_conversion = 37.0))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar de Tailandia", simbolo = "$", tasa_conversion = 36.5))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar de Taiwan", simbolo = "$", tasa_conversion = 32.5))
                            monedaDao.insertMoneda(Moneda(nombre = "Dólar de Trinidad y Tobago", simbolo = "$", tasa_conversion = 6.78))
                            monedaDao.insertMoneda(Moneda(nombre = "Peso Uruguayo", simbolo = "$", tasa_conversion = 41.0))
                            monedaDao.insertMoneda(Moneda(nombre = "Franco Chili", simbolo = "CLF", tasa_conversion = 0.027))


                            usuarioDao.upsertUsuario(
                                Usuario(
                                    nombre = "Usuario",
                                    email = null,
                                    fechaNacimiento = null,
                                    monedaPrincipal = "Dólar",
                                    monedaSecundaria = "Bolívar",
                                    tema = TemaApp.CLARO.name,
                                    onboardingCompletado = false,
                                    ahorroAcumulado = 0.0,
                                    objetivoAhorroMensual = 0.0,
                                    fechaUltimoCierre = Date().time
                                )
                            )

                            categoriaDao.insertCategoria(
                                Categoria(
                                    nombre = "Ingreso General",
                                    icono = IconosEstandar.OTROS.name,
                                    tipo = TipoTransaccion.INGRESO.name,
                                    esPersonalizada = false
                                )
                            )

                            val categoriasDeGastos = IconosEstandar.values().map { icono ->
                                Categoria(
                                    nombre = icono.name.replace('_', ' ').lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    icono = icono.name,
                                    tipo = TipoTransaccion.GASTO.name,
                                    esPersonalizada = false
                                )
                            }
                            categoriasDeGastos.forEach { categoriaDao.insertCategoria(it) }
                        }
                    }
                }
            })
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