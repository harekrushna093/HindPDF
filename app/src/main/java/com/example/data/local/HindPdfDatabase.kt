package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ActivityLogEntity::class], version = 1, exportSchema = false)
abstract class HindPdfDatabase : RoomDatabase() {
  abstract fun activityLogDao(): ActivityLogDao

  companion object {
    @Volatile
    private var INSTANCE: HindPdfDatabase? = null

    fun getDatabase(context: Context): HindPdfDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          HindPdfDatabase::class.java,
          "hindpdf_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
