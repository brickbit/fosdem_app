package com.rgr.fosdem.data.dataSource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rgr.fosdem.data.dataSource.db.entity.ScheduleAndroidEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleAndroidDao {
    @Query("SELECT * FROM schedule")
    fun fetchSchedules(): Flow<List<ScheduleAndroidEntity>>

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun findScheduleId(id: Int): Flow<ScheduleAndroidEntity?>

    @Update
    suspend fun updateSchedule(schedule: ScheduleAndroidEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(schedules: List<ScheduleAndroidEntity>)
}