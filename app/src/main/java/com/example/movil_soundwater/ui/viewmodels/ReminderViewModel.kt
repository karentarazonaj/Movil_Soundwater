package com.example.movil_soundwater.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.movil_soundwater.data.database.ReminderDatabase
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.data.model.ReminderType
import com.example.movil_soundwater.data.repository.ReminderRepository
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReminderRepository
    val allReminders: LiveData<List<Reminder>>

    init {
        val reminderDao = ReminderDatabase.getDatabase(application).reminderDao()
        repository = ReminderRepository(reminderDao)
        allReminders = repository.allReminders
    }

    fun getReminderById(id: Long): LiveData<Reminder> {
        return repository.getReminderById(id)
    }

    fun getRemindersByType(type: ReminderType): LiveData<List<Reminder>> {
        return repository.getRemindersByType(type)
    }

    fun insertReminder(reminder: Reminder) = viewModelScope.launch {
        repository.insert(reminder)
    }

    fun updateReminder(reminder: Reminder) = viewModelScope.launch {
        repository.update(reminder)
    }

    fun deleteReminder(reminder: Reminder) = viewModelScope.launch {
        repository.delete(reminder)
    }

    fun deleteReminderById(id: Long) = viewModelScope.launch {
        repository.deleteById(id)
    }

}