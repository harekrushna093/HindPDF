package com.example.data.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

class NotificationHelper(private val context: Context) {
  companion object {
    const val CHANNEL_ID = "hindpdf_channel_updates"
    const val CHANNEL_NAME = "HindPDF Task Notifications"
    const val CHANNEL_DESC = "Notifications for completed PDF conversions, document security, and sync"
  }

  init {
    createNotificationChannel()
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
        description = CHANNEL_DESC
        enableVibration(true)
      }
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun sendTaskCompletedNotification(toolName: String, fileName: String, isProQuality: Boolean = false) {
    val intent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(
      context,
      0,
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val qualityLabel = if (isProQuality) "Studio Pro HD" else "Standard"
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.stat_sys_download_done)
      .setContentTitle("✅ $toolName Completed")
      .setContentText("$fileName is ready in $qualityLabel quality.")
      .setStyle(
        NotificationCompat.BigTextStyle()
          .bigText("HindPDF processed '$fileName' client-side with 256-bit encryption. Ready to view and download.")
      )
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)

    try {
      val notificationManager = NotificationManagerCompat.from(context)
      val notificationId = (System.currentTimeMillis() % 10000).toInt()
      notificationManager.notify(notificationId, builder.build())
    } catch (e: SecurityException) {
      // Notification permission not granted yet
    }
  }
}
