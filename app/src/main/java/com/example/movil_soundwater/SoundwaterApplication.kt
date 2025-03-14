package com.example.movil_soundwater

import android.app.Application
import com.example.movil_soundwater.data.database.ReminderDatabase
import com.example.movil_soundwater.data.repository.ReminderRepository
import com.example.movil_soundwater.service.AlarmManagerHelper
import com.example.movil_soundwater.util.NotificationUtils
import kotlinx.coroutines.launch

class SoundwaterApplication : Application() {

    private lateinit var repository: ReminderRepository
    private lateinit var alarmManagerHelper: AlarmManagerHelper
    private lateinit var notificationUtils: NotificationUtils

    override fun onCreate() {
        super.onCreate()

        // Initialize database and repository
        val database = ReminderDatabase.getDatabase(this)
        repository = ReminderRepository(database.reminderDao())

        // Initialize notification utils
        notificationUtils = NotificationUtils()
        notificationUtils.createNotificationChannel(this)

        // Initialize alarm manager helper
        alarmManagerHelper = AlarmManagerHelper(this)

        // Schedule all active reminders on app start
        scheduleActiveReminders()
    }

    private fun scheduleActiveReminders() {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val activeReminders = repository.getAllActiveReminders()
                activeReminders.forEach { reminder ->
                    alarmManagerHelper.scheduleReminder(reminder)
                }
            } catch (e: Exception) {
                // Log the error but don't crash the app
                android.util.Log.e("SoundwaterApp", "Error scheduling reminders: ${e.message}")
            }
        }
    }

}