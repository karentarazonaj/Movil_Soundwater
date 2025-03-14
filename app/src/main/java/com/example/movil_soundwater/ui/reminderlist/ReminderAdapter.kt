package com.example.movil_soundwater.ui.reminderlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.data.model.ReminderType
import com.example.movil_soundwater.databinding.ItemReminderBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ReminderAdapter(
    private val onItemClick: (Reminder) -> Unit
) : ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReminderViewHolder(
        private val binding: ItemReminderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(reminder: Reminder) {
            binding.textReminderTitle.text = reminder.title

            val typeText = when (reminder.type) {
                ReminderType.INTERVAL -> {
                    val minutes = reminder.intervalMinutes ?: 0
                    when {
                        minutes == 30 -> "Cada 30 minutos"
                        minutes == 60 -> "Cada 1 hora"
                        minutes == 120 -> "Cada 2 horas"
                        else -> "Intervalo personalizado"
                    }
                }
                ReminderType.PERSONALIZED -> {
                    val times = reminder.scheduledTimes?.size ?: 0
                    "Personalizado ($times horarios)"
                }
            }

            binding.textReminderType.text = typeText

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.textReminderDate.text = dateFormat.format(reminder.createdAt)

            binding.switchReminderActive.isChecked = reminder.isActive
        }
    }

    class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem == newItem
        }
    }

}