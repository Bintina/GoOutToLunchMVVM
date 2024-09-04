package com.bintina.goouttolunchmvvm.model.fcm

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
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveRestaurantToRoom
import com.bintina.goouttolunchmvvm.user.viewmodel.saveLocalUserToRoom
import com.bintina.goouttolunchmvvm.utils.MainActivity
import com.bintina.goouttolunchmvvm.utils.fetchAndUpdateRestaurant
import com.bintina.goouttolunchmvvm.utils.fetchAndUpdateUser
import com.bintina.goouttolunchmvvm.utils.fetchAndUpdateUserWithRestaurant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

 class BaseFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG  = "BaseFCMServiceLog"

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived() called. Message: $message. From: ${message.from}")

        if (message.data.isNotEmpty()) {
            val title = message.data["title"] ?: "Default Title"
            val body = message.data["message"] ?: "Default Message"

            val userId = message.data["userId"]
            val restaurantId = message.data["restaurantId"]
            val userWithRestaurantUid = message.data["uid"]

            if (userId != null) {
                fetchAndUpdateUser(userId)
            }

            if (restaurantId != null) {
                fetchAndUpdateRestaurant(restaurantId)
            }
            if (userWithRestaurantUid != null) {
                fetchAndUpdateUserWithRestaurant(userWithRestaurantUid)
            }

            val heading = message.data["heading"]
            val messageText = message.data["message"]
            if (heading != null && messageText != null){
                sendNotification(heading, messageText)
            handleCustomMessage(heading, messageText, message)
            }
        } else {
            Log.d(TAG, "Message data is empty" )
        }

        super.onMessageReceived(message)
    }

    fun handleCustomMessage(title: String, body: String, message: RemoteMessage){
Log.d(TAG, " title: $title, body: $body, message: $message")
    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "New token received: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "sendRegistrationToServer() called. Token: $token")
        // Logic to send token to your server
    }

    private fun sendNotification(messageTitle: String, messageBody: String) {
        val requestCode = 0

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            action = "com.google.firebase.MESSAGING_EVENT"
            putExtra("message_title", messageTitle)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create Channel Id
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
            .setContentTitle(messageTitle)
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

}