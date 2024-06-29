package com.bintina.goouttolunchmvvm.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentDate
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

fun objectToJson(attendingList: List<LocalUser>): String {
    val attendingJsonString = Gson().toJson(attendingList)

    return attendingJsonString
}

fun <T> jsonToObject(attendingJsonString: String, clazz: Class<T>): List<T> {
    val typeToken = TypeToken.getParameterized(List::class.java, clazz).type
    return Gson().fromJson(attendingJsonString, typeToken)
}

fun isGooglePlayServicesAvailable(context: Context): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
    return resultCode == ConnectionResult.SUCCESS
}

