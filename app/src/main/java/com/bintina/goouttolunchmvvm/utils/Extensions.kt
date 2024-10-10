package com.bintina.goouttolunchmvvm.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getRealtimeRestaurants
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getUsersWithRestaurantsFromRealtime
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveRestaurantToRoom
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveRestaurantsToRealtimeDatabase
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveRestaurantsWithUsersToRealtimeDatabase
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveUserWithRestaurantToRealtimeDatabase
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveUsersWithRestaurantsToRoom
import com.bintina.goouttolunchmvvm.user.viewmodel.getRealtimeUsers
import com.bintina.goouttolunchmvvm.user.viewmodel.saveLocalUserToRoom
import com.bintina.goouttolunchmvvm.user.viewmodel.saveUsersToRealtimeDatabase
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentDate
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime


fun convertRawUrlToUrl(restaurant: Restaurant): String {
    val TAG = "ExtensionsLog"
    val rawUrl = "https://maps.googleapis.com/maps/api/place/photo"

    // Check if the restaurant has photos and if the list is not null
    val photos = restaurant.photos
    if (photos != null && photos.isNotEmpty()) {
        val photo = photos.first() // Get the first photo

        // Construct the URL parts
        val widthString = "?maxwidth=${photo.width}"
        val photoReferenceString = "&photoreference=${photo.photo_reference}"
        val apiKey = "&key=${MAPS_API_KEY}"

        // Concatenate the URL parts
        val concatenatedPhotoReference = "$rawUrl$widthString$photoReferenceString$apiKey"
        //Log.d(TAG, "concatenatedPhotoReference is $concatenatedPhotoReference")

        return concatenatedPhotoReference
    } else {
        Log.d(TAG, "No photos available for this restaurant or photos list is null.")
        return ""
    }
}
/*
fun rawToUrl(restaurant: Restaurant): String {
    val TAG = "ExtensionsLog"

    val photos = restaurant.photos
    if (photos.isNotEmpty() && photos.first().photo_reference != null) {
        val photoReference = photos.first().photo_reference
        val rawImageUrl = "https://maps.googleapis.com/maps/api/place/photo"
        val photoWidth = 400

        return convertRawUrlToUrl(rawImageUrl, photoWidth, photoReference)
    } else {
        Log.d(TAG, "No valid photo reference found")
        return ""
    }
}

fun getRaw(restaurant: com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant) {
    val TAG = "ExtensionsLog"
    val url = rawToUrl(restaurant)

    if (url.isNotEmpty()) {
        Log.d(TAG, "Generated URL: $url")
    } else {
        Log.d(TAG, "No URL generated due to missing photo reference.")
    }
}*/

/**
 * Initializes today's date.
 *
 * @return The current date.
 */

fun instantiateTodaysDate(): LocalDateTime {
    currentDate = LocalDateTime.now()

    return currentDate

}

//Image Loading
fun loadImage(url: String, imageView: ImageView) {
    Glide.with(imageView.context)
        .load(url)
        .placeholder(R.drawable.hungry_droid)
        .centerCrop()
        .into(imageView)
}

fun userListObjectToJson(attendingList: List<LocalUser>): String {
    Log.d("Debug", "Attending list size: ${attendingList.size}")
    for (user in attendingList) {
        Log.d("Debug", "User in attending list: $user. Attending list is $attendingList")
    }
    val attendingJsonString = Gson().toJson(attendingList)

    return attendingJsonString
}

fun userListJsonToObject(attendingJsonString: String): MutableList<LocalUser> {
    if (attendingJsonString.isEmpty()) return mutableListOf()
    val listType = object : TypeToken<List<LocalUser>>() {}.type
    return Gson().fromJson(attendingJsonString, listType)
}

fun isGooglePlayServicesAvailable(context: Context): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
    return resultCode == ConnectionResult.SUCCESS
}

fun saveLocalClassesToRoom(listUsers: List<LocalUser>, listRestaurants: List<LocalRestaurant>) {
    val db = MyApp.db
    CoroutineScope(Dispatchers.IO).launch {
        db.userDao().insertAll(listUsers)
        db.restaurantDao().insertAll(listRestaurants)
    }

}



suspend fun uploadToRealtime(
) {
    withContext(Dispatchers.IO) {
        //val db = MyApp.db
        Log.d("ExtensionsLog", "uploadToRealtime called")
        //Upload users
        saveUsersToRealtimeDatabase()
        Log.d("ExtensionsLog", "saveUsersToRealtimeDatabase called")

        //Upload restaurants
        saveRestaurantsToRealtimeDatabase()
        Log.d("ExtensionsLog", "saveRestaurantsToRealtimeDatabase called")

        saveUserWithRestaurantToRealtimeDatabase()
        Log.d("ExtensionsLog", "saveRestaurantsWithUsersToRealtimeDatabase called")
    }
}

fun downloadRealtimeUpdates() {

    getRealtimeUsers()
    getRealtimeRestaurants()
    getUsersWithRestaurantsFromRealtime()
}

//FCM Methods

fun fetchAndUpdateUser(userId: String) {
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
            Log.d("ExtensionsLog", "Failed to fetch user data: ${databaseError.message}")
        }
    })
}

fun fetchAndUpdateRestaurant(restaurantId: String) {
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
            Log.d("ExtensionsLog", "Failed to fetch restaurant data: ${databaseError.message}")
        }
    })
}
fun fetchAndUpdateUserWithRestaurant(uid: String) {
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
            Log.d("ExtensionsLog", "Failed to fetch userWithRestaurant data: ${databaseError.message}")
        }
    })
}

//Fetch FCM Registration Token

fun getFcmRegistrationToken(context: Context){
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("ExtensionsLog", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

        // Log and toast
        //val msg = getString(R.string.msg_token_fmt, token)
        Log.d("ExtensionsLog", token)
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    })
}

 fun signOut(context: Context) {
    Log.d("ExtensionsLog", "signOut called")
    com.firebase.ui.auth.AuthUI.getInstance()
        .signOut(context)
        .addOnCompleteListener {
            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
        }

}

