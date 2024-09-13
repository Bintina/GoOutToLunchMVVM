package com.bintina.goouttolunchmvvm.utils

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bintina.goouttolunchmvvm.databinding.ActivityNavigationBinding

class NotificationActivity: AppCompatActivity() {

    lateinit var binding: ActivityNavigationBinding
    private val TAG = "NotificationActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

       showDialogFragment()

    }

/*    private fun handleIntent(intent: Intent) {
        Log.d(TAG, "handleIntent() called with action: ${intent.action}")
        if (intent.action == "OPEN_NOTIFICATION_ACTIVITY") {
            val messageTitle = intent.getStringExtra("message_title")
            if (messageTitle != null) {
                showDialogFragment(messageTitle)
            }
        }
    }*/

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
       /* setIntent(intent)
        handleIntent(intent)*/
    }
    private fun showDialogFragment() {
        // If the fragment is not already added, show it
        val currentFragment = supportFragmentManager.findFragmentByTag("NotificationDialog")
        if (currentFragment == null) {
            // Here, you can optionally get the title from the intent if needed
            val messageTitle = intent.getStringExtra("message_title") ?: "Default Title"

            // Create and show the dialog fragment
            val dialogFragment = NotificationDialog.newInstance(messageTitle)
            dialogFragment.show(supportFragmentManager, "NotificationDialog")
        } else {
            Log.d(TAG, "Dialog fragment already added")
        }
    }
}