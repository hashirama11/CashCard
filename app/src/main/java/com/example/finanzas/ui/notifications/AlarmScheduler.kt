package com.example.finanzas.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.finanzas.data.local.entity.Transaccion

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(transaccion: Transaccion) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", transaccion.descripcion)
            putExtra("EXTRA_TITLE", "Recordatorio de Transacción Pendiente")
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            transaccion.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        transaccion.fechaConcrecion?.let {
            // No programar alarmas para fechas que ya pasaron
            if (it.time < System.currentTimeMillis()) return

            // --- ESTA ES LA CORRECCIÓN DE SEGURIDAD ---
            // Verificamos si podemos programar alarmas exactas
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        it.time,
                        pendingIntent
                    )
                } else {
                    // No tenemos permiso. La alarma no se programará, pero la app no crasheará.
                    // En una versión futura, aquí podrías guiar al usuario a los ajustes.
                }
            } else {
                // Para versiones antiguas de Android, no se necesita permiso especial.
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    it.time,
                    pendingIntent
                )
            }
        }
    }

    fun cancel(transaccion: Transaccion) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            transaccion.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}