package com.example.movil_soundwater.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.data.model.ReminderType
import java.util.Calendar

class AlarmManagerHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleReminder(reminder: Reminder) {
        if (!reminder.isActive) return

        when (reminder.type) {
            ReminderType.INTERVAL -> scheduleIntervalReminder(reminder)
            ReminderType.PERSONALIZED -> schedulePersonalizedReminder(reminder)
        }
    }

    private fun scheduleIntervalReminder(reminder: Reminder) {
        val intervalMinutes = reminder.intervalMinutes ?: return
        val intervalMillis = intervalMinutes * 60 * 1000L

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("REMINDER_ID", reminder.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // Handle lack of SCHEDULE_EXACT_ALARM permission
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + intervalMillis,
                intervalMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + intervalMillis,
                pendingIntent
            )
        }
    }

    private fun schedulePersonalizedReminder(reminder: Reminder) {
        val times = reminder.scheduledTimes ?: return

        for ((index, timeString) in times.withIndex()) {
            val (hour, minute) = parseTimeString(timeString)

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)

                // If time has already passed today, schedule for tomorrow
                if (timeInMillis < System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("REMINDER_ID", reminder.id)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                (reminder.id.toInt() * 100) + index,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun parseTimeString(timeString: String): Pair<Int, Int> {
        // Format: "HH:MM AM/PM"
        val parts = timeString.split(" ")
        val timeParts = parts[0].split(":")

        var hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        // Convert to 24-hour format
        if (parts[1] == "PM" && hour < 12) {
            hour += 12
        } else if (parts[1] == "AM" && hour == 12) {
            hour = 0
        }

        return Pair(hour, minute)
    }

    fun cancelReminder(reminder: Reminder) {
        val intent = Intent(context, ReminderReceiver::class.java)

        when (reminder.type) {
            ReminderType.INTERVAL -> {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    reminder.id.toInt(),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
                )

                pendingIntent?.let { alarmManager.cancel(it) }
            }
            ReminderType.PERSONALIZED -> {
                val times = reminder.scheduledTimes ?: return

                for (index in times.indices) {
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        (reminder.id.toInt() * 100) + index,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
                    )

                    pendingIntent?.let { alarmManager.cancel(it) }
                }
            }
        }
    }

}