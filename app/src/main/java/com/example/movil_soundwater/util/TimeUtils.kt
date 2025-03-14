package com.example.movil_soundwater.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TimeUtils {

    fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
    }

    fun intervalToMinutes(intervalType: Int): Int {
        return when (intervalType) {
            0 -> 30      // 30 minutes
            1 -> 60      // 1 hour
            2 -> 120     // 2 hours
            else -> 30   // Default to 30 minutes
        }
    }

    fun calculateNextReminder(intervalMinutes: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, intervalMinutes)
        return calendar.time
    }

    fun calculateMinutesRemaining(targetTime: Date): Int {
        val now = Calendar.getInstance().time
        val diff = targetTime.time - now.time
        return (diff / (1000 * 60)).toInt()
    }

    fun getDefaultReminderTimes(): List<Pair<Int, Int>> {
        // Format: Hour, Minute pairs for default reminder times
        return listOf(
            Pair(6, 0),   // 6:00 AM
            Pair(8, 0),   // 8:00 AM
            Pair(10, 0),  // 10:00 AM
            Pair(12, 0),  // 12:00 PM
            Pair(14, 0),  // 2:00 PM
            Pair(16, 0),  // 4:00 PM
            Pair(18, 0),  // 6:00 PM
            Pair(20, 0),  // 8:00 PM
            Pair(22, 0)   // 10:00 PM
        )
    }

}