package com.example.movil_soundwater.ui.reminderdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movil_soundwater.databinding.TimeChipItemBinding

class TimeChipAdapter(
    private val times: List<String>,
    private val onTimeSelected: (String, Boolean) -> Unit
) : RecyclerView.Adapter<TimeChipAdapter.TimeChipViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeChipViewHolder {
        val binding = TimeChipItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeChipViewHolder, position: Int) {
        holder.bind(times[position], selectedPositions.contains(position))
    }

    override fun getItemCount(): Int = times.size

    inner class TimeChipViewHolder(
        private val binding: TimeChipItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val isSelected = !selectedPositions.contains(position)
                    if (isSelected) {
                        selectedPositions.add(position)
                    } else {
                        selectedPositions.remove(position)
                    }
                    binding.chipTime.isChecked = isSelected
                    onTimeSelected(times[position], isSelected)
                }
            }
        }

        fun bind(time: String, isSelected: Boolean) {
            binding.chipTime.text = time
            binding.chipTime.isChecked = isSelected
        }
    }

}