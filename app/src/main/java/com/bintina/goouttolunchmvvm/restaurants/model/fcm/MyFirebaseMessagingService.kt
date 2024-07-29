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
import com.bintina.goouttolunchmvvm.utils.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FCMServiceLog"

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived() called.message is $message")

        //Check if data needs to be processed by long running job
        if (isLongRunningJob()) {
            scheduleJob()
        } else {
            handleNow()
        }

        //Check if message contains a notification payload.
        message.notification?.let {
            Log.d(TAG, "Message Notification body : ${it.body}")
            it.body?.let { body -> sendNotification(body)}
        }
        super.onMessageReceived(message)
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
    private fun sendNotification(messageBody: String) {
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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