package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
  @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 50")
  fun getAllActivityLogs(): Flow<List<ActivityLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLog(log: ActivityLogEntity): Long

  @Query("DELETE FROM activity_logs")
  suspend fun clearAllLogs()

  @Query("DELETE FROM activity_logs WHERE id = :id")
  suspend fun deleteLogById(id: Long)
}
