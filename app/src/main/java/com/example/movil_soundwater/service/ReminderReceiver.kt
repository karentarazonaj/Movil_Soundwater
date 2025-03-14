package com.example.movil_soundwater.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.movil_soundwater.data.database.ReminderDatabase
import com.example.movil_soundwater.data.repository.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra("REMINDER_ID", -1L)
        if (reminderId == -1L) return

        val database = ReminderDatabase.getDatabase(context)
        val reminderDao = database.reminderDao()
        val repository = ReminderRepository(reminderDao)

        val notificationService = NotificationService(context)
        val alarmManagerHelper = AlarmManagerHelper(context)

        CoroutineScope(Dispatchers.IO).launch {
            val reminder = repository.getReminderByIdSync(reminderId)

            reminder?.let {
                if (it.isActive) {
                    // Show notification
                    notificationService.showNotification(it)

                    // For interval reminders, schedule the next one
                    if (it.type == com.example.movil_soundwater.data.model.ReminderType.INTERVAL) {
                        alarmManagerHelper.scheduleReminder(it)
                    }
                }
            }
        }
    }

}