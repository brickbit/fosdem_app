package com.rgr.fosdem.data.dataSource.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.rgr.fosdem.data.dataSource.db.dao.SchedulesDao
import com.rgr.fosdem.data.model.entity.ScheduleEntity

const val DATABASE_NAME = "schedule.db"

@Database(
    entities = [ScheduleEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): SchedulesDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}