package com.example.finanzas.notifications

import android.Manifest
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.finanzas.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val transactionId = intent.getIntExtra("EXTRA_TRANSACTION_ID", -1)
        if (transactionId == -1) return

        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: return
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Recordatorio"
        val channelId = "pending_transaction_channel"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notificationManager.notify(transactionId, builder.build())
        }
    }
}