package com.rgr.fosdem.android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ScheduleAndroidEntity::class, VideoAndroidEntity::class, StandAndroidEntity::class], version = 1)
@TypeConverters(ArrayListStringConverter::class, ArrayListAttachmentConverter::class, ArrayListStandFeaturesConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleAndroidDao
    abstract fun videoDao(): VideoAndroidDao
    abstract fun standDao(): StandAndroidDao
}