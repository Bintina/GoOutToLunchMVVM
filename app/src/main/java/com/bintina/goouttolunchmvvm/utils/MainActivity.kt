package com.bintina.goouttolunchmvvm.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.work.WorkManager
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ActivityMainBinding
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getWorkManagerStartDelay


import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.navController
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


open class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivityLog"
    private lateinit var databaseReference: DatabaseReference
    //private lateinit var outPutWorkInfoItems: LiveData<List<WorkInfo>>


    // WorkManager variables
    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted: Boolean ->
        if (isGranted){
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if Google Play Services are available
        if (isGooglePlayServicesAvailable(this)) {
            Log.d(TAG, "Google Play Services are available")
        } else {
            // Handle the case where Google Play Services are not available
            Log.d(TAG, "Google Play Services are not available")
        }


        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)
        //Customize the toolbar color
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.google.android.material.R.color.design_default_color_secondary
            )
        )
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        MyApp.navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        instantiateTodaysDate()


        // Write a message to the database
        databaseReference = Firebase.database.reference
        //readFromRealtimeDatabase()

        // Initialize WorkManager and LiveData
        //outPutWorkInfoItems = workManager.getWorkInfosByTagLiveData("restaurant")
        resetUserChoicesWork()
        //observeWorkStatus()
        downloadRealtimeUpdates()

        Log.d(TAG, "Fragment committed")

        //Handle incoming intent
        handleIntent(intent)

        // Check if the intent has the detailed message
        intent?.let {
            val messageDetail = it.getStringExtra("message_detail")
            if (!messageDetail.isNullOrEmpty()) {
                // Show the dialog with the message detail
                val dialog = NotificationDialog.newInstance(messageDetail)
                dialog.show(supportFragmentManager, "NotificationDetailDialog")
            }
        }

        askNotificationPermission()
        getFcmRegistrationToken(this)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == "com.bintina.goouttolunchmvvm.SHOW_COWORKER_FRAGMENT")
            navController.navigate(R.id.notification_dialog_dest)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun readFromRealtimeDatabase() {
        // Read from the database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue()
                Log.d(TAG, "Realtime Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read Realtime value.", error.toException())
            }
        })
    }

    //Initialize the contents of the Activity's standard options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //Called when menu item is selected.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.coworker_btn -> {
                navController.navigate(R.id.coworkers_dest)

                return true
            }

            R.id.restaurant_list_btn -> {
                navController.navigate(R.id.restaurant_list_dest)

                return true
            }

            R.id.restaurant_map_btn -> {
                navController.navigate(R.id.restaurant_map_dest)
                Log.d(TAG, "navigate to Maps Fragment called.")
                return true
            }

            R.id.sign_out_btn -> {
                signOut()
                navController.navigate(R.id.login_dest)

                return true
            }

            R.id.settings_btn -> {
                navController.navigate(R.id.settings_dest)

                return true
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }

    private fun signOut() {
        Log.d(TAG, "signOut called")
        com.firebase.ui.auth.AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
            }

    }



    private fun resetUserChoicesWork() {
        val initialDelay = getWorkManagerStartDelay()

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
                ){
                //can post notifications
            } else {
                //Ask for permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
 /*   private fun observeWorkStatus() {
        outPutWorkInfoItems.observe(this) { workInfos ->
            if (workInfos.isNullOrEmpty()) {
                return@observe
            }

            // Check the status of the work
            val workInfo = workInfos[0]
            when (workInfo.state) {
                WorkInfo.State.ENQUEUED -> {
                    Log.d(TAG, "Work is enqueued with state: ${workInfo.state}")
                    // Do something when the work is enqueued
                }

                WorkInfo.State.RUNNING -> {
                    Log.d(TAG, "Work is running with state: ${workInfo.state}")
                    // Do something when the work is running
                }

                WorkInfo.State.SUCCEEDED -> {
                    Log.d(TAG, "Work finished with state: ${workInfo.state}")
                    // Do something when the work is finished

                }

                WorkInfo.State.FAILED -> {
                    Log.d(TAG, "Work failed with state: ${workInfo.state}")
                    // Do something when the work has failed
                }

                WorkInfo.State.BLOCKED -> {
                    Log.d(TAG, "Work is blocked with state: ${workInfo.state}")
                    // Do something when the work is blocked
                }

                WorkInfo.State.CANCELLED -> {
                    Log.d(TAG, "Work is cancelled with state: ${workInfo.state}")
                    // Do something when the work is cancelled
                }
            }
        }
    }*/

}