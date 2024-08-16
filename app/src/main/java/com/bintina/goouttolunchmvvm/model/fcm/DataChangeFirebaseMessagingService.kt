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
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DataChangeFirebaseMessagingService : BaseFirebaseMessagingService() {
    val TAG = "FCMServiceLog"



    override fun handleCustomMessage(title: String, body: String, message: RemoteMessage) {
        Log.d(TAG, "Data change received: Title: $title, Body: $body")
        // No local notification, only data processing
    }

}