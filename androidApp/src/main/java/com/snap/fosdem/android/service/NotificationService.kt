package com.snap.fosdem.android.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.snap.fosdem.android.MainActivity
import com.snap.fosdem.android.R
import com.snap.fosdem.domain.model.EventBo
import kotlinx.serialization.json.Json


class NotificationService: BroadcastReceiver() {

    companion object {
        const val MY_CHANNEL_ID = "myChannel"
        const val NOTIFICATION_ID = 1

    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val listEvents = intent?.getStringExtra("NOTIFICATION_EVENT_ID")
        val time = intent?.getIntExtra("NOTIFICATION_TIME_ID", 10) ?: 10
        val image = intent?.getByteArrayExtra("NOTIFICATION_IMAGE_ID")
        listEvents?.let { events ->
            val decodedEvents = Json.decodeFromString<EventBo>(events)
            context?.let { newContext -> pushNotification(newContext, decodedEvents, time, image) }
        }
    }

    private fun pushNotification(context: Context, event: EventBo, time: Int, image: ByteArray?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = image?.let {
            val bitmap =  BitmapFactory.decodeByteArray(image, 0, image.size)
            NotificationCompat.Builder(context, MY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(event.talk.title)
                .setContentText(
                    context.getString(
                        R.string.notification_description,
                        time.toString(),
                        event.talk.room.name
                    ))
                .setContentIntent(pendingIntent)
                .setLargeIcon(bitmap)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .build()
        } ?: NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentTitle(event.talk.title)
            .setContentText(
                context.getString(
                    R.string.notification_description,
                    time.toString(),
                    event.talk.room.name
                ))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .build()


        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

}