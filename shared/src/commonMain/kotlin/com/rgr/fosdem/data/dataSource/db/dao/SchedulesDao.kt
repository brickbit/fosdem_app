package com.rgr.fosdem.data.dataSource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rgr.fosdem.data.model.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchedulesDao {

    @Query("SELECT * FROM ScheduleEntity")
    fun fetchSchedules(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM ScheduleEntity WHERE id = :id")
    fun findScheduleId(id: Int): Flow<ScheduleEntity?>

    @Query("SELECT COUNT(id) FROM ScheduleEntity")
    suspend fun countSchedules(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(schedules: List<ScheduleEntity>)
}