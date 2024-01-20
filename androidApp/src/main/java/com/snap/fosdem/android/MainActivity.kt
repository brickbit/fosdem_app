package com.snap.fosdem.android

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.snap.fosdem.android.provider.ActivityProvider
import com.snap.fosdem.android.scaffold.FosdemScaffold
import com.snap.fosdem.android.service.NotificationService
import com.snap.fosdem.android.service.NotificationService.Companion.MY_CHANNEL_ID
import com.snap.fosdem.android.service.NotificationService.Companion.NOTIFICATION_ID
import com.snap.fosdem.app.state.ScaffoldState
import com.snap.fosdem.app.state.SendNotificationState
import com.snap.fosdem.app.viewModel.MainActivityViewModel
import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.calculateTimeInMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import java.net.URL

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by inject()
    private val provider: ActivityProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController: NavHostController = rememberNavController()
            createChannel()
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val state = viewModel.state.collectAsState().value as ScaffoldState.Initialized
                    val routeName = navBackStackEntry?.destination?.route

                    LaunchedEffect(routeName) {
                        viewModel.getRouteInformation(routeName)
                    }
                    FosdemScaffold(
                        navController = navController,
                        visible = state.visible,
                        route = state.route
                    )
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
            val image = URL(event.talk?.room?.building?.map).readBytes()
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



