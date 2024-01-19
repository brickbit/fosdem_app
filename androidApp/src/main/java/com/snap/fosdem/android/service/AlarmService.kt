package com.snap.fosdem.android.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.snap.fosdem.android.R
import java.util.Calendar

class AlarmService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    /*private val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmReceiver = AlarmReceiver()

    override fun onCreate() {
        super.onCreate()

        // Schedule the alarm for the next hour
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, 1)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        // Cancel the alarm
        alarmManager.cancel(PendingIntent.getBroadcast(this, ALARM_ID, Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_CANCEL_CURRENT))
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun createNotification(): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hourly Reminder")
            .setContentText("Your hourly reminder is running!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return notificationBuilder.build()
    }*/
}