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
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getRestaurantUsers
import com.bintina.goouttolunchmvvm.utils.MainActivity
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationFirebaseMessagingService : BaseFirebaseMessagingService() {

    val TAG = "MyNotificationMessagingServiceLog"

    override fun handleCustomMessage(title: String, body: String, message: RemoteMessage) {
        Log.d(TAG, "Notification message received: Title: $title, Body: $body")
        sendNotification(title, body)
    }


    private fun sendNotification(messageTitle: String, messageBody: String) {
        val requestCode = 0
        val fullNotificationText = composeNotification()
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            action = "com.bintina.goouttolunchmvvm.SHOW_COWORKER_FRAGMENT"
            putExtra("message_detail", fullNotificationText)
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


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Check Build version to implement channel or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())

    }

    private fun composeNotification(): String {
        val currentUserName = MyApp.currentUser!!.displayName
        val currentRestaurant = MyApp.currentUserWithRestaurant.restaurant
        var fullNotificationText = ""

        if (currentRestaurant != null) {
            val currentUserRestaurantChoice = currentRestaurant.name
            val currentRestaurantVicinity = currentRestaurant.address

            listAttendingUsers { displayNames ->
                // Handle the list of display names here
                Log.d("AttendingUsers", "Attending users' names: $displayNames")

                // Example of updating the UI or performing another action
                if (displayNames.isNotEmpty()) {
                    // Update UI with the list of names, for example:
                    val attendingCoworkerNames = displayNames.joinToString(", ")
                    fullNotificationText =
                        "$currentUserName, you are going to $currentUserRestaurantChoice, $currentRestaurantVicinity. $attendingCoworkerNames will be joining you."
                } else {
                    // Handle case where no users are attending
                    val noUsersText = "No coworkers are joining you."
                    fullNotificationText =
                        "$currentUserName, you are going to $currentUserRestaurantChoice, $currentRestaurantVicinity. $noUsersText"
                }
            }
        } else {
            val noRestaurantText = "You have not chosen a restaurant today"
            fullNotificationText = noRestaurantText
        }

        return fullNotificationText

    }

    private fun listAttendingUsers(callback: (List<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUsersAttendingObjects =
                getRestaurantUsers(MyApp.currentUserWithRestaurant.restaurant!!.restaurantId)

            val currentUsersAttendingNames = currentUsersAttendingObjects
                .map { it?.displayName } // Transform each LocalUser to their displayName
                .filterNotNull() // Remove any null display names if applicable

            withContext(Dispatchers.Main) {
                callback(currentUsersAttendingNames) // Return the result via callback
            }
        }
    }

    private fun sendRegistrationToServer(token: String?) {
        //TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(messageBody: String) {
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


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Check Build version to implement channel or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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