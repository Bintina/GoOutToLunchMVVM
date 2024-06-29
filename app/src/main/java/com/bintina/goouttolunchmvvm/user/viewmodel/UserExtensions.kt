package com.bintina.goouttolunchmvvm.user.viewmodel

import android.util.Log
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Convert a FirebaseUser object to a LocalUser object
fun mapFirebaseUserToLocalUser(firebaseUser: FirebaseUser): LocalUser {
    val downloadDate = System.currentTimeMillis().toLong()
    var refreshedDate = System.currentTimeMillis().toLong()
    return LocalUser(
        displayName = firebaseUser.displayName.toString(),
        uid = firebaseUser.uid.toString(),
        email = firebaseUser.email.toString(),
        profilePictureUrl = firebaseUser.photoUrl.toString(),
        attending = null,
        createdAt = downloadDate,
        updatedAt = refreshedDate
    )
}

// Save a LocalUser object to the Room database
fun saveLocalUserToRoomDatabase(result: FirebaseUser?) {

    // Convert each User object to a LocalRestaurant object
    val localUser =
        mapFirebaseUserToLocalUser(result!!)
    Log.d("UserExtensionLog", "localUserList is $localUser")

    val localUserJson = userObjectToJson(localUser!!)
    CoroutineScope(Dispatchers.IO).launch {

        // Get the AppDatabase instance
        val db = MyApp.db

        // Save each LocalUser object to the database

        db.userDao().insert(localUserJson!!)
        Log.d("UserExtensionLog", "Inserting($localUser) called")
    }
}

fun saveUsersToRealtimeDatabase(
) {
    val databaseReference = Firebase.database.reference

    val db = MyApp.db
    val localUserList = fetchLocalUserList()
    Log.d("UserExtensionLog", "insertAll has been called. localUserList is $localUserList")
    Log.d("UserExtensionLog", "insertAll has been called")
    writeToRealtimeDatabaseExtension(localUserList, databaseReference)
    Log.d("UserExtensionLog", "writeToRealtimeDatabaseExtension called")

}



fun writeToRealtimeDatabaseExtension(localUserList: List<LocalUser>, databaseReference: DatabaseReference) {

    //Writing data to Firebase Realtime Database
    val firebaseUserId = databaseReference.push().key!!

    databaseReference.child("users").child(firebaseUserId).setValue(localUserList)
        .addOnCanceledListener {
            Log.d("UserExtensionLog", "Write to database canceled")
        }
        .addOnFailureListener {
            Log.d("UserExtensionLog", "Write to database failed")
        }

}
fun getUpdatedLocalUserList (){

}
fun fetchLocalUserList(): List<LocalUser>{
    var localUserList = listOf<LocalUser>()
    CoroutineScope(Dispatchers.IO).launch {
        val db = MyApp.db
        val localUserString = db.userDao().getAllUsers()
        localUserList = userJsonToObject(localUserString, LocalUser::class.java)
        Log.d("UserExtensionLog", "localUserList is $localUserList")

    }
    return localUserList
}
fun userObjectToJson(user: LocalUser): String {
    val attendingJsonString = Gson().toJson(user)

    return attendingJsonString
}

fun <T> userJsonToObject(attendingJsonString: String, clazz: Class<T>): List<T> {
    val typeToken = object : TypeToken<List<LocalUser>>() {}.type
    return Gson().fromJson(attendingJsonString, typeToken)
}
