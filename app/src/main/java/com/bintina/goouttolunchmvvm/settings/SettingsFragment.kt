package com.bintina.goouttolunchmvvm.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bintina.goouttolunchmvvm.databinding.FragmentSettingsBinding
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.downloadPlacesRestaurantList
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.downloadRealtimeUpdates
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment: Fragment() {

val TAG = "SettingsFragLog"
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupNotificationToggle()
binding.resetBtn.setOnClickListener {
    CoroutineScope(Dispatchers.IO).launch {

    downloadPlacesRestaurantList()
        Toast.makeText(requireContext(), "You're local Restaurants are being downloaded.", Toast.LENGTH_LONG).show()
    }
}
        return binding.root
    }

    private fun setupNotificationToggle() {
        // Set the default state of the ToggleButton
        binding.notificationToggle.isChecked = true // Default state to subscribed
        MyApp.getNotifications = true

        // Log the initial state
        Log.d(TAG, "Initial notification toggle state is ${binding.notificationToggle.isChecked}")

        binding.notificationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // User is subscribing to notifications
                FirebaseMessaging.getInstance().subscribeToTopic("PushNotification")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Subscribed to topic 'PushNotification'")
                            MyApp.getNotifications = true
                        } else {
                            Log.e(TAG, "Failed to subscribe", task.exception)
                            binding.notificationToggle.isChecked = false // Revert the state if failed
                        }
                    }
            } else {
                // User is unsubscribing from notifications
                FirebaseMessaging.getInstance().unsubscribeFromTopic("PushNotification")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Unsubscribed from topic 'PushNotification'")
                            MyApp.getNotifications = false
                        } else {
                            Log.e(TAG, "Failed to unsubscribe", task.exception)
                            binding.notificationToggle.isChecked = true // Revert the state if failed
                        }
                    }
            }

            // Log the state after action
            Log.d(TAG, "Notification toggle state after action is ${binding.notificationToggle.isChecked}")
        }
    }
}