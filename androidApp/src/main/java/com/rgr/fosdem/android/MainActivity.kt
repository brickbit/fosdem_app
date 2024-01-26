package com.rgr.fosdem.android

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rgr.fosdem.android.provider.ActivityProvider
import com.rgr.fosdem.android.service.NotificationService
import com.rgr.fosdem.android.service.NotificationService.Companion.MY_CHANNEL_ID
import com.rgr.fosdem.android.service.NotificationService.Companion.NOTIFICATION_ID
import com.rgr.fosdem.app.state.SendNotificationState
import com.rgr.fosdem.app.viewModel.MainActivityViewModel
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.calculateTimeInMillis
import com.rgr.fosdem.domain.provider.ConnectivityProvider
import com.rgr.fosdem.android.screens.common.Main
import com.rgr.fosdem.android.screens.common.NoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import java.net.URL

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by inject()
    private val provider: ActivityProvider by inject()
    private val connectivityObserver: ConnectivityProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController: NavHostController = rememberNavController()
            createChannel()
            val status by connectivityObserver.observe().collectAsState(
                initial = ConnectivityProvider.Status.Available
            )

            var useOfflineMode by remember { mutableStateOf(false) }

            when(status) {
                ConnectivityProvider.Status.Available -> { Main(navController = navController) }
                ConnectivityProvider.Status.Unavailable -> {
                    if(useOfflineMode) {
                        Main(navController = navController)
                    } else {
                        NoConnection{ useOfflineMode = true}
                    }
                }
                ConnectivityProvider.Status.Losing ->  {
                    if(useOfflineMode) {
                        Main(navController = navController)
                    } else {
                        NoConnection{ useOfflineMode = true}
                    }
                }
                ConnectivityProvider.Status.Lost ->  {
                    if(useOfflineMode) {
                        Main(navController = navController)
                    } else {
                        NoConnection{ useOfflineMode = true}
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        searchNotification()
    }

    override fun onStart() {
        super.onStart()
        provider.setActivity(activity = this)
    }

    private fun searchNotification() {
        viewModel.getEventsForNotification()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.sendNotificationState.collect { eventState ->
                    when (eventState) {
                        is SendNotificationState.Ready -> {
                            eventState.notification.events.map { event ->
                                scheduleNotification(event,eventState.notification.time)
                            }
                        }

                        SendNotificationState.Initialized -> {}
                    }
                }
            }
        }
    }

    private fun scheduleNotification(event: EventBo, timeBefore: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val image = try { URL(event.talk.room.building.map).readBytes() } catch (e: Exception) { null }
            val intent = Intent(applicationContext, NotificationService::class.java)
            intent.putExtra("NOTIFICATION_EVENT_ID", Json.encodeToString(event))
            intent.putExtra("NOTIFICATION_TIME_ID", timeBefore)
            intent.putExtra("NOTIFICATION_IMAGE_ID", image)


            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                when {
                    // If permission is granted, proceed with scheduling exact alarms.
                    alarmManager.canScheduleExactAlarms() -> {
                        event.calculateTimeInMillis(timeBefore)
                            ?.let { alarmManager.setExact(AlarmManager.RTC_WAKEUP, it, pendingIntent) }
                    }
                    else -> {
                        // Ask users to go to exact alarm page in system settings.
                        startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    }
                }
            } else {
                event.calculateTimeInMillis(timeBefore)
                    ?.let { alarmManager.setExact(AlarmManager.RTC_WAKEUP, it, pendingIntent) }
            }
        }

    }

    private fun createChannel() {
        val channel = NotificationChannel(
            MY_CHANNEL_ID,
            "MySuperChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "SUSCRIBETE"
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}



