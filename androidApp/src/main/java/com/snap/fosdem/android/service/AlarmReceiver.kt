package com.snap.fosdem.android.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.snap.fosdem.android.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Get the alarm ID from the intent
        /*val alarmId = intent.getIntExtra(ALARM_ID_EXTRA, 0)

        // Create a notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hourly Reminder")
            .setContentText("It's time for your hourly reminder!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Create a pending intent to restart the alarm
        val pendingIntent = PendingIntent.getBroadcast(context, alarmId, Intent(context, AlarmReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setAutoCancel(true)

        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)*/
    }
}