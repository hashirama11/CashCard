package com.example.finanzas // Asegúrate de que este sea el paquete correcto para tu MyApplication

import android.app.Application
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import androidx.hilt.work.HiltWorkerFactory
import com.example.finanzas.data.local.DataInitializer // Importa tu DataInitializer
import com.example.finanzas.worker.CorteDeMesWorker // Importa tu Worker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider { // Tu clase Application original

    // Inyección de HiltWorkerFactory para WorkManager
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    // Inyección de tu DataInitializer
    @Inject
    lateinit var dataInitializer: DataInitializer

    // Implementación de Configuration.Provider para WorkManager
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory) // Usar la factory inyectada por Hilt
            .build()


    override fun onCreate() {
        super.onCreate()

        // 1. Inicialización de datos (tu nueva lógica de post-migración)
        dataInitializer.initializeDefaultData(applicationContext)

        // 2. Configuración del trabajo periódico de WorkManager
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<CorteDeMesWorker>(
            repeatInterval = 30, // Cada 30 días
            repeatIntervalTimeUnit = TimeUnit.DAYS,
            flexTimeInterval = 1, // Puedes ajustar esto para un período de flexibilidad (ej: 1 día antes o después)
            flexTimeIntervalUnit = TimeUnit.DAYS
        )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "corte_de_mes_worker",
            ExistingPeriodicWorkPolicy.KEEP, // Mantiene el trabajo existente si ya está en cola.
            // Considera REPLACE si quieres que se reprograme siempre.
            repeatingRequest
        )
    }
}