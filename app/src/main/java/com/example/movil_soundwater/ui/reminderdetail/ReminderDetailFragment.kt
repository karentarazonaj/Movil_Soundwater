package com.example.movil_soundwater.ui.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movil_soundwater.data.model.ReminderType
import com.example.movil_soundwater.databinding.FragmentReminderDetailBinding
import com.example.movil_soundwater.ui.viewmodels.ReminderViewModel

class ReminderDetailFragment : Fragment() {

    private var _binding: FragmentReminderDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReminderViewModel by viewModels()
    private val args: ReminderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        loadReminderDetails()
    }

    private fun setupButtons() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonDesactivar.setOnClickListener {
            viewModel.getReminderById(args.reminderId).observe(viewLifecycleOwner) { reminder ->
                reminder?.let {
                    val updatedReminder = it.copy(isActive = !it.isActive)
                    viewModel.updateReminder(updatedReminder)
                    updateButtonState(updatedReminder.isActive)
                }
            }
        }

        binding.buttonPermisos.setOnClickListener {
            // Navigate to permissions screen or show dialog
        }
    }

    private fun loadReminderDetails() {
        viewModel.getReminderById(args.reminderId).observe(viewLifecycleOwner) { reminder ->
            reminder?.let {
                binding.textReminderTitle.text = it.title

                val detailText = when (it.type) {
                    ReminderType.INTERVAL -> {
                        val minutes = it.intervalMinutes ?: 0
                        "Cada ${formatInterval(minutes)}"
                    }
                    ReminderType.PERSONALIZED -> {
                        val nextTime = it.scheduledTimes?.firstOrNull() ?: "No programado"
                        "Próximo: $nextTime"
                    }
                }

                binding.textReminderDetail.text = detailText
                updateButtonState(it.isActive)
            }
        }
    }

    private fun formatInterval(minutes: Int): String {
        return when {
            minutes < 60 -> "$minutes min"
            minutes == 60 -> "1 hora"
            else -> "${minutes / 60} horas"
        }
    }

    private fun updateButtonState(isActive: Boolean) {
        binding.buttonDesactivar.text = if (isActive) "Desactivar" else "Activar"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}