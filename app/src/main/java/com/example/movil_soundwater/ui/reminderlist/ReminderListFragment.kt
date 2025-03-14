package com.example.movil_soundwater.ui.reminderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movil_soundwater.R
import com.example.movil_soundwater.databinding.FragmentReminderListBinding
import com.example.movil_soundwater.ui.viewmodels.ReminderViewModel

class ReminderListFragment : Fragment() {

    private var _binding: FragmentReminderListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReminderViewModel by viewModels()
    private lateinit var adapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = ReminderAdapter(
            onItemClick = { reminder ->
                val action = ReminderListFragmentDirections
                    .actionReminderListFragmentToReminderDetailFragment(reminder.id)
                findNavController().navigate(action)
            }
        )

        binding.recyclerViewReminders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReminderListFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.allReminders.observe(viewLifecycleOwner) { reminders ->
            adapter.submitList(reminders)
            binding.emptyView.visibility = if (reminders.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.fabAddReminder.setOnClickListener {
            showReminderTypeDialog()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showReminderTypeDialog() {
        val options = arrayOf("Intervalo", "Personalizado")

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar tipo de recordatorio")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> findNavController().navigate(
                        R.id.action_reminderListFragment_to_intervalReminderFragment
                    )
                    1 -> findNavController().navigate(
                        R.id.action_reminderListFragment_to_personalizedReminderFragment
                    )
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}