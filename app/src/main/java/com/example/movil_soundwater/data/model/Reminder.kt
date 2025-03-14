package com.example.movil_soundwater.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "Beber Agua",
    val description: String = "Recordatorio para mantenerte hidratado",
    val type: ReminderType,
    val intervalMinutes: Int? = null,
    val scheduledTimes: List<String>? = null,
    val nextReminderTime: Long,
    val isActive: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrate: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
