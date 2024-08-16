package com.bintina.goouttolunchmvvm.restaurants.model.fcm

import android.util.Log
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveRestaurantToRoom
import com.bintina.goouttolunchmvvm.user.viewmodel.saveLocalUserToRoom
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

abstract class BaseFirebaseMessagingService: FirebaseMessagingService() {
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

            handleCustomMessage(title, body, message)
        }

        super.onMessageReceived(message)
    }

    abstract fun handleCustomMessage(title: String, body: String, message: RemoteMessage)

    private fun fetchAndUpdateUser(userId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(LocalUser::class.java)
                if (user != null) {
                    CoroutineScope(Dispatchers.IO).launch {

                    saveLocalUserToRoom(user)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "Failed to fetch user data: ${databaseError.message}")
            }
        })
    }

    private fun fetchAndUpdateRestaurant(restaurantId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("restaurants").child(restaurantId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val restaurant = dataSnapshot.getValue(LocalRestaurant::class.java)
                if (restaurant != null) {
                 CoroutineScope(Dispatchers.IO).launch {
                    saveRestaurantToRoom(restaurant)
                 }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "Failed to fetch restaurant data: ${databaseError.message}")
            }
        })
    }
    private fun fetchAndUpdateUserWithRestaurant(uid: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("userWithRestaurant").child(uid)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userWithRestaurant = dataSnapshot.getValue(LocalRestaurant::class.java)
                if (userWithRestaurant != null) {
                 CoroutineScope(Dispatchers.IO).launch {
                    saveRestaurantToRoom(userWithRestaurant)
                 }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "Failed to fetch userWithRestaurant data: ${databaseError.message}")
            }
        })
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "New token received: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "sendRegistrationToServer() called. Token: $token")
        // Logic to send token to your server
    }
}