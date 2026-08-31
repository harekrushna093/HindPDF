package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "tool_name")
  val toolName: String,
  @ColumnInfo(name = "file_name")
  val fileName: String,
  @ColumnInfo(name = "time_formatted")
  val timeFormatted: String,
  @ColumnInfo(name = "timestamp")
  val timestamp: Long = System.currentTimeMillis(),
  @ColumnInfo(name = "output_size")
  val outputSize: String = "1.2 MB"
)
