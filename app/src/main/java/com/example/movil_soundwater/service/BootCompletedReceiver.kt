package com.example.movil_soundwater.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.movil_soundwater.data.database.ReminderDatabase
import com.example.movil_soundwater.data.repository.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            val database = ReminderDatabase.getDatabase(context)
            val reminderDao = database.reminderDao()
            val repository = ReminderRepository(reminderDao)

            val alarmManagerHelper = AlarmManagerHelper(context)

            CoroutineScope(Dispatchers.IO).launch {
                val activeReminders = repository.getAllActiveReminders()

                for (reminder in activeReminders) {
                    alarmManagerHelper.scheduleReminder(reminder)
                }
            }
        }
    }

}