package com.bintina.goouttolunchmvvm.utils

import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY

fun openSearchActivity(){

}

fun openCoworkerActivity(){

}

fun openRestaurantListActivity(){

}

fun openRestaurantMapActivity(){

}

fun convertRawUrlToUrl(rawUrl: String, width: String, photoReference: String): String?{
    val regex = """href\s*=\s*["'](https?://[^\s"']+)["']""".toRegex()
    val matchResult = regex.find(rawUrl)

    val contributorUrl = matchResult?.groups?.get(1)?.value
    val widthString= "?maxwidth=$width"
    val photoReferenceString = "&photoreference=$photoReference"
    val apiKey = "&key=${MAPS_API_KEY}"

    val photoReference = "$rawUrl$widthString$photoReferenceString$apiKey"

    return photoReference
}