package com.bintina.goouttolunchmvvm.utils

import android.util.Log
import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentDate
import com.google.firebase.auth.FirebaseUser
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
        Log.d(TAG, "concatenatedPhotoReference is $concatenatedPhotoReference")

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
fun mapFirebaseUserToUser(firebaseUser: FirebaseUser): LocalUser {
    return LocalUser(
        displayName = firebaseUser.displayName.toString(),
        uid = firebaseUser.uid.toString(),
        email = firebaseUser.email.toString(),
        profilePictureUrl = firebaseUser.photoUrl.toString()
    )
}