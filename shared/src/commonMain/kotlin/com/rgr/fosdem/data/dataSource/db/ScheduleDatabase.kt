package com.rgr.fosdem.data.dataSource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rgr.fosdem.data.dataSource.db.dao.SchedulesDao
import com.rgr.fosdem.data.model.entity.ScheduleEntity

const val DATABASE_NAME = "schedule.db"

@Database(
    entities = [ScheduleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun scheduleDao(): SchedulesDao
}