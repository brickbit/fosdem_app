package com.rgr.fosdem.data.dataSource.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.rgr.fosdem.data.dataSource.db.converter.ArrayListAttachmentConverter
import com.rgr.fosdem.data.dataSource.db.converter.ArrayListStandFeaturesConverter
import com.rgr.fosdem.data.dataSource.db.converter.ArrayListStringConverter
import com.rgr.fosdem.data.dataSource.db.dao.ScheduleAndroidDao
import com.rgr.fosdem.data.dataSource.db.dao.StandAndroidDao
import com.rgr.fosdem.data.dataSource.db.dao.VideoAndroidDao
import com.rgr.fosdem.data.dataSource.db.entity.ScheduleAndroidEntity
import com.rgr.fosdem.data.dataSource.db.entity.StandAndroidEntity
import com.rgr.fosdem.data.dataSource.db.entity.VideoAndroidEntity

@Database(entities = [ScheduleAndroidEntity::class, VideoAndroidEntity::class, StandAndroidEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(ArrayListStringConverter::class, ArrayListAttachmentConverter::class, ArrayListStandFeaturesConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleAndroidDao
    abstract fun videoDao(): VideoAndroidDao
    abstract fun standDao(): StandAndroidDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}