package com.example.movil_soundwater.ui.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movil_soundwater.data.model.Reminder
import com.example.movil_soundwater.data.model.ReminderType
import com.example.movil_soundwater.databinding.FragmentPersonalizedReminderBinding
import com.example.movil_soundwater.ui.viewmodels.ReminderViewModel
import com.google.android.material.tabs.TabLayout

class PersonalizedReminderFragment : Fragment() {

    private var _binding: FragmentPersonalizedReminderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReminderViewModel by viewModels()
    private lateinit var timeChipAdapter: TimeChipAdapter

    private val selectedTimes = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setupRecyclerView()
        setupButtons()
    }

    private fun setupTabLayout() {
        binding.tabLayout.getTabAt(1)?.select()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    // Navigate to interval reminder fragment
                    findNavController().navigate(
                        PersonalizedReminderFragmentDirections
                            .actionPersonalizedReminderFragmentToIntervalReminderFragment()
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        val suggestedTimes = listOf(
            "06:00 AM", "08:00 AM", "10:00 AM",
            "12:00 PM", "02:00 PM", "04:00 PM",
            "06:00 PM", "08:00 PM", "10:00 PM"
        )

        timeChipAdapter = TimeChipAdapter(
            times = suggestedTimes,
            onTimeSelected = { time, isSelected ->
                if (isSelected) {
                    selectedTimes.add(time)
                } else {
                    selectedTimes.remove(time)
                }
            }
        )

        binding.recyclerTimeChips.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = timeChipAdapter
        }
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
        if (selectedTimes.isEmpty()) {
            // Show error message
            return
        }

        val reminder = Reminder(
            title = "Recordatorio de agua",
            description = "Es hora de tomar agua",
            type = ReminderType.PERSONALIZED,
            scheduledTimes = selectedTimes,
            nextReminderTime = System.currentTimeMillis(),
            isActive = true
        )

        viewModel.insertReminder(reminder)
        findNavController().navigate(
            PersonalizedReminderFragmentDirections
                .actionPersonalizedReminderFragmentToReminderListFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}