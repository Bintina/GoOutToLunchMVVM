package com.bintina.goouttolunchmvvm.utils

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.user.login.viewmodel.LoginViewModel
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentDate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date



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
