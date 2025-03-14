package com.example.movil_soundwater.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.util.RoomConverters

@Database(entities = [Reminder::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "soundwater_database"
                )
                    //.fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}