package com.bintina.goouttolunchmvvm.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantListBinding
import com.bintina.goouttolunchmvvm.databinding.FragmentSettingsBinding
import com.bintina.goouttolunchmvvm.utils.MyApp

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

        saveNotificationSettingBoolean()

        return binding.root
    }

    private fun saveNotificationSettingBoolean() {
               Log.d(TAG, "notification toggle is ${binding.notificationToggle.isActivated} before action")
        binding.notificationToggle.setOnClickListener {
           if (binding.notificationToggle.isActivated){
            MyApp.getNotifications = true
           } else {
               MyApp.getNotifications = false
           }
               Log.d(TAG, "notification toggle is ${binding.notificationToggle.isActivated} after action")
        }
    }
}