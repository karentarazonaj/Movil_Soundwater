package com.example.movil_soundwater.ui.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.data.model.ReminderType
import com.example.movil_soundwater.databinding.FragmentIntervalReminderBinding
import com.example.movil_soundwater.ui.viewmodels.ReminderViewModel
import com.google.android.material.tabs.TabLayout

class IntervalReminderFragment : Fragment() {

    private var _binding: FragmentIntervalReminderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntervalReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setupButtons()

        // Default selection
        binding.radio30min.isChecked = true
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    // Navigate to personalized reminder fragment
                    findNavController().navigate(
                        IntervalReminderFragmentDirections
                            .actionIntervalReminderFragmentToPersonalizedReminderFragment()
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupButtons() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonCancelar.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonGuardar.setOnClickListener {
            saveReminder()
        }
    }

    private fun saveReminder() {
        val intervalMinutes = when {
            binding.radio30min.isChecked -> 30
            binding.radio1hora.isChecked -> 60
            binding.radio2horas.isChecked -> 120
            else -> 30 // Default
        }

        val reminder = Reminder(
            title = "Recordatorio de agua",
            description = "Es hora de tomar agua",
            type = ReminderType.INTERVAL,
            intervalMinutes = intervalMinutes,
            nextReminderTime = System.currentTimeMillis(),
            isActive = true
        )

        viewModel.insertReminder(reminder)
        findNavController().navigate(
            IntervalReminderFragmentDirections
                .actionIntervalReminderFragmentToReminderListFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}