package com.rgr.fosdem.android.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScheduleAndroidEntity::class, VideoAndroidEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleAndroidDao
    abstract fun videoDao(): VideoAndroidDao
}