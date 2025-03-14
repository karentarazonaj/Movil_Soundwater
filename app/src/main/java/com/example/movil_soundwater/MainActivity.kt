package com.example.movil_soundwater

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movil_soundwater.databinding.ActivityMainBinding
import com.example.movil_soundwater.util.NotificationUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Create notification channel
        val notificationUtils = NotificationUtils()
        notificationUtils.createNotificationChannel(this)

        // Set up navigation button listeners
        setupNavButtons()

        // Observe navigation to adjust UI elements
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.navigationBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.navigationBar.visibility = View.VISIBLE
                }
            }
        }

        // Check if opened from notification
        intent.extras?.getLong("REMINDER_ID", -1)?.let { reminderId ->
            if (reminderId != -1L) {
                // Navigate to reminder detail
                val bundle = Bundle().apply {
                    putLong("reminderId", reminderId)
                }
                navController.navigate(R.id.reminderDetailFragment, bundle)
            }
        }
    }

    private fun setupNavButtons() {
        binding.navBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.navCircleButton.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeFragment) {
                navController.navigate(R.id.homeFragment)
            }
        }

        binding.navSquareButton.setOnClickListener {
            // Could be used for another top-level destination
            if (navController.currentDestination?.id != R.id.reminderListFragment) {
                navController.navigate(R.id.reminderListFragment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}