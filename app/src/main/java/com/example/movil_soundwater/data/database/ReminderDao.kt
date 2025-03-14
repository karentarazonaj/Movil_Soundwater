package com.example.movil_soundwater.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.data.model.ReminderType
import java.util.Date

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders ORDER BY nextReminderTime ASC")
    fun getAllReminders(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE isActive = 1 ORDER BY nextReminderTime ASC")
    suspend fun getAllActiveReminders(): List<Reminder>

    @Query("SELECT * FROM reminders WHERE type = :type ORDER BY nextReminderTime ASC")
    fun getRemindersByType(type: ReminderType): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    fun getReminderById(id: Long): LiveData<Reminder>

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    suspend fun getReminderByIdSync(reminderId: Long): Reminder?

    @Query("SELECT * FROM reminders WHERE nextReminderTime < :currentTime AND isActive = 1")
    suspend fun getDueReminders(currentTime: Date): List<Reminder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: Reminder): Long

    @Update
    suspend fun update(reminder: Reminder)

    @Query("UPDATE reminders SET isActive = :isActive WHERE id = :id")
    suspend fun updateActiveStatus(id: Long, isActive: Boolean)

    @Query("UPDATE reminders SET nextReminderTime = :nextTime WHERE id = :id")
    suspend fun updateNextReminderTime(id: Long, nextTime: Date)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteById(id: Long)

}