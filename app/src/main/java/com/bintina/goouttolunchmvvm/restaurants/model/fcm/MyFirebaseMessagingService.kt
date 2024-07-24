package com.bintina.goouttolunchmvvm.restaurants.model.fcm

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bintina.goouttolunchmvvm.utils.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FCMServiceLog"

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived() called.message is $message")
        super.onMessageReceived(message)
    }

    private fun isLongRunningJob() = true

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken() called. token is $token")
        sendRegistrationToServer(token)
    }

    //Dispatch job
    private fun scheduleJob(){
        Log.d(TAG, "scheduleJob() called")
        val work = OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()
            WorkManager.getInstance(this).beginWith(work).enqueue()

    }

    private fun handleNow(){
        Log.d(TAG, "short lived task is done. handleNow() called")
    }


    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "sendRegistrationToServer() called. token is $token")
        //TODO("implement this method to send token to server")
    }

    //Create and show Notification
    privat fun sendNotification(messageBody: String) {
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // TODO("Create channel Id")
        // TODO("fetch notification tone reference")
        // TODO("Build notification")
    }
}