package com.example.movil_soundwater.util

import androidx.room.TypeConverter
import com.example.movil_soundwater.data.model.ReminderType
import java.util.Date

class RoomConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromReminderType(value: ReminderType): String {
        return value.name
    }

    @TypeConverter
    fun toReminderType(name: String): ReminderType {
        return try {
            ReminderType.valueOf(name)
        } catch (e: IllegalArgumentException) {
            ReminderType.INTERVAL
        }
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

}