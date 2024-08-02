package com.bintina.goouttolunchmvvm.restaurants.model.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MainActivity
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FCMServiceLog"

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived() called. Message: $message")

        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${message.data}")
            val title = message.data["title"] ?: "Default Title"
            val body = message.data["message"] ?: "Default Message"
            Log.d(TAG, "Data Message Title: $title, Body: $body")

            // Extract relevant identifiers and update local Room database
            val userId = message.data["userId"]
            val restaurantId = message.data["restaurantId"]

            if (userId != null) {
                // Fetch updated user data and update Room database
                fetchAndUpdateUser(userId)
            }

            if (restaurantId != null) {
                // Fetch updated restaurant data and update Room database
                fetchAndUpdateRestaurant(restaurantId)
            }

            sendNotification(title, body)
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            val title = it.title ?: "Default Title" // Provide a default title if null
            val body = it.body ?: "Default Message" // Provide a default body if null
            Log.d(TAG, "Notification Message Title: $title, Body: $body")
            sendNotification(title, body)
        }

        // Call the super method if necessary
        super.onMessageReceived(message)
    }

    private fun fetchAndUpdateUser(userId: String) {
        // Logic to fetch updated user data from Firebase Realtime Database
        // and update the local Room database
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(LocalUser::class.java)
                if (user != null) {
                    // Update Room database with the fetched user data
                    updateUserInRoomDatabase(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "Failed to fetch user data: ${databaseError.message}")
            }
        })
    }

    private fun fetchAndUpdateRestaurant(restaurantId: String) {
        // Logic to fetch updated restaurant data from Firebase Realtime Database
        // and update the local Room database
        val databaseReference = FirebaseDatabase.getInstance().getReference("restaurants").child(restaurantId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val restaurant = dataSnapshot.getValue(LocalRestaurant::class.java)
                if (restaurant != null) {
                    // Update Room database with the fetched restaurant data
                    updateRestaurantInRoomDatabase(restaurant)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "Failed to fetch restaurant data: ${databaseError.message}")
            }
        })
    }

    private fun updateUserInRoomDatabase(user: LocalUser) {
        // Logic to update the local Room database with the new user data
    }

    private fun updateRestaurantInRoomDatabase(restaurant: LocalRestaurant) {
        // Logic to update the local Room database with the new restaurant data
    }


    private fun isLongRunningJob() = true

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken() called. token is $token")
        sendRegistrationToServer(token)
    }

    //Dispatch job
    private fun scheduleJob() {
        Log.d(TAG, "scheduleJob() called")
        val work = OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()

    }

    private fun handleNow() {
        Log.d(TAG, "short lived task is done. handleNow() called")
    }


    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "sendRegistrationToServer() called. token is $token")
        //TODO("implement this method to send token to server")
    }

    //Create and show remote Notification
    private fun sendNotification(messageTitle: String, messageBody: String) {
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            action = "com.bintina.goouttolunchmvvm.SHOW_COWORKER_FRAGMENT"
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Create Channel Id
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Check Build version to implement channel or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())

    }
}