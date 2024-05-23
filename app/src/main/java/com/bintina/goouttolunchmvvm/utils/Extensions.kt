package com.bintina.goouttolunchmvvm.utils

import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

fun openSearchActivity() {

}

fun openCoworkerActivity() {

}

fun openRestaurantListActivity() {

}

fun openRestaurantMapActivity() {

}

fun convertRawUrlToUrl(rawUrl: String, width: String, photoReference: String): String? {
/*    val regex = """href\s*=\s*["'](https?://[^\s"']+)["']""".toRegex()
    val matchResult = regex.find(rawUrl)*/

    val widthString = "?maxwidth=$width"
    val photoReferenceString = "&photoreference=$photoReference"
    val apiKey = "&key=${MAPS_API_KEY}"

    val concatenatedPhotoReference = "$rawUrl$widthString$photoReferenceString$apiKey"

    return concatenatedPhotoReference
}

/**
 * Initializes today's date.
 *
 * @return The current date.
 */

fun instantiateTodaysDate(): LocalDateTime {


    currentDate = LocalDateTime.now()


    return currentDate

}