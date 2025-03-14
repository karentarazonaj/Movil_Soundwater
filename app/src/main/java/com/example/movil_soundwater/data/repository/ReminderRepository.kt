package com.example.movil_soundwater.data.repository

import androidx.lifecycle.LiveData
import com.example.movil_soundwater.data.database.ReminderDao
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.data.model.ReminderType
import java.util.Date

class ReminderRepository(private val reminderDao: ReminderDao) {

    val allReminders: LiveData<List<Reminder>> = reminderDao.getAllReminders()

    suspend fun getAllActiveReminders(): List<Reminder> {
        return reminderDao.getAllActiveReminders()
    }

    fun getRemindersByType(type: ReminderType): LiveData<List<Reminder>> {
        return reminderDao.getRemindersByType(type)
    }

    fun getReminderById(id: Long): LiveData<Reminder> {
        return reminderDao.getReminderById(id)
    }

    suspend fun getReminderByIdSync(reminderId: Long): Reminder? {
        return reminderDao.getReminderByIdSync(reminderId)
    }

    suspend fun getDueReminders(currentTime: Date = Date()): List<Reminder> {
        return reminderDao.getDueReminders(currentTime)
    }

    suspend fun insert(reminder: Reminder): Long {
        return reminderDao.insert(reminder)
    }

    suspend fun update(reminder: Reminder) {
        reminderDao.update(reminder)
    }

    suspend fun updateActiveStatus(id: Long, isActive: Boolean) {
        reminderDao.updateActiveStatus(id, isActive)
    }

    suspend fun updateNextReminderTime(reminder: Reminder) {
        val nextTime = when (reminder.type) {
            ReminderType.INTERVAL -> {
                // For interval reminders, add the interval to current time
                val calendar = java.util.Calendar.getInstance()
                calendar.add(java.util.Calendar.MINUTE, reminder.intervalMinutes ?: 30)
                calendar.time
            }
            ReminderType.PERSONALIZED -> {
                // For personalized reminders, find the next scheduled time
                findNextScheduledTime(reminder)
            }
        }

        reminderDao.updateNextReminderTime(reminder.id, nextTime)
    }

    private fun findNextScheduledTime(reminder: Reminder): Date {
        val currentTimeMillis = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance()
        val currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(java.util.Calendar.MINUTE)

        // Find the next scheduled time from the list
        var nextTime: Date? = null
        reminder.scheduledTimes?.forEach { timeString ->
            val timeParts = timeString.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            calendar.time = Date()
            calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
            calendar.set(java.util.Calendar.MINUTE, minute)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)

            // If time has passed for today, set for tomorrow
            if (hour < currentHour || (hour == currentHour && minute <= currentMinute)) {
                calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
            }

            if (nextTime == null || calendar.time.before(nextTime)) {
                nextTime = calendar.time
            }
        }

        return nextTime ?: Date(currentTimeMillis + 24 * 60 * 60 * 1000) // Default to tomorrow if no times found
    }

    suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder)
    }

    suspend fun deleteById(id: Long) {
        reminderDao.deleteById(id)
    }

}